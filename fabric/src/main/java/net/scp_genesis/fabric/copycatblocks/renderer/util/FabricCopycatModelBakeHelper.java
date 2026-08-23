package net.scp_genesis.fabric.copycatblocks.renderer.util;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;

public final class FabricCopycatModelBakeHelper {

    private FabricCopycatModelBakeHelper() {
    }

    public static BakedModel bakeModel(
            ModelBaker baker,
            ResourceLocation modelLocation,
            ModelState modelState
    ) {
        if (modelLocation == null) {
            throw new IllegalStateException(
                    "Missing Copycat base model"
            );
        }

        BakedModel model = baker.bake(
                modelLocation,
                modelState
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