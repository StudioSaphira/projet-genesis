package net.scp_genesis.common.platform;

import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Provides platform-specific services used by the common code.
 */
public interface PlatformServicesImpl {

    /**
     * Returns the platform-specific model data provider.
     */
    PlatformModelData<?> modelData();

    /**
     * Requests a model data update for the specified BlockEntity.
     *
     * <p>The actual implementation is responsible for performing
     * the update using the active mod loader.</p>
     */
    void requestModelDataUpdate(BlockEntity blockEntity);

    Object getModelData(BlockEntity blockEntity);
}