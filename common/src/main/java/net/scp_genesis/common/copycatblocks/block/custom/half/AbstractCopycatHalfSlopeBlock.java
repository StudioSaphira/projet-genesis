package net.scp_genesis.common.copycatblocks.block.custom.half;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.*;
import net.scp_genesis.common.copycatblocks.block.AbstractCopycatBlock;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.geometry.slope.*;
import net.scp_genesis.common.copycatblocks.util.CopycatHalfSlopeTransitions;
import net.scp_genesis.common.registry.ModItems;
import org.jetbrains.annotations.NotNull;

/** Common gameplay and targeting for the single and both double half-slope arrangements. */
public abstract class AbstractCopycatHalfSlopeBlock extends AbstractCopycatBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<CopycatSlopeSide> SIDE = EnumProperty.create("side", CopycatSlopeSide.class);
    protected AbstractCopycatHalfSlopeBlock(Properties properties) { super(properties); }
    /** Identifies which orientation properties and material slots this block supports. */
    public abstract CopycatHalfSlopeForm form();

    @Override protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                                    @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return CopycatHalfSlopeShapes.get(state);
    }
    // A multipart shape must not occlude an entire neighbor when only one material is opaque.
    @Override protected @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state,
                                                             @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }
    @Override public boolean isWrenchable() { return true; }
    @Override protected boolean isMultipartState(@NotNull BlockState state) { return form() != CopycatHalfSlopeForm.SINGLE; }

    /** Rotates the frame, leaving material slots attached to their geometric component. */
    @Override public InteractionResult onWrench(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide) level.setBlock(pos, CopycatHalfSlopeState.next(state), Block.UPDATE_ALL);
        return InteractionResult.SUCCESS;
    }

    /** Resolves lateral halves or diagonal regions using a point nudged inside the clicked face. */
    @Override public CopycatPart getCopycatPart(@NotNull BlockState state, @NotNull BlockHitResult hit) {
        if (form() == CopycatHalfSlopeForm.SINGLE) return CopycatPart.MAIN;
        Direction normal = hit.getDirection();
        Vec3 point = hit.getLocation().subtract(Vec3.atLowerCornerOf(hit.getBlockPos()))
                .subtract(normal.getStepX() * 1.0e-5, normal.getStepY() * 1.0e-5, normal.getStepZ() * 1.0e-5);
        Vec3 local = CopycatHalfSlopeState.local(point, state.getValue(FACING));
        return (form() == CopycatHalfSlopeForm.HORIZONTAL ? local.x < 0.5 : local.y + local.z <= 1)
                ? CopycatPart.BOTTOM : CopycatPart.TOP;
    }

    /** All mutations occur on the server; the client only acknowledges the interaction. */
    @Override public @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state,
            @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit) {
        if (stack.is(ModItems.COPYCAT_HALF_SLOPE.get()) && form() == CopycatHalfSlopeForm.SINGLE) {
            return CopycatHalfSlopeTransitions.merge(level, pos, state, player, stack, hit);
        }
        if (level.isClientSide) {
            var entity = getCopycatBlockEntity(level, pos);
            var part = getCopycatPart(state, hit);
            boolean tool = stack.getItem() instanceof net.scp_genesis.common.copycatblocks.item.CopycatWrenchItem
                    || stack.getItem() instanceof net.scp_genesis.common.copycatblocks.item.CopycatRemoverItem
                    || (stack.getItem() instanceof net.scp_genesis.common.copycatblocks.item.CopycatScraperItem
                        && entity != null && entity.hasCopiedState(part));
            var copied = net.scp_genesis.common.copycatblocks.api.CopycatBlocksAPI.getBlockStateFromItem(stack);
            boolean canCopy = entity != null && !entity.hasCopiedState(part)
                    && net.scp_genesis.common.copycatblocks.api.CopycatBlocksAPI.canCopy(level, pos, copied);
            return tool || canCopy ? ItemInteractionResult.SUCCESS
                    : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    @Override protected InteractionResult onRemovePart(Level level, BlockPos pos, @NotNull BlockState state,
            @NotNull CopycatPart removed, Player player, @NotNull ItemStack ignored, @NotNull ItemStack copied) {
        return CopycatHalfSlopeTransitions.remove(level, pos, state, removed, player, copied);
    }

    /** Picking any arrangement gives its building component, never an unobtainable double block. */
    @Override public @NotNull ItemStack getCloneItemStack(@NotNull LevelReader level, @NotNull BlockPos pos,
                                                         @NotNull BlockState state) {
        return new ItemStack(ModItems.COPYCAT_HALF_SLOPE.get());
    }
}
