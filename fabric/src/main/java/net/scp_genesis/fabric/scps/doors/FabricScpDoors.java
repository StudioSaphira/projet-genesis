package net.scp_genesis.fabric.scps.doors;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.Minecraft;
import net.scp_genesis.common.scps.doors.ScpSlidingDoorRenderer;
import net.scp_genesis.common.registry.ModBlockEntities;

/** Registers platform model loading and the shared vanilla sliding-door renderer. */
public final class FabricScpDoors {
    private FabricScpDoors() {}
    public static void register() {
        ModelLoadingPlugin.register(context -> context.addModels(ScpSlidingDoorRenderer.FRONT, ScpSlidingDoorRenderer.BACK));
        BlockEntityRenderers.register(ModBlockEntities.SCP_SLIDING_DOOR.get(), context -> new ScpSlidingDoorRenderer(
                () -> ((FabricBakedModelManager) Minecraft.getInstance().getModelManager()).getModel(ScpSlidingDoorRenderer.FRONT),
                () -> ((FabricBakedModelManager) Minecraft.getInstance().getModelManager()).getModel(ScpSlidingDoorRenderer.BACK)));
    }
}
