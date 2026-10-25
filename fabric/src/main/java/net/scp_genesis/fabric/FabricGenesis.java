package net.scp_genesis.fabric;

import net.fabricmc.api.ModInitializer;

import net.saphyra.fabric.platform.FabricPlatformRegistry;
import net.scp_genesis.common.registry.ModBlocks;

@SuppressWarnings("unused")
public final class FabricGenesis implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricPlatformRegistry registry = new FabricPlatformRegistry("scp_genesis");

        net.scp_genesis.common.registry.ModRegistries.register(registry);
        registry.register();
        net.scp_genesis.fabric.scps.readers.FabricReaders.register();
        ModBlocks.registerFlammables();
    }
}
