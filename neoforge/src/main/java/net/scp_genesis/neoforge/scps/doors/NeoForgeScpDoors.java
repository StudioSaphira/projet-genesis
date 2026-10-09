package net.scp_genesis.neoforge.scps.doors;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.*;
import net.minecraft.client.Minecraft;
import net.scp_genesis.common.scps.doors.ScpSlidingDoorRenderer;
import net.scp_genesis.common.registry.ModBlockEntities;

/** Registers platform model loading and the shared vanilla sliding-door renderer. */
public final class NeoForgeScpDoors {
    private NeoForgeScpDoors() {}
    public static void register(IEventBus bus) {
        var front = new ModelResourceLocation(ScpSlidingDoorRenderer.FRONT, "standalone");
        var back = new ModelResourceLocation(ScpSlidingDoorRenderer.BACK, "standalone");
        bus.addListener((ModelEvent.RegisterAdditional event) -> { event.register(front); event.register(back); });
        bus.addListener((EntityRenderersEvent.RegisterRenderers event) -> event.registerBlockEntityRenderer(
                ModBlockEntities.SCP_SLIDING_DOOR.get(), context -> new ScpSlidingDoorRenderer(
                        () -> Minecraft.getInstance().getModelManager().getModel(front),
                        () -> Minecraft.getInstance().getModelManager().getModel(back))));
    }
}
