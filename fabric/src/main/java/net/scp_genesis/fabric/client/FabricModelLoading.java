package net.scp_genesis.fabric.client;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.resources.ResourceLocation;
import net.scp_genesis.fabric.copycatblocks.renderer.model.FabricCopycatModelLoader;
import net.scp_genesis.fabric.copycatblocks.renderer.model.FabricCopycatModelDefinition;
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
            Map<ResourceLocation, FabricCopycatModelDefinition> definitions,
            ModelLoadingPlugin.Context context
    ) {
        context.modifyModelBeforeBake().register(
                ModelModifier.OVERRIDE_PHASE,
                (model, modifierContext) ->
                        modifyModelBeforeBake(
                                model,
                                modifierContext,
                                definitions
                        )
        );
    }

    private static net.minecraft.client.resources.model.UnbakedModel
    modifyModelBeforeBake(
            net.minecraft.client.resources.model.UnbakedModel model,
            ModelModifier.BeforeBake.Context context,
            Map<ResourceLocation, FabricCopycatModelDefinition> definitions
    ) {
        ResourceLocation resourceId =
                context.resourceId();

        if (resourceId == null) {
            return model;
        }

        FabricCopycatModelDefinition definition =
                definitions.get(resourceId);

        if (definition == null) {
            return model;
        }

        return switch (definition.geometryType()) {
            case CUBE ->
                    new FabricCopycatUnbakedModel(
                            definition.baseModel()
                    );

            case SLAB, STAIRS ->
                    new FabricCopycatUnbakedModel(
                            definition.geometryType(),
                            definition.baseModels()
                    );

            case SLOPE ->
                    new FabricCopycatUnbakedModel(
                            FabricCopycatUnbakedModel.GeometryType.SLOPE,
                            null
                    );
        };
    }
}