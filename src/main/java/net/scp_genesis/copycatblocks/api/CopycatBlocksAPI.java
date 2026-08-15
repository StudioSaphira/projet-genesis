package net.scp_genesis.copycatblocks.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import net.scp_genesis.copycatblocks.block.AbstractCopycatBlock;
import net.scp_genesis.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.copycatblocks.data.CopycatPart;
import net.scp_genesis.copycatblocks.util.CopycatBlockPredicate;

import org.jetbrains.annotations.Nullable;

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
    private static CopycatBlockEntity getCopycatBlockEntity(
            Level level,
            BlockPos pos
    ) {
        if (level.getBlockEntity(pos) instanceof CopycatBlockEntity blockEntity) {
            return blockEntity;
        }

        return null;
    }

    /**
     * Returns the Copycat BlockEntity at the given position.
     */
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
     */
    public static boolean isCopycat(BlockState state) {
        return state.getBlock() instanceof AbstractCopycatBlock;
    }

    /**
     * Returns whether the specified BlockState can be copied by a Copycat Cube.
     */
    public static boolean canCopy(
            Level level,
            BlockPos pos,
            BlockState state
    ) {
        return CopycatBlockPredicate.isValid(
                level,
                pos,
                state
        );
    }

    // ------------------------------------------------------------------------
    // Copy
    // ------------------------------------------------------------------------

    /**
     * Copies a BlockState into the main part of the Copycat Block.
     *
     * <p>This is the default copy operation used by single-part
     * Copycat Blocks such as the Cube.</p>
     */
    public static boolean copy(
            Level level,
            BlockPos pos,
            BlockState copiedState
    ) {
        return copy(
                level,
                pos,
                CopycatPart.MAIN,
                copiedState
        );
    }

    /**
     * Copies a BlockState into the specified Copycat part.
     */
    public static boolean copy(
            Level level,
            BlockPos pos,
            CopycatPart part,
            BlockState copiedState
    ) {

        if (!canCopy(level, pos, copiedState)) {
            return false;
        }

        CopycatBlockEntity blockEntity =
                getCopycatBlockEntity(level, pos);

        if (blockEntity == null) {
            return false;
        }

        if (blockEntity.hasCopiedState(part)) {
            return false;
        }

        blockEntity.setCopiedState(
                part,
                copiedState
        );

        return true;
    }

    // ------------------------------------------------------------------------
    // Item conversion
    // ------------------------------------------------------------------------

    /**
     * Returns the BlockState represented by the given ItemStack.
     *
     * @return the default BlockState, or AIR if the ItemStack is not a BlockItem.
     */
    public static BlockState getBlockStateFromItem(ItemStack stack) {

        if (!(stack.getItem() instanceof BlockItem blockItem)) {
            return Blocks.AIR.defaultBlockState();
        }

        return blockItem.getBlock().defaultBlockState();
    }

    // ------------------------------------------------------------------------
    // Clear
    // ------------------------------------------------------------------------

    /**
     * Clears the main copied BlockState.
     */
    public static void clear(
            Level level,
            BlockPos pos
    ) {
        clear(
                level,
                pos,
                CopycatPart.MAIN
        );
    }

    /**
     * Clears the copied BlockState from the specified part.
     */
    public static void clear(
            Level level,
            BlockPos pos,
            CopycatPart part
    ) {
        CopycatBlockEntity blockEntity =
                getCopycatBlockEntity(level, pos);

        if (blockEntity != null) {
            blockEntity.clearCopiedState(part);
        }
    }

    // ------------------------------------------------------------------------
    // Has copied state
    // ------------------------------------------------------------------------

    /**
     * Returns whether the main Copycat currently contains
     * a copied BlockState.
     */
    public static boolean hasCopiedState(
            Level level,
            BlockPos pos
    ) {
        return hasCopiedState(
                level,
                pos,
                CopycatPart.MAIN
        );
    }

    /**
     * Returns whether the specified Copycat part contains
     * a copied BlockState.
     */
    public static boolean hasCopiedState(
            Level level,
            BlockPos pos,
            CopycatPart part
    ) {
        CopycatBlockEntity blockEntity =
                getCopycatBlockEntity(level, pos);

        return blockEntity != null
                && blockEntity.hasCopiedState(part);
    }

    // ------------------------------------------------------------------------
    // Get copied state
    // ------------------------------------------------------------------------

    /**
     * Returns the main copied BlockState.
     */
    @Nullable
    public static BlockState getCopiedState(
            Level level,
            BlockPos pos
    ) {
        return getCopiedState(
                (BlockAndTintGetter) level,
                pos,
                CopycatPart.MAIN
        );
    }

    /**
     * Returns the copied BlockState from the specified part.
     */
    @Nullable
    public static BlockState getCopiedState(
            BlockAndTintGetter level,
            BlockPos pos
    ) {
        return getCopiedState(
                level,
                pos,
                CopycatPart.MAIN
        );
    }

    /**
     * Returns the copied BlockState from the specified part.
     */
    @Nullable
    public static BlockState getCopiedState(
            BlockAndTintGetter level,
            BlockPos pos,
            CopycatPart part
    ) {
        CopycatBlockEntity blockEntity =
                getCopycatBlockEntity(level, pos);

        if (blockEntity == null) {
            return null;
        }

        return blockEntity.getCopiedState(part);
    }
}