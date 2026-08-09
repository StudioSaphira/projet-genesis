package net.scp_genesis.copycatblocks.renderer;

import net.scp_genesis.copycatblocks.util.CopycatConstants;

/**
 * Geometry loader used by Copycat models.
 *
 * <p>This class is responsible for loading the custom Copycat model
 * declared in block model JSON files.</p>
 *
 * <p>Its implementation will be completed when the rendering pipeline
 * is connected through NeoForge's geometry loader system.</p>
 */
public final class CopycatModelLoader {

    /**
     * Loader identifier.
     */
    public static final String ID = CopycatConstants.MODEL_LOADER_ID;

    /**
     * Singleton instance.
     */
    public static final CopycatModelLoader INSTANCE = new CopycatModelLoader();

    /**
     * Prevent external instantiation.
     */
    private CopycatModelLoader() {
    }

}