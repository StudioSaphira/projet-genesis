package net.scp_genesis.fabric.furnitures;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.scp_genesis.common.furnitures.client.*;
import net.scp_genesis.common.registry.*;

public final class FabricFurnituresLockers {
    private FabricFurnituresLockers() {}
    public static void register() {
        ModelLoadingPlugin.register(context -> context.addModels(LockerRenderer.DOOR));
        BlockEntityRenderers.register(ModBlockEntities.LOCKER.get(), context -> new LockerRenderer(
                () -> ((FabricBakedModelManager) Minecraft.getInstance().getModelManager()).getModel(LockerRenderer.DOOR)));
        MenuScreens.register(ModMenus.LOCKER.get(), LockerScreen::new);
    }
}
