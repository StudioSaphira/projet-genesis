package net.scp_genesis.common.registry;

import net.minecraft.world.inventory.MenuType;

import net.scp_genesis.common.furnitures.storage.LockerMenu;
import net.scp_genesis.common.platform.*;

public final class ModMenus {
    public static PlatformRegistryObject<MenuType<net.scp_genesis.common.furnitures.storage.DrawerMenu>> DRAWER;
    public static PlatformRegistryObject<MenuType<LockerMenu>> LOCKER;
    public static PlatformRegistryObject<MenuType<net.scp_genesis.common.scps.readers.menu.ReaderMenu>> READER;
    private ModMenus() {}
    public static void register(PlatformRegistry registry) {
        READER = registry.registerMenu("reader", net.scp_genesis.common.scps.readers.menu.ReaderMenu::new);
        DRAWER = registry.registerMenu("drawer", net.scp_genesis.common.furnitures.storage.DrawerMenu::new);
        LOCKER = registry.registerMenu("locker", LockerMenu::new);
    }
}
