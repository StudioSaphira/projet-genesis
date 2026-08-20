package net.scp_genesis.copycatblocks.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.copycatblocks.block.AbstractCopycatBlock;
import net.scp_genesis.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.copycatblocks.data.CopycatPart;
import org.jetbrains.annotations.NotNull;

public final class CopycatItemHelper {

    private CopycatItemHelper() {
    }

    /**
     * Creates the ItemStack corresponding to a Copycat BlockState.
     */
    public static @NotNull ItemStack getCopycatItem(
            @NotNull BlockState state
    ) {
        if (!(state.getBlock() instanceof AbstractCopycatBlock)) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(
                state.getBlock().asItem()
        );
    }

    /**
     * Creates the ItemStack corresponding to the block
     * copied by a Copycat part.
     */
    public static @NotNull ItemStack getCopiedBlockItem(
            @NotNull CopycatBlockEntity blockEntity,
            @NotNull CopycatPart part
    ) {
        BlockState copiedState =
                blockEntity.getCopiedState(part);

        if (copiedState.isAir()) {
            return ItemStack.EMPTY;
        }

        return new ItemStack(
                copiedState.getBlock().asItem()
        );
    }

    /**
     * Gives an ItemStack to the player.
     *
     * <p>If the inventory is full, the item is dropped.</p>
     */
    public static void giveOrDrop(
            @NotNull Player player,
            @NotNull ItemStack stack
    ) {
        if (stack.isEmpty()) {
            return;
        }

        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }
}