package net.scp_genesis.fabric.copycatblocks.renderer.model;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelResolver;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.scp_genesis.common.constants.ModConstants;

import java.util.Map;

public final class FabricCopycatModelLoadingPlugin
        implements PreparableModelLoadingPlugin<
        Map<ResourceLocation, FabricCopycatModelDefinition>
        > {

    public static final FabricCopycatModelLoadingPlugin INSTANCE =
            new FabricCopycatModelLoadingPlugin();

    private FabricCopycatModelLoadingPlugin() {
    }

    /**
     * Registers the Copycat model loading plugin.
     */
    public static void register() {
        PreparableModelLoadingPlugin.register(
                FabricCopycatModelDataLoader::load,
                INSTANCE
        );
    }

    @Override
    public void onInitializeModelLoader(
            Map<ResourceLocation, FabricCopycatModelDefinition> definitions,
            ModelLoadingPlugin.Context context
    ) {
        context.resolveModel().register(
                resolverContext -> resolveModel(
                        resolverContext,
                        definitions
                )
        );
    }

    private static UnbakedModel resolveModel(
            ModelResolver.Context context,
            Map<ResourceLocation, FabricCopycatModelDefinition> definitions
    ) {
        ResourceLocation modelId =
                context.id();

        FabricCopycatModelDefinition definition =
                definitions.get(modelId);

        if (definition == null) {
            return null;
        }

        ModConstants.LOGGER.debug(
                "[COPYCAT] Resolving model: {}",
                modelId
        );

        if (definition.geometryType()
                == FabricCopycatUnbakedModel.GeometryType.CUBE) {

            return new FabricCopycatUnbakedModel(
                    definition.baseModel()
            );
        }

        return new FabricCopycatUnbakedModel(
                definition.geometryType(),
                definition.baseModels()
        );
    }
}