package net.scp_genesis.copycatblocks.data;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

/**
 * Stores the data used by a Copycat Block.
 *
 * <p>This class is only responsible for holding the copied BlockState.
 * Serialization is handled by the BlockEntity.</p>
 */
public final class CopycatData {

    /**
     * The copied BlockState.
     */
    private BlockState copiedState = Blocks.AIR.defaultBlockState();

    /**
     * Creates an empty CopycatData.
     */
    public CopycatData() {
        this(Blocks.AIR.defaultBlockState());
    }

    /**
     * Creates a CopycatData with an initial BlockState.
     *
     * @param copiedState the copied BlockState
     */
    public CopycatData(BlockState copiedState) {
        this.copiedState = copiedState;
    }

    /**
     * Returns the copied BlockState.
     *
     * @return the copied BlockState, or {@code null} if none exists
     */
    public BlockState getCopiedState() {
        return copiedState;
    }

    /**
     * Sets the copied BlockState.
     *
     * @param copiedState the BlockState to copy
     */
    public void setCopiedState(BlockState copiedState) {
        this.copiedState = copiedState;
    }

    /**
     * Returns whether a BlockState has been copied.
     *
     * @return {@code true} if a BlockState exists
     */
    public boolean hasCopiedState() {
        return !copiedState.isAir();
    }

    /**
     * Clears the copied BlockState.
     */
    public void clear() {
        copiedState = Blocks.AIR.defaultBlockState();
    }
}