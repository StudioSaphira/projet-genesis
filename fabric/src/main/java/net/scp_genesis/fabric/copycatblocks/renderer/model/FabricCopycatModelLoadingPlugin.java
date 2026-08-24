package net.scp_genesis.fabric.copycatblocks.renderer.model;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
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

    private static UnbakedModel modifyModelBeforeBake(
            UnbakedModel model,
            ModelModifier.BeforeBake.Context context,
            Map<ResourceLocation, FabricCopycatModelDefinition> definitions
    ) {
        ResourceLocation resourceId =
                context.resourceId();

        /*
         * BeforeBake can also be called for top-level models
         * identified by a ModelResourceLocation.
         *
         * We only care about regular resource-based models.
         */
        if (resourceId == null) {
            return model;
        }

        FabricCopycatModelDefinition definition =
                definitions.get(resourceId);

        if (definition == null) {
            return model;
        }

        ModConstants.LOGGER.debug(
                "[COPYCAT] Replacing model before bake: {}",
                resourceId
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