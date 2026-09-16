package net.scp_genesis.neoforge.furnitures;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.scp_genesis.common.furnitures.client.DrawerScreen;
import net.scp_genesis.common.registry.ModMenus;

public final class NeoForgeFurnituresDrawers {
    private NeoForgeFurnituresDrawers() {}
    public static void register(IEventBus bus) {
        bus.addListener((RegisterMenuScreensEvent event) -> event.register(ModMenus.DRAWER.get(), DrawerScreen::new));
    }
}
