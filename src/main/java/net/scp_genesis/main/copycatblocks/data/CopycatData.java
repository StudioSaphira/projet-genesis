package net.scp_genesis.main.copycatblocks.data;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * Stores the data used by a Copycat Block.
 *
 * <p>This class is only responsible for holding the copied BlockStates.
 * Serialization is handled by the BlockEntity.</p>
 */
public final class CopycatData {

    private final java.util.EnumMap<CopycatPart, BlockState> copiedStates =
            new java.util.EnumMap<>(CopycatPart.class);

    /**
     * Creates an empty CopycatData.
     */
    public CopycatData() {
        clear();
    }

    /**
     * Returns the copied BlockState for the specified part.
     *
     * @param part Copycat part.
     * @return the copied BlockState, or AIR if the part is empty.
     */
    public @NotNull BlockState getCopiedState(CopycatPart part) {
        return copiedStates.getOrDefault(
                part,
                Blocks.AIR.defaultBlockState()
        );
    }

    /**
     * Sets the copied BlockState for the specified part.
     *
     * @param part Copycat part.
     * @param state BlockState to copy.
     */
    public void setCopiedState(
            CopycatPart part,
            @NotNull BlockState state
    ) {
        copiedStates.put(part, state);
    }

    /**
     * Returns whether the specified part contains a copied BlockState.
     *
     * @param part Copycat part.
     * @return true if the part contains a copied BlockState.
     */
    public boolean hasCopiedState(CopycatPart part) {
        return !getCopiedState(part).isAir();
    }

    /**
     * Clears the copied BlockState for the specified part.
     *
     * @param part Copycat part.
     */
    public void clear(CopycatPart part) {
        copiedStates.put(
                part,
                Blocks.AIR.defaultBlockState()
        );
    }

    /**
     * Clears all copied BlockStates.
     */
    public void clear() {
        for (CopycatPart part : CopycatPart.values()) {
            copiedStates.put(
                    part,
                    Blocks.AIR.defaultBlockState()
            );
        }
    }

    // ------------------------------------------------------------------------
    // Main part compatibility helpers
    // ------------------------------------------------------------------------

    /**
     * Returns the main copied BlockState.
     *
     * <p>This method preserves the existing Copycat Cube API.</p>
     */
    public @NotNull BlockState getCopiedState() {
        return getCopiedState(CopycatPart.MAIN);
    }

    /**
     * Sets the main copied BlockState.
     *
     * <p>This method preserves the existing Copycat Cube API.</p>
     */
    public void setCopiedState(@NotNull BlockState state) {
        setCopiedState(CopycatPart.MAIN, state);
    }

    /**
     * Returns whether the main part contains a copied BlockState.
     */
    public boolean hasCopiedState() {
        return hasCopiedState(CopycatPart.MAIN);
    }
}