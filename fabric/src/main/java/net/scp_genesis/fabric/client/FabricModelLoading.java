package net.scp_genesis.fabric.client;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelResolver;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.util.Identifier;
import net.scp_genesis.common.constants.ModConstants;
import net.scp_genesis.common.copycatblocks.util.CopycatConstants;
import net.scp_genesis.fabric.copycatblocks.renderer.model.FabricCopycatUnbakedModel;

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
        Identifier id = context.id();

        /*
         * Pour l'instant, nous ne faisons qu'identifier
         * les modèles Copycat.
         *
         * La résolution réelle sera ajoutée avec
         * FabricCopycatUnbakedModel.
         */
        return null;
    }
}