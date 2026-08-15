package net.scp_genesis.copycatblocks.renderer.model;

import com.google.gson.JsonParseException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.scp_genesis.constants.ModConstants;
import net.scp_genesis.copycatblocks.util.CopycatConstants;
import org.jetbrains.annotations.NotNull;

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

        ResourceLocation baseModel =
                ResourceLocation.parse(
                        json.get("base_model").getAsString()
                );

        return new CopycatUnbakedGeometry(baseModel);
    }
}