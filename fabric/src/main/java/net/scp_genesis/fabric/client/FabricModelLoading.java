package net.scp_genesis.fabric.client;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelResolver;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

public final class FabricModelLoading {

    private FabricModelLoading() {
    }

    public static void register() {
        ModelLoadingPlugin.register(
                FabricModelLoading::registerModelResolver
        );
    }

    private static void registerModelResolver(
            ModelLoadingPlugin.Context context
    ) {
        context.resolveModel().register(
                FabricModelLoading::resolveModel
        );
    }

    private static UnbakedModel resolveModel(
            ModelResolver.Context context
    ) {
        ResourceLocation id = context.id();

        /*
         * For now, we only identify Copycat Models.
         *
         * The real resolution will be added with
         * FabricCopycatUnbakedModel.
         */
        return null;
    }
}