package net.scp_genesis.common.registry;

import net.saphyra.common.platform.PlatformRegistry;

/**
 * Declares Genesis content in dependency order using the Saphyra registry of the active loader.
 * The loader entry point finishes registration after all declarations.
 */
public final class ModRegistries {

    private ModRegistries() {}

    public static void register(PlatformRegistry registry) {
        ModEntities.register(registry);
        ModBlocks.register(registry);
        ModItems.register(registry);
        ModBlockEntities.register(registry);
        ModMenus.register(registry);
        ModTabs.register(registry);
    }
}
