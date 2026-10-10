package net.scp_genesis.neoforge.platform;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.common.extensions.IBlockEntityExtension;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.platform.PlatformModelData;
import net.scp_genesis.common.platform.PlatformServicesImpl;

public final class NeoForgePlatformServices
        implements PlatformServicesImpl {

    @Override public void sendReader(net.minecraft.server.level.ServerPlayer player,
                                      net.scp_genesis.common.scps.readers.ReaderPacket packet) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player,packet);
    }

    private static final PlatformModelData<NeoForgePlatformModelData.Data>
            MODEL_DATA =
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
    public PlatformModelData.ModelData getModelData(
            BlockEntity blockEntity
    ) {
        if (blockEntity instanceof CopycatBlockEntity copycat) {
            return MODEL_DATA.create(
                    copycat.getCopiedStates()
            );
        }

        return NeoForgePlatformModelData.empty();
    }
}