package net.scp_genesis.fabric.furnitures;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.scp_genesis.common.furnitures.client.FurnituresGeometry;

import java.util.Collection;
import java.util.function.Function;

public final class FabricFurnituresUnbakedModel extends BlockModel {
    private final FurnituresGeometry geometry;

    public FabricFurnituresUnbakedModel(FurnituresGeometry geometry) {
        super(null, java.util.List.of(), java.util.Map.of(), null, null,
                net.minecraft.client.renderer.block.model.ItemTransforms.NO_TRANSFORMS, java.util.List.of());
        this.geometry = geometry;
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return geometry.template().getDependencies();
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> getter) {
        geometry.template().resolveParents(getter);
    }

    @Override
    public BakedModel bake(ModelBaker baker, Function<Material, TextureAtlasSprite> sprites, ModelState state) {
        // Also covers top-level inventory models replaced by the before-bake hook.
        resolveParents(baker::getModel);
        BlockModel model = geometry.template();
        return geometry.bake(model::getMaterial, sprites, state, model.getTransforms(),
                ItemOverrides.EMPTY, model.hasAmbientOcclusion(), model.getGuiLight() == BlockModel.GuiLight.SIDE);
    }
}
