package net.scp_genesis.neoforge.furnitures;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.*;
import net.scp_genesis.common.furnitures.client.*;
import net.scp_genesis.common.registry.*;

public final class NeoForgeFurnituresLockers {
    private static final ModelResourceLocation DOOR = new ModelResourceLocation(LockerRenderer.DOOR, "standalone");
    private NeoForgeFurnituresLockers() {}
    public static void register(IEventBus bus) {
        bus.addListener((ModelEvent.ModifyBakingResult event) -> event.getModels().replaceAll((id, model) -> {
            if (id.id().getNamespace().equals("scp_genesis") && !id.variant().equals("inventory")
                    && (id.id().getPath().equals("locker") || id.id().getPath().equals("locker_shelf"))) {
                return new NeoForgeFurnituresLockerModel(model);
            }
            return model;
        }));
        bus.addListener((ModelEvent.RegisterAdditional event) -> event.register(DOOR));
        bus.addListener((EntityRenderersEvent.RegisterRenderers event) ->
                event.registerBlockEntityRenderer(ModBlockEntities.LOCKER.get(), context -> new LockerRenderer(
                        () -> Minecraft.getInstance().getModelManager().getModel(DOOR))));
        bus.addListener((RegisterMenuScreensEvent event) -> event.register(ModMenus.LOCKER.get(), LockerScreen::new));
    }
}
