package net.scp_genesis.main.constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Global constants used throughout SCP: Genesis.
 */
public final class ModConstants {

    /**
     * Mod identifier.
     */
    public static final String MOD_ID = "scp_genesis";

    /**
     * Mod logger.
     */
    public static final Logger LOGGER = LogManager.getLogger();

    /**
     * Prevent instantiation.
     */
    private ModConstants() {
    }

}
