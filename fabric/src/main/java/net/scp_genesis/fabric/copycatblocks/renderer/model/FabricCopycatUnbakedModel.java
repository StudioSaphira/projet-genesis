package net.scp_genesis.fabric.copycatblocks.renderer.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class FabricCopycatUnbakedModel
        implements UnbakedModel {

    public enum GeometryType {
        CUBE,
        SLAB,
        STAIRS
    }

    private final ResourceLocation baseModel;
    private final Map<String, ResourceLocation> baseModels;
    private final GeometryType geometryType;

    /**
     * Constructor for simple Copycat models such as Cube.
     */
    public FabricCopycatUnbakedModel(
            @NotNull ResourceLocation baseModel
    ) {
        this.geometryType = GeometryType.CUBE;
        this.baseModel = baseModel;
        this.baseModels = null;
    }

    /**
     * Constructor for multipart Copycat models.
     */
    public FabricCopycatUnbakedModel(
            @NotNull GeometryType geometryType,
            @NotNull Map<String, ResourceLocation> baseModels
    ) {
        this.geometryType = geometryType;
        this.baseModel = null;
        this.baseModels = baseModels;
    }

    public GeometryType getGeometryType() {
        return geometryType;
    }

    public ResourceLocation getBaseModel() {
        return baseModel;
    }

    public Map<String, ResourceLocation> getBaseModels() {
        return baseModels;
    }

    @Override
    public void resolveParents(
            @NotNull Function<ResourceLocation, UnbakedModel> modelLoader
    ) {
        // Models will be fixed during baking.
    }

    @Override
    public BakedModel bake(
            @NotNull ModelBaker baker,
            @NotNull Function<Material, TextureAtlasSprite> spriteGetter,
            @NotNull ModelState modelState
    ) {
        throw new UnsupportedOperationException(
                "FabricCopycatUnbakedModel baking not implemented yet"
        );
    }

    @Override
    public @NotNull List<ResourceLocation> getDependencies() {
        return List.of();
    }
}