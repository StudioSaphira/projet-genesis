package net.scp_genesis.common.copycatblocks.util.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.scp_genesis.common.copycatblocks.api.CopycatBlocksAPI;
import net.scp_genesis.common.copycatblocks.block.AbstractCopycatBlock;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.item.*;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

/** Dispatches tools before material copying, preserving block hooks and vanilla item fallback. */
public final class CopycatInteractionActions {
    private CopycatInteractionActions() {}

    /** Uses callbacks so protected subclass hooks need not become public APIs. */
    @SuppressWarnings("IfCanBeSwitch")
    public static ItemInteractionResult use(AbstractCopycatBlock block, ItemStack stack, BlockState state,
                                            Level level, BlockPos pos, Player player, BlockHitResult hit, Supplier<CopycatPart> target,
                                            BiPredicate<CopycatPart, BlockState> copy) {
        if (stack.getItem() instanceof CopycatScraperItem) return result(block.onScrape(level, pos, state, hit, player));
        if (stack.getItem() instanceof CopycatRemoverItem) return result(block.onRemove(level, pos, state, hit, player));
        if (stack.getItem() instanceof CopycatWrenchItem) {
            return block.isWrenchable() ? result(block.onWrench(level, pos, state))
                    : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (stack.getItem() instanceof BlockItem item && item.getBlock() instanceof AbstractCopycatBlock) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        BlockState material = CopycatBlocksAPI.getBlockStateFromItem(stack);
        if (material.isAir() || !copy.test(target.get(), material)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!player.isCreative()) stack.shrink(1);
        return ItemInteractionResult.SUCCESS;
    }

    /** Predicts a half-block interaction on the client without changing materials or inventory. */
    public static ItemInteractionResult predict(ItemStack stack, Level level, BlockPos pos,
                                                 @Nullable CopycatBlockEntity entity, CopycatPart part) {
        boolean tool = stack.getItem() instanceof CopycatWrenchItem || stack.getItem() instanceof CopycatRemoverItem
                || (stack.getItem() instanceof CopycatScraperItem && entity != null && entity.hasCopiedState(part));
        var material = CopycatBlocksAPI.getBlockStateFromItem(stack);
        boolean canCopy = entity != null && !entity.hasCopiedState(part) && CopycatBlocksAPI.canCopy(level, pos, material);
        return tool || canCopy ? ItemInteractionResult.SUCCESS : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private static ItemInteractionResult result(InteractionResult result) {
        return result.consumesAction() ? ItemInteractionResult.SUCCESS : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
