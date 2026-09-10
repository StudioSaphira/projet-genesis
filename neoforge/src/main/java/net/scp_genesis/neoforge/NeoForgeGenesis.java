package net.scp_genesis.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.scp_genesis.common.registry.ModBlocks;

import net.scp_genesis.neoforge.platform.NeoForgePlatformRegistry;

@Mod("scp_genesis")
public final class NeoForgeGenesis {

    public NeoForgeGenesis(IEventBus modEventBus) {
        NeoForgePlatformRegistry registry = new NeoForgePlatformRegistry(modEventBus);

        registry.register();
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModBlocks::registerFlammables);
    }
}
