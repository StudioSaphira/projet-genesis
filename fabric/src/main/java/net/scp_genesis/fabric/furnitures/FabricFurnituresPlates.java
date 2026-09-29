package net.scp_genesis.fabric.furnitures;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.scp_genesis.common.furnitures.client.PlateRenderer;
import net.scp_genesis.common.registry.ModBlockEntities;
/** Registers the shared plate renderer on the client only. */
public final class FabricFurnituresPlates {
    private FabricFurnituresPlates() {}
    public static void register(){BlockEntityRenderers.register(ModBlockEntities.PLATE.get(),PlateRenderer::new);}
}
