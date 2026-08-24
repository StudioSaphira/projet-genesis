package net.scp_genesis.common.copycatblocks.util;

/**
 * Common constants used by the Copycat system.
 */
public final class CopycatConstants {

    /**
     * Prevent instantiation.
     */
    private CopycatConstants() {
    }

    // ------------------------------------------------------------------------
    // NBT
    // ------------------------------------------------------------------------

    /**
     * NBT tag storing the copied BlockState.
     */
    public static final String COPIED_STATES_TAG = "CopiedStates";

    // ------------------------------------------------------------------------
    // Rendering
    // ------------------------------------------------------------------------

    /**
     * Copycat model loader identifier.
     */
    public static final String MODEL_LOADER_ID = "copycat";

    public static final String MODEL_SLOPE_ID = "copycat_slope";

    public static final String BLOCK_ENTITY_ID = "copycat";

}