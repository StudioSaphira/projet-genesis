package net.scp_genesis.fabric.copycatblocks.renderer.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import net.scp_genesis.common.constants.ModConstants;
import net.scp_genesis.common.copycatblocks.util.CopycatConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/** Genesis-specific geometry selection and base-model definitions. */
public final class FabricCopycatModelLoader {


    private static final String LOADER_PROPERTY = "loader";
    private static final String BASE_MODEL_PROPERTY = "base_model";

    private static final ResourceLocation COPYCAT_LOADER =
            ResourceLocation.fromNamespaceAndPath(
                    ModConstants.MOD_ID,
                    CopycatConstants.MODEL_LOADER_ID
            );

    private static final ResourceLocation COPYCAT_SLOPE_LOADER =
            ResourceLocation.fromNamespaceAndPath(
                    ModConstants.MOD_ID,
                    CopycatConstants.MODEL_SLOPE_ID
            );

    private FabricCopycatModelLoader() {
    }

    /** Supplies Genesis model identifiers and parsing rules to Saphyra's shared resource loader. */
    public static CompletableFuture<Map<ResourceLocation, FabricCopycatModelDefinition>> load(
            ResourceManager resourceManager, Executor executor) {
        return net.saphyra.common.api.rendering.ModelDefinitionLoader.load(
                resourceManager, executor,
                id -> id.getNamespace().equals(ModConstants.MOD_ID)
                        && id.getPath().startsWith("models/block/copycat_"),
                json -> isCopycatModel(json) ? parseDefinition(json) : null,
                ModConstants.LOGGER);
    }

    private static boolean isCopycatModel(JsonObject json) {
        if (!json.has(LOADER_PROPERTY)) {return false;}

        String loader = json.get(LOADER_PROPERTY).getAsString();

        return COPYCAT_LOADER.toString().equals(loader)
                || COPYCAT_SLOPE_LOADER.toString().equals(loader);
    }

    private static FabricCopycatModelDefinition parseDefinition(JsonObject json) {
        String loader =
                json.get(LOADER_PROPERTY)
                        .getAsString();

        /*
         * ============================================================
         * SLOPE
         * ============================================================
         *
         * The Slope has no base_model.
         * Its geometry is generated directly by
         * CopycatSlopeGeometry.
         */

        if (COPYCAT_SLOPE_LOADER.toString().equals(loader)) {
            var type = switch (json.has("shape") ? json.get("shape").getAsString() : "slope") {
                case "vertical" -> FabricCopycatUnbakedModel.GeometryType.VERTICAL_SLOPE;
                case "half" -> FabricCopycatUnbakedModel.GeometryType.HALF_SLOPE;
                case "horizontal_half" -> FabricCopycatUnbakedModel.GeometryType.HORIZONTAL_HALF_SLOPE;
                case "vertical_half" -> FabricCopycatUnbakedModel.GeometryType.VERTICAL_HALF_SLOPE;
                default -> FabricCopycatUnbakedModel.GeometryType.SLOPE;
            };
            return FabricCopycatModelDefinition.multipart(type, Map.of());
        }

        /*
         * ============================================================
         * STANDARD COPYCAT
         * ============================================================
         */

        if (json.has("shape") && "half_stairs".equals(json.get("shape").getAsString())) {
            return FabricCopycatModelDefinition.multipart(FabricCopycatUnbakedModel.GeometryType.HALF_STAIRS, Map.of());
        }
        if (json.has("shape") && "half_panel".equals(json.get("shape").getAsString())) {
            return FabricCopycatModelDefinition.multipart(FabricCopycatUnbakedModel.GeometryType.HALF_PANEL, Map.of());
        }
        if (json.has("shape") && "half_slab".equals(json.get("shape").getAsString())) {
            return FabricCopycatModelDefinition.multipart(FabricCopycatUnbakedModel.GeometryType.HALF_SLAB, Map.of());
        }
        if (json.has("shape") && "panel".equals(json.get("shape").getAsString())) {
            return FabricCopycatModelDefinition.multipart(FabricCopycatUnbakedModel.GeometryType.PANEL, Map.of());
        }

        if (!json.has(BASE_MODEL_PROPERTY)) {
            throw new IllegalStateException(
                    "Copycat model is missing required property: \"base_model\""
            );
        }

        JsonElement baseModelElement = json.get(BASE_MODEL_PROPERTY);

        /*
         * ============================================================
         * SIMPLE MODEL / CUBE
         * ============================================================
         */

        if (baseModelElement.isJsonPrimitive()) {

            ResourceLocation baseModel = ResourceLocation.parse(baseModelElement.getAsString());

            return FabricCopycatModelDefinition.cube(baseModel);
        }

        /*
         * ============================================================
         * MULTIPART MODEL
         * ============================================================
         */

        if (baseModelElement.isJsonObject()) {

            JsonObject baseModels = baseModelElement.getAsJsonObject();

            /*
             * --------------------------------------------------------
             * STAIRS
             * --------------------------------------------------------
             */

            if (baseModels.has("straight")
                    && baseModels.has("inner")
                    && baseModels.has("outer")) {

                Map<String, ResourceLocation> models = new HashMap<>();

                models.put(
                        "straight",
                        parseModelId(
                                baseModels,
                                "straight"
                        )
                );

                models.put(
                        "inner",
                        parseModelId(
                                baseModels,
                                "inner"
                        )
                );

                models.put(
                        "outer",
                        parseModelId(
                                baseModels,
                                "outer"
                        )
                );

                return FabricCopycatModelDefinition.multipart(
                        FabricCopycatUnbakedModel.GeometryType.STAIRS,
                        models
                );
            }

            /*
             * --------------------------------------------------------
             * SLAB
             * --------------------------------------------------------
             */

            String[] requiredModels = {
                    "bottom",
                    "top",
                    "double",
                    "double_secondary"
            };

            Map<String, ResourceLocation> models = new HashMap<>();

            for (String key : requiredModels) {

                if (!baseModels.has(key)) {
                    throw new IllegalStateException(
                            "Copycat model is missing required base model: \""
                                    + key
                                    + "\""
                    );
                }

                models.put(
                        key,
                        parseModelId(
                                baseModels,
                                key
                        )
                );
            }

            return FabricCopycatModelDefinition.multipart(
                    FabricCopycatUnbakedModel.GeometryType.SLAB,
                    models
            );
        }

        throw new IllegalStateException("Invalid \"base_model\" in Copycat model");
    }

    private static ResourceLocation parseModelId(
            JsonObject json,
            String key
    ) {
        return ResourceLocation.parse(json.get(key).getAsString());
    }

}
