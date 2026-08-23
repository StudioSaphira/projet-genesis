package net.scp_genesis.fabric.copycatblocks.renderer.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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

    private static final Gson GSON = new Gson();

    private FabricCopycatModelLoader() {
    }

    /**
     * Loads all Copycat model definitions from the ResourceManager.
     */
    public static CompletableFuture<
            Map<ResourceLocation, FabricCopycatUnbakedModel>
            > load(
            ResourceManager resourceManager,
            Executor executor
    ) {
        return CompletableFuture.supplyAsync(
                () -> loadModels(resourceManager),
                executor
        );
    }

    private static Map<
            ResourceLocation,
            FabricCopycatUnbakedModel
            > loadModels(
            ResourceManager resourceManager
    ) {
        Map<
                ResourceLocation,
                FabricCopycatUnbakedModel
                > models = new HashMap<>();

        /*
         * We currently know the Copycat models explicitly.
         *
         * This can later be replaced by a resource scan if we
         * want arbitrary Copycat model definitions.
         */
        loadModel(
                resourceManager,
                ResourceLocation.fromNamespaceAndPath(
                        ModConstants.MOD_ID,
                        "models/block/copycat_cube.json"
                ),
                models
        );

        loadModel(
                resourceManager,
                ResourceLocation.fromNamespaceAndPath(
                        ModConstants.MOD_ID,
                        "models/block/copycat_slab.json"
                ),
                models
        );

        loadModel(
                resourceManager,
                ResourceLocation.fromNamespaceAndPath(
                        ModConstants.MOD_ID,
                        "models/block/copycat_stairs.json"
                ),
                models
        );

        return models;
    }

    private static void loadModel(
            ResourceManager resourceManager,
            ResourceLocation resourceLocation,
            Map<ResourceLocation, FabricCopycatUnbakedModel> models
    ) {
        try {
            Resource resource =
                    resourceManager.getResource(resourceLocation)
                            .orElseThrow(
                                    () -> new IllegalStateException(
                                            "Missing Copycat model: "
                                                    + resourceLocation
                                    )
                            );

            try (Reader reader = resource.openAsReader()) {

                JsonObject json =
                        GSON.fromJson(
                                reader,
                                JsonObject.class
                        );

                FabricCopycatUnbakedModel model =
                        parseModel(json);

                /*
                 * Convert:
                 *
                 * assets/scp_genesis/models/block/foo.json
                 *
                 * into:
                 *
                 * scp_genesis:block/foo
                 */
                ResourceLocation modelId =
                        ResourceLocation.fromNamespaceAndPath(
                                resourceLocation.getNamespace(),
                                resourceLocation.getPath()
                                        .substring(
                                                "models/".length(),
                                                resourceLocation.getPath().length()
                                                        - ".json".length()
                                        )
                        );

                models.put(
                        modelId,
                        model
                );
            }

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Failed to load Copycat model: "
                            + resourceLocation,
                    exception
            );
        }
    }

    private static FabricCopycatUnbakedModel parseModel(
            JsonObject json
    ) {
        if (!json.has("base_model")) {
            throw new JsonParseException(
                    "Copycat model is missing required property: \"base_model\""
            );
        }

        var baseModelElement =
                json.get("base_model");

        /*
         * Simple Copycat model.
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
         * Multipart Copycat model.
         */
        if (baseModelElement.isJsonObject()) {

            JsonObject baseModels =
                    baseModelElement.getAsJsonObject();

            /*
             * Stairs.
             */
            if (baseModels.has("straight")
                    && baseModels.has("inner")
                    && baseModels.has("outer")) {

                Map<String, ResourceLocation> models =
                        new HashMap<>();

                for (String key : new String[]{
                        "straight",
                        "inner",
                        "outer"
                }) {
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
             * Slab.
             */
            Map<String, ResourceLocation> models =
                    new HashMap<>();

            for (String key : new String[]{
                    "bottom",
                    "top",
                    "double",
                    "double_secondary"
            }) {

                if (!baseModels.has(key)) {
                    throw new JsonParseException(
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

        throw new JsonParseException(
                "Invalid \"base_model\" in Copycat model"
        );
    }
}