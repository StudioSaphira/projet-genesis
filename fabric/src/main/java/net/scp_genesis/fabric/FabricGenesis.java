package net.scp_genesis.fabric;

import net.fabricmc.api.ModInitializer;

import net.scp_genesis.fabric.platform.FabricPlatformRegistry;

@SuppressWarnings("unused")
public final class FabricGenesis
        implements ModInitializer {

    @Override
    public void onInitialize() {

        FabricPlatformRegistry registry =
                new FabricPlatformRegistry();

        registry.register();
    }
}