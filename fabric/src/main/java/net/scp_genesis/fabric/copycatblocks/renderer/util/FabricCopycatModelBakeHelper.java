package net.scp_genesis.fabric.copycatblocks.renderer.util;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public final class FabricCopycatModelBakeHelper {

    private FabricCopycatModelBakeHelper() {
    }

    public static @NotNull BakedModel bakeModel(
            @NotNull ModelBaker baker,
            @NotNull ResourceLocation modelLocation,
            @NotNull ModelState modelState
    ) {
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