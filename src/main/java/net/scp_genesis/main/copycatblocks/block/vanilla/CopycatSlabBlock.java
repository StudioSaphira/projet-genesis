package net.scp_genesis.main.copycatblocks.block.vanilla;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.scp_genesis.main.copycatblocks.api.CopycatBlocksAPI;
import net.scp_genesis.main.copycatblocks.block.AbstractCopycatBlock;

import net.scp_genesis.main.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.main.copycatblocks.data.CopycatPart;
import net.scp_genesis.main.copycatblocks.util.CopycatItemHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopycatSlabBlock extends AbstractCopycatBlock {

    public static final MapCodec<CopycatSlabBlock> CODEC =
            Block.simpleCodec(CopycatSlabBlock::new);

    public CopycatSlabBlock(Properties properties) {
        super(properties);

        registerDefaultState(
                stateDefinition.any()
                        .setValue(
                                BlockStateProperties.SLAB_TYPE,
                                SlabType.BOTTOM
                        )
                        .setValue(
                                BlockStateProperties.WATERLOGGED,
                                false
                        )
        );
    }

    @Override
    protected void createBlockStateDefinition(
            @NotNull StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(
                BlockStateProperties.SLAB_TYPE,
                BlockStateProperties.WATERLOGGED
        );
    }

    @Override
    public @NotNull BlockState getStateForPlacement(
            @NotNull BlockPlaceContext context
    ) {
        BlockPos pos = context.getClickedPos();
        FluidState fluidState =
                context.getLevel().getFluidState(pos);

        return defaultBlockState()
                .setValue(
                        BlockStateProperties.SLAB_TYPE,
                        getSlabType(context)
                )
                .setValue(
                        BlockStateProperties.WATERLOGGED,
                        fluidState.getType() == Fluids.WATER
                );
    }

    private SlabType getSlabType(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        BlockState existingState =
                context.getLevel().getBlockState(pos);

        if (existingState.getBlock() == this) {
            SlabType existingType =
                    existingState.getValue(
                            BlockStateProperties.SLAB_TYPE
                    );

            if (existingType == SlabType.BOTTOM
                    && context.getClickLocation().y - pos.getY() >= 0.5D) {
                return SlabType.DOUBLE;
            }

            if (existingType == SlabType.TOP
                    && context.getClickLocation().y - pos.getY() <= 0.5D) {
                return SlabType.DOUBLE;
            }
        }

        return context.getClickedFace() == Direction.DOWN
                ? SlabType.TOP
                : context.getClickedFace() == Direction.UP
                ? SlabType.BOTTOM
                : context.getClickLocation().y - pos.getY() > 0.5D
                ? SlabType.TOP
                : SlabType.BOTTOM;
    }

    @Override
    protected @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context
    ) {
        return switch (state.getValue(BlockStateProperties.SLAB_TYPE)) {
            case TOP -> Shapes.box(
                    0.0D, 0.5D, 0.0D,
                    1.0D, 1.0D, 1.0D
            );

            case DOUBLE -> Shapes.block();

            default -> Shapes.box(
                    0.0D, 0.0D, 0.0D,
                    1.0D, 0.5D, 1.0D
            );
        };
    }

    @Override
    public boolean isWrenchable() {
        return true;
    }

    @Override
    protected boolean isMultipartState(
            @NotNull BlockState state
    ) {
        return state.getValue(BlockStateProperties.SLAB_TYPE)
                == SlabType.DOUBLE;
    }

    @Override
    public InteractionResult onWrench(
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull BlockState state
    ) {
        SlabType currentType =
                state.getValue(
                        BlockStateProperties.SLAB_TYPE
                );

        /*
         * The Wrench only switches between BOTTOM and TOP.
         * DOUBLE is intentionally ignored.
         */
        if (currentType == SlabType.DOUBLE) {
            return InteractionResult.PASS;
        }

        CopycatBlockEntity blockEntity =
                level.getBlockEntity(pos) instanceof CopycatBlockEntity copycat
                        ? copycat
                        : null;

        if (blockEntity == null) {
            return InteractionResult.PASS;
        }

        CopycatPart sourcePart =
                currentType == SlabType.BOTTOM
                        ? CopycatPart.BOTTOM
                        : CopycatPart.TOP;

        CopycatPart targetPart =
                currentType == SlabType.BOTTOM
                        ? CopycatPart.TOP
                        : CopycatPart.BOTTOM;

        /*
         * Transfer the copied state to the new part.
         */
        BlockState copiedState =
                blockEntity.getCopiedState(sourcePart);

        blockEntity.setCopiedState(
                targetPart,
                copiedState
        );

        blockEntity.clearCopiedState(
                sourcePart
        );

        /*
         * Change the physical slab state.
         */
        SlabType newType =
                currentType == SlabType.BOTTOM
                        ? SlabType.TOP
                        : SlabType.BOTTOM;

        level.setBlock(
                pos,
                state.setValue(
                        BlockStateProperties.SLAB_TYPE,
                        newType
                ),
                Block.UPDATE_ALL
        );

        return InteractionResult.SUCCESS;
    }

    @Override
    protected InteractionResult onRemovePart(
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull BlockState state,
            @NotNull CopycatPart removedPart,
            @Nullable Player player,
            @NotNull ItemStack copycatItem,
            @NotNull ItemStack copiedBlockItem
    ) {
        CopycatBlocksAPI.clear(
                level,
                pos,
                removedPart
        );

        CopycatPart remainingPart =
                removedPart == CopycatPart.BOTTOM
                        ? CopycatPart.TOP
                        : CopycatPart.BOTTOM;

        SlabType newType =
                remainingPart == CopycatPart.BOTTOM
                        ? SlabType.BOTTOM
                        : SlabType.TOP;

        level.setBlock(
                pos,
                state.setValue(
                        BlockStateProperties.SLAB_TYPE,
                        newType
                ),
                Block.UPDATE_ALL
        );

        if (player != null && !player.isCreative()) {
            CopycatItemHelper.giveOrDrop(
                    player,
                    copycatItem
            );

            CopycatItemHelper.giveOrDrop(
                    player,
                    copiedBlockItem
            );
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected CopycatPart getCopycatPart(
            @NotNull BlockState state,
            @NotNull BlockHitResult hitResult
    ) {
        SlabType slabType =
                state.getValue(BlockStateProperties.SLAB_TYPE);

        if (slabType == SlabType.BOTTOM) {
            return CopycatPart.BOTTOM;
        }

        if (slabType == SlabType.TOP) {
            return CopycatPart.TOP;
        }

        /*
         * DOUBLE slab.
         *
         * Determine the targeted half from the hit position.
         */
        double y =
                hitResult.getLocation().y
                        - hitResult.getBlockPos().getY();

        return y >= 0.5D
                ? CopycatPart.TOP
                : CopycatPart.BOTTOM;
    }

    @Override
    protected boolean canBeReplaced(
            @NotNull BlockState state,
            @NotNull BlockPlaceContext context
    ) {
        SlabType slabType =
                state.getValue(BlockStateProperties.SLAB_TYPE);

        // A DOUBLE can no longer be replaced.
        if (slabType == SlabType.DOUBLE) {
            return false;
        }

        // Only the same Copycat Slab can merge with this one.
        if (!context.getItemInHand().is(this.asItem())) {
            return false;
        }

        // We must be clicking directly on the Slab.
        if (!context.replacingClickedOnBlock()) {
            return false;
        }

        Direction clickedFace = context.getClickedFace();

        /*
         * BOTTOM + click on the upper side
         * → DOUBLE
         */
        if (slabType == SlabType.BOTTOM
                && clickedFace == Direction.UP) {
            return true;
        }

        /*
         * TOP + click on the lower side
         * → DOUBLE
         */
        if (slabType == SlabType.TOP
                && clickedFace == Direction.DOWN) {
            return true;
        }

        return false;
    }

    @Override
    protected @NotNull MapCodec<? extends CopycatSlabBlock> codec() {
        return CODEC;
    }
}