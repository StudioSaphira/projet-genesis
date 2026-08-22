package net.scp_genesis.platform;

import net.neoforged.bus.api.IEventBus;

import net.scp_genesis.registry.ModBlockEntities;
import net.scp_genesis.registry.ModBlocks;
import net.scp_genesis.registry.ModItems;
import net.scp_genesis.registry.ModTabs;

public final class NeoForgePlatformRegistry
        implements PlatformRegistry {

    private final IEventBus modEventBus;

    public NeoForgePlatformRegistry(
            IEventBus modEventBus
    ) {
        this.modEventBus = modEventBus;
    }

    @Override
    public void register() {
        ModBlocks.REGISTRY.register(modEventBus);
        ModItems.REGISTRY.register(modEventBus);
        ModBlockEntities.REGISTRY.register(modEventBus);
        ModTabs.REGISTRY.register(modEventBus);
    }
}