package net.scp_genesis;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import net.scp_genesis.client.ModBlockColors;
import net.scp_genesis.client.ModModelLoading;
import net.scp_genesis.platform.NeoForgePlatformRegistry;

@Mod("scp_genesis")
public final class NeoForgeGenesis {

    public NeoForgeGenesis(
            IEventBus modEventBus
    ) {
        NeoForgePlatformRegistry registry =
                new NeoForgePlatformRegistry(
                        modEventBus
                );

        registry.register();

        modEventBus.addListener(
                ModBlockColors::register
        );

        modEventBus.addListener(
                ModModelLoading::registerGeometryLoaders
        );
    }
}
