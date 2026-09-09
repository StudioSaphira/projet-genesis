package net.scp_genesis.fabric.client;

import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.scp_genesis.common.constants.ModConstants;
import net.scp_genesis.fabric.copycatblocks.renderer.model.FabricCopycatModelLoader;
import net.scp_genesis.fabric.copycatblocks.renderer.model.FabricCopycatModelDefinition;
import net.scp_genesis.fabric.copycatblocks.renderer.model.FabricCopycatUnbakedModel;

import java.util.Map;

public final class FabricModelLoading {

    private FabricModelLoading() {}

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
        /*
         * ============================================================
         * MODEL RESOLVER
         * ============================================================
         *
         * The Slope item is resolved directly because its item model
         * does not correspond to one of the block definitions loaded
         * by FabricCopycatModelLoader.
         */
        context.resolveModel().register(resolverContext -> {

            ResourceLocation id = resolverContext.id();

            ResourceLocation slopeItemModel =
                    ResourceLocation.fromNamespaceAndPath(
                            ModConstants.MOD_ID,
                            "item/copycat_slope"
                    );

            if (id.equals(slopeItemModel)) {
                return new FabricCopycatUnbakedModel(
                        FabricCopycatUnbakedModel.GeometryType.SLOPE,
                        null
                );
            }

            return null;
        });

        /*
         * ============================================================
         * BEFORE BAKE
         * ============================================================
         */
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
        ResourceLocation resourceId = context.resourceId();

        /*
         * ============================================================
         * DIRECTLY IDENTIFIABLE MODELS
         * ============================================================
         */
        if (resourceId != null) {

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

        /*
         * ============================================================
         * TOP-LEVEL ITEM MODELS
         * ============================================================
         *
         * Item models use a ModelResourceLocation with the
         * "#inventory" variant.
         */
        ModelResourceLocation topLevelId =
                context.topLevelId();

        if (topLevelId == null) {
            return model;
        }

        ModelResourceLocation slopeItemModel =
                ModelResourceLocation.inventory(
                        ResourceLocation.fromNamespaceAndPath(
                                ModConstants.MOD_ID,
                                "copycat_slope"
                        )
                );

        if (topLevelId.equals(slopeItemModel)) {
            return new FabricCopycatUnbakedModel(
                    FabricCopycatUnbakedModel.GeometryType.SLOPE,
                    null
            );
        }

        return model;
    }
}