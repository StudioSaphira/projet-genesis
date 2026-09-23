package net.scp_genesis.common.copycatblocks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatHalfSlopeState;
import net.scp_genesis.common.registry.*;
import org.jetbrains.annotations.Nullable;
import static net.scp_genesis.common.copycatblocks.block.custom.half.AbstractCopycatHalfSlopeBlock.*;

/** Server-side merges and removal, preserving material identity across block-entity replacement. */
public final class CopycatHalfSlopeTransitions {
    private CopycatHalfSlopeTransitions() {}

    /** Chooses a merge only on the free lateral side or the stepped approximation of the diagonal. */
    public static @Nullable BlockState mergedState(BlockState state, Direction clicked) {
        Direction facing = state.getValue(FACING);
        var side = state.getValue(SIDE);
        var half = state.getValue(HALF);
        Direction freeSide = side == CopycatSlopeSide.LEFT ? facing.getClockWise() : facing.getCounterClockWise();
        if (clicked == freeSide) {
            return ModBlocks.COPYCAT_HORIZONTAL_HALF_SLOPE.get().defaultBlockState()
                    .setValue(FACING, facing).setValue(HALF, half);
        }
        if (clicked == (half == Half.BOTTOM ? Direction.UP : Direction.DOWN) || clicked == facing.getOpposite()) {
            // Reflecting the original TOP prism changes which end of the vertical double is high.
            return ModBlocks.COPYCAT_VERTICAL_HALF_SLOPE.get().defaultBlockState()
                    .setValue(FACING, half == Half.TOP ? facing.getOpposite() : facing)
                    .setValue(SIDE, half == Half.TOP ? side.opposite() : side);
        }
        return null;
    }

    /** Maps the original single prism to a stable slot in the merged geometry. */
    public static CopycatPart originalPart(BlockState original, BlockState merged) {
        boolean first = CopycatHalfSlopeState.form(merged) == CopycatHalfSlopeForm.HORIZONTAL
                ? original.getValue(SIDE) == CopycatSlopeSide.LEFT : original.getValue(HALF) == Half.BOTTOM;
        return first ? CopycatPart.BOTTOM : CopycatPart.TOP;
    }

    /** Inverse merge: returns the exact single prism occupied by the surviving material. */
    public static BlockState remainingState(BlockState state, CopycatPart remaining) {
        var single = ModBlocks.COPYCAT_HALF_SLOPE.get().defaultBlockState();
        Direction facing = state.getValue(FACING);
        if (CopycatHalfSlopeState.form(state) == CopycatHalfSlopeForm.HORIZONTAL) {
            return single.setValue(FACING, facing).setValue(HALF, state.getValue(HALF))
                    .setValue(SIDE, remaining == CopycatPart.BOTTOM ? CopycatSlopeSide.LEFT : CopycatSlopeSide.RIGHT);
        }
        boolean upper = remaining == CopycatPart.TOP;
        return single.setValue(FACING, upper ? facing.getOpposite() : facing)
                .setValue(HALF, upper ? Half.TOP : Half.BOTTOM)
                .setValue(SIDE, upper ? state.getValue(SIDE).opposite() : state.getValue(SIDE));
    }

    /** Completes a prism without losing its original material or consuming an item on failure. */
    public static ItemInteractionResult merge(Level level, BlockPos pos, BlockState state, Player player,
                                               ItemStack stack, BlockHitResult hit) {
        BlockState target = mergedState(state, hit.getDirection());
        if (target == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!level.mayInteract(player, pos) || !player.mayUseItemAt(pos, hit.getDirection(), stack)
                || !level.isUnobstructed(target, pos, CollisionContext.of(player))) return ItemInteractionResult.FAIL;
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof CopycatBlockEntity entity)) return ItemInteractionResult.FAIL;
        BlockState material = entity.getCopiedState(CopycatPart.MAIN);
        if (!replace(level, pos, target, originalPart(state, target), material)) return ItemInteractionResult.FAIL;
        if (!player.isCreative()) stack.shrink(1);
        var sound = target.getSoundType();
        level.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS, (sound.getVolume() + 1) / 2, sound.getPitch() * 0.8F);
        level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, target));
        return ItemInteractionResult.SUCCESS;
    }

    /** Removes only the targeted prism and refunds one component and its copied material. */
    public static InteractionResult remove(Level level, BlockPos pos, BlockState state, CopycatPart removed,
                                           Player player, ItemStack copied) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof CopycatBlockEntity entity)) return InteractionResult.PASS;
        CopycatPart remaining = removed == CopycatPart.BOTTOM ? CopycatPart.TOP : CopycatPart.BOTTOM;
        BlockState material = entity.getCopiedState(remaining);
        if (!replace(level, pos, remainingState(state, remaining), CopycatPart.MAIN, material)) return InteractionResult.FAIL;
        if (player != null && !player.isCreative()) {
            CopycatItemHelper.giveOrDrop(player, new ItemStack(ModItems.COPYCAT_HALF_SLOPE.get()));
            CopycatItemHelper.giveOrDrop(player, copied);
        }
        return InteractionResult.SUCCESS;
    }

    private static boolean replace(Level level, BlockPos pos, BlockState target, CopycatPart slot, BlockState material) {
        if (!level.setBlock(pos, target, Block.UPDATE_ALL)) return false;
        if (!(level.getBlockEntity(pos) instanceof CopycatBlockEntity replacement)) {
            throw new IllegalStateException("Half-slope is missing its registered Copycat block entity");
        }
        replacement.setCopiedState(slot, material);
        return true;
    }
}
