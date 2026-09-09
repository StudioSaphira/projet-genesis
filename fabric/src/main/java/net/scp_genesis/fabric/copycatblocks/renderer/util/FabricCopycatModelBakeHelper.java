package net.scp_genesis.fabric.copycatblocks.renderer.util;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class FabricCopycatModelBakeHelper {

    private FabricCopycatModelBakeHelper() {}

    public static @NotNull BakedModel bakeModel(
            @NotNull ModelBaker baker,
            @NotNull ResourceLocation modelLocation,
            @NotNull ModelState modelState,
            @NotNull Function<Material, TextureAtlasSprite> spriteGetter
    ) {
        UnbakedModel unbakedModel = baker.getModel(modelLocation);

        BakedModel model =
                unbakedModel.bake(
                        baker,
                        spriteGetter,
                        modelState
                );

        if (model == null) {
            throw new IllegalStateException("Failed to bake Copycat base model: " + modelLocation);
        }

        return model;
    }
}