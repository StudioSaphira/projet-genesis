package net.scp_genesis.common.copycatblocks.util.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.scp_genesis.common.copycatblocks.data.CopycatHalfForm;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import java.util.function.Supplier;

/** Merge-first interaction routing and server-side rotation for every half-block family. */
public final class CopycatHalfActions {
    private CopycatHalfActions() {}
    /** Preserves the existing merge priority, non-mutating client prediction and server fallback. */
    public static ItemInteractionResult use(CopycatHalfBehavior behavior, CopycatHalfForm form,
            ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit,
            Supplier<CopycatBlockEntity> entity, Supplier<CopycatPart> target,
            Supplier<ItemInteractionResult> normalInteraction) {
        if (stack.is(behavior.componentItem()) && form == CopycatHalfForm.SINGLE) {
            return behavior.merge(level, pos, state, player, stack, hit);
        }
        if (level.isClientSide) return CopycatInteractionActions.predict(stack, level, pos, entity.get(), target.get());
        return normalInteraction.get();
    }
    /** Rotates the block frame; the family defines the order, material slots remain unchanged. */
    public static InteractionResult wrench(CopycatHalfBehavior behavior, Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide) level.setBlock(pos, behavior.next(state), Block.UPDATE_ALL);
        return InteractionResult.SUCCESS;
    }
}
