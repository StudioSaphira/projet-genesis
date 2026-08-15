package net.scp_genesis.copycatblocks.renderer.model;

import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.scp_genesis.constants.ModConstants;
import net.scp_genesis.copycatblocks.util.CopycatConstants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Geometry loader used by Copycat models.
 *
 * <p>This loader reads the Copycat model JSON and creates the corresponding
 * {@link CopycatUnbakedGeometry} instance used during model baking.</p>
 */
public final class CopycatGeometryLoader
    implements IGeometryLoader<CopycatUnbakedGeometry> {

    /**
     * Loader identifier.
     */
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(
                    ModConstants.MOD_ID,
                    CopycatConstants.MODEL_LOADER_ID
            );

    /**
     * Singleton instance.
     */
    public static final CopycatGeometryLoader INSTANCE =
            new CopycatGeometryLoader();

    /**
     * Prevent external instantiation.
     */
    private CopycatGeometryLoader() {
    }

    @Override
    public @NotNull CopycatUnbakedGeometry read(
            @NotNull JsonObject json,
            @NotNull JsonDeserializationContext context
    ) throws JsonParseException {

        ModConstants.LOGGER.info("[COPYCAT] Loading Copycat Geometry");

        if (!json.has("base_model")) {
            throw new JsonParseException(
                    "Copycat model is missing required property: \"base_model\""
            );
        }

        var baseModelElement = json.get("base_model");

        /*
         * Simple Copycat model.
         *
         * Example:
         * "base_model": "minecraft:block/cube_all"
         */
        if (baseModelElement.isJsonPrimitive()) {

            ResourceLocation baseModel =
                    ResourceLocation.parse(
                            baseModelElement.getAsString()
                    );

            return new CopycatUnbakedGeometry(baseModel);
        }

        /*
         * Multipart Copycat model.
         *
         * Example:
         * "base_model": {
         *     "bottom": "...",
         *     "top": "...",
         *     "double": "..."
         * }
         */
        if (baseModelElement.isJsonObject()) {

            JsonObject baseModels =
                    baseModelElement.getAsJsonObject();

            String[] requiredModels = {
                    "bottom",
                    "top",
                    "double"
            };

            Map<String, ResourceLocation> models =
                    new HashMap<>();

            for (String key : requiredModels) {

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
                                baseModels.get(key).getAsString()
                        )
                );
            }

            return new CopycatUnbakedGeometry(models);
        }

        throw new JsonParseException(
                "Invalid \"base_model\" in Copycat model"
        );
    }
}