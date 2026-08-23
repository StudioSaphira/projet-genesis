package net.scp_genesis.neoforge.platform;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.scp_genesis.common.platform.PlatformServices;

/**
 * NeoForge implementation of the platform services.
 */
public final class NeoForgePlatformServices
        implements PlatformServices.PlatformServicesImpl {

    @Override
    public void requestModelDataUpdate(
            BlockEntity blockEntity
    ) {
        blockEntity.requestModelDataUpdate();
    }
}