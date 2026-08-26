package net.scp_genesis.neoforge.copycatblocks.renderer.model;

import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.scp_genesis.common.constants.ModConstants;
import net.scp_genesis.common.copycatblocks.util.CopycatConstants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Geometry loader used by Copycat models.
 *
 * <p>This loader reads the Copycat model JSON and creates the corresponding
 * {@link NeoForgeCopycatUnbakedGeometry} instance used during model baking.</p>
 */
public final class NeoForgeCopycatModelLoader implements IGeometryLoader<NeoForgeCopycatUnbakedGeometry> {

    /**
     * Loader identifier.
     */
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(
                    ModConstants.MOD_ID,
                    CopycatConstants.MODEL_LOADER_ID
            );

    public static final ResourceLocation ID_SLOPE =
            ResourceLocation.fromNamespaceAndPath(
                    ModConstants.MOD_ID,
                    CopycatConstants.MODEL_SLOPE_ID
            );

    /**
     * Singleton instance.
     */
    public static final NeoForgeCopycatModelLoader INSTANCE =
            new NeoForgeCopycatModelLoader();

    /**
     * Prevent external instantiation.
     */
    private NeoForgeCopycatModelLoader() {
    }

    @Override
    public @NotNull NeoForgeCopycatUnbakedGeometry read(
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
         * Simple Copycat Model
         */
        if (baseModelElement.isJsonPrimitive()) {

            ResourceLocation baseModel =
                    ResourceLocation.parse(
                            baseModelElement.getAsString()
                    );

            return new NeoForgeCopycatUnbakedGeometry(baseModel);
        }

        /*
         * Multipart Copycat Model
         */
        if (baseModelElement.isJsonObject()) {

            JsonObject baseModels =
                    baseModelElement.getAsJsonObject();

            /*
             * Stair Copycat Model.
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
                                    baseModels.get(key).getAsString()
                            )
                    );
                }

                return new NeoForgeCopycatUnbakedGeometry(
                        NeoForgeCopycatUnbakedGeometry.GeometryType.STAIRS,
                        models
                );
            }

            /*
             * Slab Copycat Model.
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

            return new NeoForgeCopycatUnbakedGeometry(
                    NeoForgeCopycatUnbakedGeometry.GeometryType.SLAB,
                    models
            );
        }

        throw new JsonParseException(
                "Invalid \"base_model\" in Copycat model"
        );
    }
}