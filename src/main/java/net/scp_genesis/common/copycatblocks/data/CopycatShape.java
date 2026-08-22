package net.scp_genesis.common.copycatblocks.data;

/**
 * Defines the logical shapes of a Copycat Block.
 *
 * <p>Different Copycat Blocks may use different shapes.</p>
 */
public enum CopycatShape {

    /**
     * Main Copycat shape.
     */
    MAIN,

    /**
     * Inner Copycat shape.
     */
    INNER,

    /**
     * Left-side Inner Copycat shape.
     */
    INNER_LEFT,

    /**
     * Right-side Inner Copycat shape.
     */
    INNER_RIGHT,

    /**
     * Outer Copycat shape.
     */
    OUTER,

    /**
     * Left-side Outer Copycat shape.
     */
    OUTER_LEFT,

    /**
     * Right-side Outer Copycat shape.
     */
    OUTER_RIGHT
}