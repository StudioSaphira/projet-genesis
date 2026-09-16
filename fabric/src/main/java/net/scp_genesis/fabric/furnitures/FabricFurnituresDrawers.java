package net.scp_genesis.fabric.furnitures;

import net.minecraft.client.gui.screens.MenuScreens;
import net.scp_genesis.common.furnitures.client.DrawerScreen;
import net.scp_genesis.common.registry.ModMenus;

public final class FabricFurnituresDrawers {
    private FabricFurnituresDrawers() {}
    public static void register() { MenuScreens.register(ModMenus.DRAWER.get(), DrawerScreen::new); }
}
