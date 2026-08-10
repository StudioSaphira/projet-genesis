package net.scp_genesis.copycatblocks.renderer.model;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelProperty;

/**
 * Model properties used by the Copycat rendering system.
 *
 * <p>These properties are used to transfer data from the
 * Copycat BlockEntity to the Copycat renderer.</p>
 */
public final class CopycatModelProperties {

    /**
     * ModelProperty containing the copied BlockState.
     */
    public static final ModelProperty<BlockState> COPIED_STATE = new ModelProperty<>();

    /**
     * Prevent instantiation.
     */
    private CopycatModelProperties() {
    }

}