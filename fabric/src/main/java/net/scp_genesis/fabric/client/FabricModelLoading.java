package net.scp_genesis.fabric.client;

import com.google.gson.JsonObject;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelResolver;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;

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
            Map<ResourceLocation, JsonObject> models,
            ModelLoadingPlugin.Context context
    ) {
        context.resolveModel().register(
                resolverContext ->
                        resolveModel(
                                resolverContext,
                                models
                        )
        );
    }

    private static net.minecraft.client.resources.model.UnbakedModel resolveModel(
            ModelResolver.Context context,
            Map<ResourceLocation, JsonObject> models
    ) {
        ResourceLocation id =
                context.id();

        JsonObject json =
                models.get(id);

        /*
         * Not a Copycat model.
         */
        if (json == null) {
            return null;
        }

        return FabricCopycatModelLoader.createModel(
                json
        );
    }
}