package net.scp_genesis.neoforge.furnitures;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.scp_genesis.common.furnitures.client.FurnituresGeometry;

import java.util.function.Function;

public final class NeoForgeFurnituresGeometry implements IUnbakedGeometry<NeoForgeFurnituresGeometry> {
    private final FurnituresGeometry geometry;

    public NeoForgeFurnituresGeometry(FurnituresGeometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker,
                           Function<Material, TextureAtlasSprite> sprites, ModelState state, ItemOverrides overrides) {
        return geometry.bake(context::getMaterial, sprites, state, context.getTransforms(), overrides,
                context.useAmbientOcclusion(), context.useBlockLight());
    }
}
