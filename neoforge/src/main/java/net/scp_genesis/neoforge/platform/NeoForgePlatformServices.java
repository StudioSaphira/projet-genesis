package net.scp_genesis.neoforge.platform;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.platform.PlatformModelData;
import net.scp_genesis.common.platform.PlatformServicesImpl;

/**
 * NeoForge implementation of the platform services.
 */
public final class NeoForgePlatformServices
        implements PlatformServicesImpl {

    private static final PlatformModelData<ModelData> MODEL_DATA =
            new NeoForgePlatformModelData();

    @Override
    public PlatformModelData<?> modelData() {
        return MODEL_DATA;
    }

    @Override
    public void requestModelDataUpdate(
            BlockEntity blockEntity
    ) {
        /*
         * NeoForge-specific model data update.
         */
        blockEntity.requestModelDataUpdate();
    }
}