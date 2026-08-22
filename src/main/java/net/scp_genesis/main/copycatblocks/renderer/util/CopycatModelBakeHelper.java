package net.scp_genesis.main.copycatblocks.renderer.util;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public final class CopycatModelBakeHelper {

    public static BakedModel bakeModel(
            ModelBaker baker,
            ResourceLocation modelLocation,
            ModelState modelState,
            Function<Material, TextureAtlasSprite> spriteGetter
    ) {
        if (modelLocation == null) {
            throw new IllegalStateException(
                    "Missing Copycat base model"
            );
        }

        BakedModel model = baker.bake(
                modelLocation,
                modelState,
                spriteGetter
        );

        if (model == null) {
            throw new IllegalStateException(
                    "Failed to bake Copycat base model: "
                            + modelLocation
            );
        }

        return model;
    }
}
