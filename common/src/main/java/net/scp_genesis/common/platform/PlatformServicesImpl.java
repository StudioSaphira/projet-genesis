package net.scp_genesis.common.platform;

import net.minecraft.world.level.block.entity.BlockEntity;

public interface PlatformServicesImpl {
    /** Sends a private reader-menu snapshot to its authorized player. */
    void sendReader(net.minecraft.server.level.ServerPlayer player, net.scp_genesis.common.scps.readers.ReaderPacket packet);


    /**
     * Returns the platform-specific model data provider.
     */
    PlatformModelData<?> modelData();

    /**
     * Requests a model data update for the specified BlockEntity.
     */
    void requestModelDataUpdate(BlockEntity blockEntity);

    /**
     * Returns the model data associated with the specified BlockEntity.
     */
    PlatformModelData.ModelData getModelData(BlockEntity blockEntity);
}