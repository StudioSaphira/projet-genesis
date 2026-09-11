package net.scp_genesis.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.common.NeoForge;
import net.scp_genesis.neoforge.client.NeoForgeBlockColors;
import net.scp_genesis.neoforge.client.NeoForgeModelLoading;
import net.scp_genesis.neoforge.copycatblocks.client.NeoForgeCopycatOutlineHandler;

@Mod(
        value = "scp_genesis",
        dist = Dist.CLIENT
)
public final class NeoForgeGenesisClient {

    public NeoForgeGenesisClient(IEventBus modEventBus) {
        modEventBus.addListener((net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers event) ->
                event.registerEntityRenderer(net.scp_genesis.common.registry.ModEntities.CHAIR_SEAT.get(),
                        net.minecraft.client.renderer.entity.NoopRenderer::new));
        modEventBus.addListener(NeoForgeBlockColors::register);
        modEventBus.addListener(NeoForgeModelLoading::registerGeometryLoaders);

        NeoForge.EVENT_BUS.register(NeoForgeCopycatOutlineHandler.class);
    }
}