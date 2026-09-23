package net.scp_genesis.common.copycatblocks.block.custom;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.scp_genesis.common.copycatblocks.block.AbstractCopycatBlock;

import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import org.jetbrains.annotations.NotNull;

import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatSlopeShapes;

/**
 * A material-copying 45-degree slope with eight orientations.
 * Gameplay is independent of loaders; collision and render geometry have separate owners.
 */
public class CopycatSlopeBlock extends AbstractCopycatBlock {
    public static final MapCodec<CopycatSlopeBlock> CODEC = Block.simpleCodec(CopycatSlopeBlock::new);
    /** Horizontal direction of the high edge in the bottom orientation. */
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    /** Whether the canonical slope is vertically reflected. */
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    /** Enables light occlusion only for an opaque copied material. */
    public static final BooleanProperty OCCLUDES = BooleanProperty.create("occludes");

    /** Creates a NORTH/BOTTOM slope without an occluding copied material. */
    public CopycatSlopeBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(HALF, Half.BOTTOM).setValue(OCCLUDES, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, OCCLUDES);
    }

    /** Uses the four-step physical approximation, independently of the rendered diagonal. */
    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                          @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return CopycatSlopeShapes.collision(state.getValue(FACING), state.getValue(HALF));
    }

    @Override
    protected boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return state.getValue(OCCLUDES);
    }

    /** Uses inset occlusion boundaries to avoid hiding faces along the diagonal. */
    @Override
    protected @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state,
                                                   @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.getValue(OCCLUDES)
                ? CopycatSlopeShapes.occlusion(state.getValue(FACING), state.getValue(HALF)) : Shapes.empty();
    }

    @Override
    protected void afterCopy(@NotNull CopycatBlockEntity blockEntity) {
        updateOcclusionState(blockEntity);
    }

    @Override
    protected void afterClear(@NotNull CopycatBlockEntity blockEntity) {
        updateOcclusionState(blockEntity);
    }

    /** Synchronizes the light-occlusion flag after a copied material changes. */
    @Override
    public void updateOcclusionState(@NotNull CopycatBlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        if (level == null) return;
        BlockPos pos = blockEntity.getBlockPos();
        BlockState state = level.getBlockState(pos);
        boolean occludes = !blockEntity.getCopiedState().isAir() && blockEntity.getCopiedState().canOcclude();
        if (state.getValue(OCCLUDES) != occludes) {
            level.setBlock(pos, state.setValue(OCCLUDES, occludes), Block.UPDATE_ALL);
        }
    }

    /** Chooses the half from click height and keeps the player's horizontal direction. */
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        Half half = context.getClickLocation().y - context.getClickedPos().getY() >= 0.5D
                ? Half.TOP : Half.BOTTOM;
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(HALF, half);
    }

    @Override
    public boolean isWrenchable() {
        return true;
    }

    /** Cycles NORTH/EAST/SOUTH/WEST, then flips the half, preserving the eight-state wrench cycle. */
    @Override
    public net.minecraft.world.InteractionResult onWrench(@NotNull Level level, @NotNull BlockPos pos,
                                                         @NotNull BlockState state) {
        Direction facing = state.getValue(FACING);
        if (facing == Direction.WEST) {
            state = state.setValue(FACING, Direction.NORTH)
                    .setValue(HALF, state.getValue(HALF) == Half.BOTTOM ? Half.TOP : Half.BOTTOM);
        } else {
            state = state.setValue(FACING, facing.getClockWise());
        }
        if (!level.isClientSide()) level.setBlock(pos, state, Block.UPDATE_ALL);
        return net.minecraft.world.InteractionResult.SUCCESS;
    }

    @Override
    protected @NotNull MapCodec<? extends CopycatSlopeBlock> codec() {
        return CODEC;
    }
}
