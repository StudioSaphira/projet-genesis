package net.scp_genesis.neoforge.platform;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.platform.PlatformModelData;
import net.scp_genesis.neoforge.copycatblocks.renderer.model.CopycatModelProperties;

import java.util.EnumMap;

/**
 * NeoForge implementation of the platform model data abstraction.
 */
public final class NeoForgePlatformModelData
        implements PlatformModelData<ModelData> {

    @Override
    public ModelData create(
            EnumMap<CopycatPart, BlockState> copiedStates
    ) {
        return ModelData.builder()
                .with(
                        CopycatModelProperties.COPIED_STATES,
                        copiedStates
                )
                .build();
    }
}