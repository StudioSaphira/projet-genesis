package net.scp_genesis.neoforge.copycatblocks.renderer.model;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;

import java.util.EnumMap;

/**
 * Model properties used by the Copycat rendering system.
 *
 * <p>These properties are used to transfer copied BlockStates
 * from the Copycat BlockEntity to the Copycat renderer.</p>
 */
public final class CopycatModelProperties {

    /**
     * ModelProperty containing the copied BlockStates
     * indexed by their Copycat part.
     */
    public static final ModelProperty<EnumMap<CopycatPart, BlockState>> COPIED_STATES =
            new ModelProperty<>();

    /**
     * Prevent instantiation.
     */
    private CopycatModelProperties() {
    }
}