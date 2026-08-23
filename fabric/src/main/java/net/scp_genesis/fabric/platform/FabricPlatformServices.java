package net.scp_genesis.fabric.platform;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.platform.PlatformModelData;
import net.scp_genesis.common.platform.PlatformServicesImpl;

public final class FabricPlatformServices
        implements PlatformServicesImpl {

    private static final PlatformModelData<FabricPlatformModelData.Data>
            MODEL_DATA =
            new FabricPlatformModelData();

    @Override
    public PlatformModelData<?> modelData() {
        return MODEL_DATA;
    }

    @Override
    public void requestModelDataUpdate(
            BlockEntity blockEntity
    ) {
        /*
         * Fabric does not have NeoForge's ModelDataManager.
         *
         * Model data will be consumed directly by the
         * Fabric Copycat renderer.
         */
    }

    @Override
    public PlatformModelData.ModelData getModelData(
            BlockEntity blockEntity
    ) {
        if (blockEntity instanceof CopycatBlockEntity copycat) {
            return MODEL_DATA.create(
                    copycat.getCopiedStates()
            );
        }

        return FabricPlatformModelData.empty();
    }
}