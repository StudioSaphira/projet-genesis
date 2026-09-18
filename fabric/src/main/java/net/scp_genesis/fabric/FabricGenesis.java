package net.scp_genesis.fabric;

import net.fabricmc.api.ModInitializer;

import net.scp_genesis.fabric.platform.FabricPlatformRegistry;
import net.scp_genesis.common.registry.ModBlocks;

@SuppressWarnings("unused")
public final class FabricGenesis implements ModInitializer {

    @Override
    public void onInitialize() {
        net.scp_genesis.common.platform.PlatformServices.initialize(
                new net.scp_genesis.fabric.platform.FabricPlatformServices());
        FabricPlatformRegistry registry = new FabricPlatformRegistry();

        registry.register();
        ModBlocks.registerFlammables();
    }
}
