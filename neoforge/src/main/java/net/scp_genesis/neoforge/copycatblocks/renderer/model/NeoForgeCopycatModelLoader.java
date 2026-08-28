package net.scp_genesis.neoforge.copycatblocks.renderer.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.scp_genesis.common.constants.ModConstants;
import net.scp_genesis.common.copycatblocks.util.CopycatConstants;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Geometry loader used by standard Copycat models.
 *
 * <p>This loader reads Copycat model JSON files containing a
 * {@code base_model} definition and creates the corresponding
 * {@link NeoForgeCopycatUnbakedGeometry} instance.</p>
 */
public final class NeoForgeCopycatModelLoader
        implements IGeometryLoader<NeoForgeCopycatUnbakedGeometry> {

    /**
     * Standard Copycat loader identifier.
     */
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(
                    ModConstants.MOD_ID,
                    CopycatConstants.MODEL_LOADER_ID
            );

    /**
     * Copycat Slope loader identifier.
     */
    public static final ResourceLocation ID_SLOPE =
            ResourceLocation.fromNamespaceAndPath(
                    ModConstants.MOD_ID,
                    CopycatConstants.MODEL_SLOPE_ID
            );

    /**
     * Singleton instance for standard Copycat models.
     */
    public static final NeoForgeCopycatModelLoader INSTANCE =
            new NeoForgeCopycatModelLoader();

    /**
     * Singleton instance for Copycat Slope models.
     */
    public static final NeoForgeCopycatSlopeModelLoader SLOPE_INSTANCE =
            new NeoForgeCopycatSlopeModelLoader();

    /**
     * Prevent external instantiation.
     */
    private NeoForgeCopycatModelLoader() {
    }

    /**
     * Reads a standard Copycat model.
     *
     * <p>Standard Copycat models require a {@code base_model}
     * property. The property may either be a single model location
     * or an object describing multipart geometry.</p>
     */
    @Override
    public @NotNull NeoForgeCopycatUnbakedGeometry read(
            @NotNull JsonObject json,
            @NotNull JsonDeserializationContext context
    ) throws JsonParseException {

        ModConstants.LOGGER.info(
                "[COPYCAT] Loading Copycat Geometry"
        );

        if (!json.has("base_model")) {
            throw new JsonParseException(
                    "Copycat model is missing required property: \"base_model\""
            );
        }

        var baseModelElement =
                json.get("base_model");

        /*
         * ============================================================
         * SIMPLE COPYCAT MODEL
         * ============================================================
         */

        if (baseModelElement.isJsonPrimitive()) {

            ResourceLocation baseModel =
                    ResourceLocation.parse(
                            baseModelElement.getAsString()
                    );

            return new NeoForgeCopycatUnbakedGeometry(
                    baseModel
            );
        }

        /*
         * ============================================================
         * MULTIPART COPYCAT MODEL
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

                return new NeoForgeCopycatUnbakedGeometry(
                        NeoForgeCopycatUnbakedGeometry.GeometryType.STAIRS,
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

            return new NeoForgeCopycatUnbakedGeometry(
                    NeoForgeCopycatUnbakedGeometry.GeometryType.SLAB,
                    models
            );
        }

        throw new JsonParseException(
                "Invalid \"base_model\" in Copycat model"
        );
    }

    /*
     * ================================================================
     * SLOPE LOADER
     * ================================================================
     */

    /**
     * Dedicated geometry loader for Copycat Slopes.
     *
     * <p>The Slope does not use a {@code base_model}. Its geometry is
     * generated procedurally by {@link net.scp_genesis.neoforge.copycatblocks.renderer.geometry.NeoForgeCopycatGeometrySlope}.</p>
     */
    public static final class NeoForgeCopycatSlopeModelLoader
            implements IGeometryLoader<NeoForgeCopycatUnbakedGeometry> {

        private NeoForgeCopycatSlopeModelLoader() {
        }

        @Override
        public @NotNull NeoForgeCopycatUnbakedGeometry read(
                @NotNull JsonObject json,
                @NotNull JsonDeserializationContext context
        ) throws JsonParseException {

            ModConstants.LOGGER.info(
                    "[COPYCAT] Loading Copycat Slope Geometry"
            );

            return new NeoForgeCopycatUnbakedGeometry(
                    NeoForgeCopycatUnbakedGeometry.GeometryType.SLOPE,
                    Map.of()
            );
        }
    }
}