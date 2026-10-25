package net.scp_genesis.fabric;

import net.fabricmc.api.ClientModInitializer;

import net.scp_genesis.fabric.client.FabricBlockColors;
import net.scp_genesis.fabric.client.FabricModelLoading;
import net.scp_genesis.fabric.copycatblocks.client.FabricCopycatOutlineHandler;
import net.scp_genesis.fabric.furnitures.*;

@SuppressWarnings("unused")
public final class FabricGenesisClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
        net.scp_genesis.common.registry.ModEntities.CHAIR_SEAT.get(),
        net.minecraft.client.renderer.entity.NoopRenderer::new);
        net.scp_genesis.fabric.scps.readers.FabricReadersClient.register();
        net.scp_genesis.fabric.scps.doors.FabricScpDoors.register();
        FabricFurnituresPlates.register();
        FabricFurnituresLockers.register();
        FabricFurnituresDrawers.register();
        FabricBlockColors.register();
        FabricModelLoading.register();
        FabricFurnituresModelLoader.register();
        FabricCopycatOutlineHandler.register();
    }
}
