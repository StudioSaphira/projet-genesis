package net.scp_genesis.neoforge.furnitures;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.scp_genesis.common.furnitures.client.PlateRenderer;
import net.scp_genesis.common.registry.ModBlockEntities;
/** Registers the shared plate renderer on the client only. */
public final class NeoForgeFurnituresPlates {
    private NeoForgeFurnituresPlates() {}
    public static void register(IEventBus bus){bus.addListener((EntityRenderersEvent.RegisterRenderers event)->event.registerBlockEntityRenderer(ModBlockEntities.PLATE.get(),PlateRenderer::new));}
}
