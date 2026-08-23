package net.scp_genesis.neoforge.platform;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.platform.PlatformModelData;

import java.util.EnumMap;

public final class NeoForgePlatformModelData
        implements PlatformModelData<ModelData> {

    /**
     * NeoForge property containing the copied BlockStates.
     */
    public static final ModelProperty<
            EnumMap<CopycatPart, BlockState>
            > COPIED_STATES =
            new ModelProperty<>();

    @Override
    public ModelData create(
            EnumMap<CopycatPart, BlockState> copiedStates
    ) {
        return ModelData.builder()
                .with(
                        COPIED_STATES,
                        copiedStates
                )
                .build();
    }
}