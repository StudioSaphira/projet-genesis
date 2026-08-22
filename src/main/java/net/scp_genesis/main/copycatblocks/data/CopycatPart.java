package net.scp_genesis.main.copycatblocks.data;

/**
 * Defines the logical parts of a Copycat Block.
 *
 * <p>Different Copycat Blocks may use different parts.
 * For example, a Cube uses MAIN while a Slab can use
 * BOTTOM and TOP.</p>
 */
public enum CopycatPart {

    /**
     * Main or single-part Copycat state.
     */
    MAIN,

    /**
     * Lower part of a Copycat Block.
     */
    BOTTOM,

    /**
     * Upper part of a Copycat Block.
     */
    TOP,

    /**
     * Left part of a Copycat Block.
     */
    LEFT,

    /**
     * Right part of a Copycat Block.
     */
    RIGHT,

    /**
     * Front part of a Copycat Block.
     */
    FRONT,

    /**
     * Back part of a Copycat Block.
     */
    BACK
}