package net.scp_genesis.neoforge.platform;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.extensions.IBlockEntityExtension;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.platform.PlatformModelData;
import net.scp_genesis.common.platform.PlatformServicesImpl;

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
        if (blockEntity instanceof IBlockEntityExtension extension) {
            extension.requestModelDataUpdate();
        }
    }

    @Override
    public ModelData getModelData(
            BlockEntity blockEntity
    ) {
        if (blockEntity instanceof CopycatBlockEntity copycat) {
            return MODEL_DATA.create(
                    copycat.getCopiedStates()
            );
        }

        return ModelData.EMPTY;
    }
}