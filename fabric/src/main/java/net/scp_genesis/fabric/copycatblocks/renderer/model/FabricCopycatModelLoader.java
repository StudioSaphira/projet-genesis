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

    private FabricCopycatModelLoader() {
    }

    /**
     * Loads all Copycat model definitions from resources.
     *
     * <p>The returned map contains the parsed JSON for every model
     * using the Copycat model loader.</p>
     */
    public static CompletableFuture<
            Map<ResourceLocation, JsonObject>
            > load(
            ResourceManager resourceManager,
            Executor executor
    ) {
        return CompletableFuture.supplyAsync(
                () -> loadModels(resourceManager),
                executor
        );
    }

    public static FabricCopycatUnbakedModel createModel(
            JsonObject json
    ) {
        if (!json.has("base_model")) {
            throw new IllegalArgumentException(
                    "Copycat model is missing required property: \"base_model\""
            );
        }

        JsonElement baseModelElement =
                json.get("base_model");

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

            return new FabricCopycatUnbakedModel(
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

                String[] requiredModels = {
                        "straight",
                        "inner",
                        "outer"
                };

                for (String key : requiredModels) {

                    models.put(
                            key,
                            ResourceLocation.parse(
                                    baseModels
                                            .get(key)
                                            .getAsString()
                            )
                    );
                }

                return new FabricCopycatUnbakedModel(
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
                    throw new IllegalArgumentException(
                            "Copycat model is missing required base model: \""
                                    + key
                                    + "\""
                    );
                }

                models.put(
                        key,
                        ResourceLocation.parse(
                                baseModels
                                        .get(key)
                                        .getAsString()
                        )
                );
            }

            return new FabricCopycatUnbakedModel(
                    FabricCopycatUnbakedModel.GeometryType.SLAB,
                    models
            );
        }

        throw new IllegalArgumentException(
                "Invalid \"base_model\" in Copycat model"
        );
    }

    private static Map<ResourceLocation, JsonObject> loadModels(
            ResourceManager resourceManager
    ) {
        Map<ResourceLocation, JsonObject> models =
                new HashMap<>();

        Map<ResourceLocation, Resource> resources =
                resourceManager.listResources(
                        "models",
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

                if (!json.has("loader")) {
                    continue;
                }

                String loader =
                        json.get("loader").getAsString();

                String expectedLoader =
                        ResourceLocation.fromNamespaceAndPath(
                                ModConstants.MOD_ID,
                                CopycatConstants.MODEL_LOADER_ID
                        ).toString();

                if (!expectedLoader.equals(loader)) {
                    continue;
                }

                /*
                 * ResourceLocation of:
                 *
                 * assets/scp_genesis/models/block/foo.json
                 *
                 * becomes:
                 *
                 * scp_genesis:block/foo
                 */
                String path =
                        resourceLocation.getPath();

                if (path.startsWith("models/")) {
                    path = path.substring(
                            "models/".length()
                    );
                }

                if (path.endsWith(".json")) {
                    path = path.substring(
                            0,
                            path.length() - ".json".length()
                    );
                }

                ResourceLocation modelId =
                        ResourceLocation.fromNamespaceAndPath(
                                resourceLocation.getNamespace(),
                                path
                        );

                models.put(
                        modelId,
                        json
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

        return models;
    }
}