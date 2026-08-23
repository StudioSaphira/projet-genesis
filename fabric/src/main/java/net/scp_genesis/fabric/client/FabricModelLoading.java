package net.scp_genesis.fabric.client;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.scp_genesis.fabric.copycatblocks.renderer.model.FabricCopycatModelLoader;
import net.scp_genesis.fabric.copycatblocks.renderer.model.FabricCopycatUnbakedModel;

import java.util.Map;

public final class FabricModelLoading {

    private FabricModelLoading() {
    }

    public static void register() {

        PreparableModelLoadingPlugin.register(
                FabricCopycatModelLoader::load,
                FabricModelLoading::initialize
        );
    }

    private static void initialize(
            Map<ResourceLocation, FabricCopycatUnbakedModel> models,
            ModelLoadingPlugin.Context context
    ) {
        context.modifyModelOnLoad().register(
                ModelModifier.OVERRIDE_PHASE,
                (model, modifierContext) ->
                        replaceModel(
                                model,
                                modifierContext,
                                models
                        )
        );
    }

    private static UnbakedModel replaceModel(
            UnbakedModel model,
            ModelModifier.OnLoad.Context context,
            Map<ResourceLocation, FabricCopycatUnbakedModel> models
    ) {
        ResourceLocation resourceId =
                context.resourceId();

        if (resourceId == null) {
            return model;
        }

        FabricCopycatUnbakedModel copycatModel =
                models.get(resourceId);

        if (copycatModel == null) {
            return model;
        }

        return copycatModel;
    }
}