package net.scp_genesis.fabric.copycatblocks.renderer.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import net.scp_genesis.common.constants.ModConstants;
import net.scp_genesis.common.copycatblocks.util.CopycatConstants;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class FabricCopycatModelLoader {

    private static final String MODEL_DIRECTORY = "models";

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
                    "copycat_slope"
            );

    private FabricCopycatModelLoader() {
    }

    public static CompletableFuture<
            Map<ResourceLocation, FabricCopycatModelDefinition>
            > load(
            ResourceManager resourceManager,
            Executor executor
    ) {
        return CompletableFuture.supplyAsync(
                () -> loadSync(resourceManager),
                executor
        );
    }

    private static Map<
            ResourceLocation,
            FabricCopycatModelDefinition
            > loadSync(
            ResourceManager resourceManager
    ) {
        Map<
                ResourceLocation,
                FabricCopycatModelDefinition
                > definitions = new HashMap<>();

        Map<ResourceLocation, Resource> resources =
                resourceManager.listResources(
                        MODEL_DIRECTORY,
                        id ->
                                id.getNamespace().equals(
                                        ModConstants.MOD_ID
                                )
                                        && id.getPath().startsWith(
                                        "block/copycat_"
                                )
                                        && id.getPath().endsWith(
                                        ".json"
                                )
                );

        for (Map.Entry<ResourceLocation, Resource> entry
                : resources.entrySet()) {

            ResourceLocation resourceLocation =
                    entry.getKey();

            Resource resource =
                    entry.getValue();

            try (Reader reader =
                         resource.openAsReader()) {

                JsonElement element =
                        JsonParser.parseReader(reader);

                if (!element.isJsonObject()) {
                    continue;
                }

                JsonObject json =
                        element.getAsJsonObject();

                if (!isCopycatModel(json)) {
                    continue;
                }

                FabricCopycatModelDefinition definition =
                        parseDefinition(json);

                ResourceLocation modelId =
                        getModelId(resourceLocation);

                definitions.put(
                        modelId,
                        definition
                );

                ModConstants.LOGGER.info(
                        "[COPYCAT] Loaded model definition: {}",
                        modelId
                );

            } catch (Exception exception) {

                ModConstants.LOGGER.error(
                        "[COPYCAT] Failed to load model definition: {}",
                        resourceLocation,
                        exception
                );
            }
        }

        ModConstants.LOGGER.info(
                "[COPYCAT] Loaded {} Copycat model(s)",
                definitions.size()
        );

        return definitions;
    }

    private static boolean isCopycatModel(
            JsonObject json
    ) {
        if (!json.has(LOADER_PROPERTY)) {
            return false;
        }

        String loader =
                json.get(LOADER_PROPERTY)
                        .getAsString();

        return COPYCAT_LOADER.toString().equals(loader)
                || COPYCAT_SLOPE_LOADER.toString().equals(loader);
    }

    private static FabricCopycatModelDefinition parseDefinition(
            JsonObject json
    ) {
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
            return FabricCopycatModelDefinition.slope();
        }

        /*
         * ============================================================
         * STANDARD COPYCAT
         * ============================================================
         */

        if (!json.has(BASE_MODEL_PROPERTY)) {
            throw new IllegalStateException(
                    "Copycat model is missing required property: \"base_model\""
            );
        }

        JsonElement baseModelElement =
                json.get(BASE_MODEL_PROPERTY);

        /*
         * ============================================================
         * SIMPLE MODEL / CUBE
         * ============================================================
         */

        if (baseModelElement.isJsonPrimitive()) {

            ResourceLocation baseModel =
                    ResourceLocation.parse(
                            baseModelElement.getAsString()
                    );

            return FabricCopycatModelDefinition.cube(
                    baseModel
            );
        }

        /*
         * ============================================================
         * MULTIPART MODEL
         * ============================================================
         */

        if (baseModelElement.isJsonObject()) {

            JsonObject baseModels =
                    baseModelElement.getAsJsonObject();

            /*
             * --------------------------------------------------------
             * STAIRS
             * --------------------------------------------------------
             */

            if (baseModels.has("straight")
                    && baseModels.has("inner")
                    && baseModels.has("outer")) {

                Map<String, ResourceLocation> models =
                        new HashMap<>();

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

            Map<String, ResourceLocation> models =
                    new HashMap<>();

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

        throw new IllegalStateException(
                "Invalid \"base_model\" in Copycat model"
        );
    }

    private static ResourceLocation parseModelId(
            JsonObject json,
            String key
    ) {
        return ResourceLocation.parse(
                json.get(key).getAsString()
        );
    }

    private static ResourceLocation getModelId(
            ResourceLocation resourceLocation
    ) {
        String path =
                resourceLocation.getPath();

        if (!path.startsWith(MODEL_DIRECTORY + "/")
                || !path.endsWith(".json")) {

            throw new IllegalArgumentException(
                    "Invalid model resource path: "
                            + resourceLocation
            );
        }

        String modelPath =
                path.substring(
                        MODEL_DIRECTORY.length() + 1,
                        path.length() - ".json".length()
                );

        return ResourceLocation.fromNamespaceAndPath(
                resourceLocation.getNamespace(),
                modelPath
        );
    }
}