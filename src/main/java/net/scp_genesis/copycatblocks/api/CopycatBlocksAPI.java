package net.scp_genesis.copycatblocks.api;

import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import net.scp_genesis.copycatblocks.block.AbstractCopycatBlock;
import net.scp_genesis.copycatblocks.blockentity.CopycatBlockEntity;

/**
 * Public API for the Copycat Blocks system.
 *
 * <p>This class provides the public entry point for interacting with
 * Copycat Blocks. Internal implementation details should remain hidden
 * behind this API whenever possible.</p>
 */
public final class CopycatBlocksAPI {

    /**
     * Prevent instantiation.
     */
    private CopycatBlocksAPI() {
    }

    /**
     * Returns the Copycat BlockEntity at the given position.
     */
    @Nullable
    private static CopycatBlockEntity getCopycatBlockEntity(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof CopycatBlockEntity blockEntity) {
            return blockEntity;
        }

        return null;
    }

    @Nullable
    private static CopycatBlockEntity getCopycatBlockEntity(
            BlockAndTintGetter level,
            BlockPos pos
    ) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof CopycatBlockEntity copycat) {
            return copycat;
        }

        return null;
    }

    /**
     * Returns whether the specified BlockState belongs to a Copycat Block.
     *
     * @param state BlockState to test.
     * @return true if this BlockState is a Copycat Block.
     */
    public static boolean isCopycat(BlockState state) {
        return state.getBlock() instanceof AbstractCopycatBlock;
    }

    /**
     * Returns whether the specified BlockState can be copied by a Copycat Block.
     *
     * @param state BlockState to test.
     * @return true if this BlockState can be copied.
     */
    public static boolean canCopy(BlockState state) {

        if (state.isAir()) {
            return false;
        }

        return !isCopycat(state);
    }

    /**
     * Copies the specified BlockState into the Copycat Block located at the given position.
     *
     * @param level World.
     * @param pos Position of the Copycat Block.
     * @param copiedState BlockState to copy.
     * @return true if the copy succeeded.
     */
    public static boolean copy(Level level, BlockPos pos, BlockState copiedState) {

        if (!canCopy(copiedState)) {
            return false;
        }

        CopycatBlockEntity blockEntity = getCopycatBlockEntity(level, pos);

        if (blockEntity == null) {
            return false;
        }

        if (blockEntity.hasCopiedState()) {
            return false;
        }

        blockEntity.setCopiedState(copiedState);

        return true;
    }

    /**
     * Returns the BlockState represented by the given ItemStack.
     *
     * @param stack ItemStack to convert.
     * @return the default BlockState, or AIR if the ItemStack is not a BlockItem.
     */
    public static BlockState getBlockStateFromItem(ItemStack stack) {

        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            return Blocks.AIR.defaultBlockState();
        }

        return blockItem.getBlock().defaultBlockState();
    }

    /**
     * Clears the copied BlockState from the specified Copycat Block.
     *
     * @param level World.
     * @param pos Position of the Copycat Block.
     */
    public static void clear(Level level, BlockPos pos) {
        CopycatBlockEntity blockEntity = getCopycatBlockEntity(level, pos);

        if (blockEntity != null) {
            blockEntity.clearCopiedState();
        }
    }

    /**
     * Returns whether the specified Copycat Block currently contains a copied BlockState.
     *
     * @param level World.
     * @param pos Position of the Copycat Block.
     * @return true if a BlockState has been copied.
     */
    public static boolean hasCopiedState(Level level, BlockPos pos) {
        CopycatBlockEntity blockEntity = getCopycatBlockEntity(level, pos);

        return blockEntity != null && blockEntity.hasCopiedState();
    }

    /**
     * Returns the currently copied BlockState.
     *
     * @param level World.
     * @param pos Position of the Copycat Block.
     * @return the copied BlockState, or AIR if none has been copied.
     */
    @Nullable
    public static BlockState getCopiedState(Level level, BlockPos pos) {
        return getCopiedState((BlockAndTintGetter) level, pos);
    }

    @Nullable
    public static BlockState getCopiedState(
            BlockAndTintGetter level,
            BlockPos pos
    ) {
        CopycatBlockEntity blockEntity = getCopycatBlockEntity(level, pos);

        if (blockEntity == null) {
            return null;
        }

        return blockEntity.getCopiedState();
    }
}