package net.scp_genesis.fabric.copycatblocks.renderer.model;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.scp_genesis.fabric.copycatblocks.renderer.geometry.FabricCopycatGeometry;
import net.scp_genesis.fabric.copycatblocks.renderer.geometry.FabricCopycatGeometryCube;
import net.scp_genesis.fabric.copycatblocks.renderer.geometry.FabricCopycatGeometrySlab;
import net.scp_genesis.fabric.copycatblocks.renderer.geometry.FabricCopycatGeometryStairs;
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatModelBakeHelper;
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatStairsModelHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
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
     * Constructor used by simple Copycat models such as the Cube.
     */
    public FabricCopycatUnbakedModel(
            ResourceLocation baseModel
    ) {
        this.geometryType = GeometryType.CUBE;
        this.baseModel = baseModel;
        this.baseModels = null;
    }

    @Override
    public void resolveParents(
            Function<ResourceLocation, UnbakedModel> modelGetter
    ) {
        /*
         * Copycat models do not use the vanilla parent system.
         *
         * The base models are explicitly baked later through
         * ModelBaker, so there is nothing to resolve here.
         */
    }

    @Override
    public @NotNull Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    /**
     * Constructor used by multipart Copycat models.
     */
    public FabricCopycatUnbakedModel(
            GeometryType geometryType,
            Map<String, ResourceLocation> baseModels
    ) {
        this.geometryType = geometryType;
        this.baseModel = null;
        this.baseModels = baseModels;
    }


    @Override
    public BakedModel bake(
            ModelBaker baker,
            Function<Material, TextureAtlasSprite> spriteGetter,
            ModelState modelState
    ) {
        /*
         * The Copycat sprite is resolved from the model material.
         *
         * The exact material used here corresponds to the "all"
         * texture declared by the Copycat model JSON.
         */
        @SuppressWarnings("deprecation")
        TextureAtlasSprite copycatSprite =
                spriteGetter.apply(
                        new Material(
                                net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS,
                                ResourceLocation.fromNamespaceAndPath(
                                        "scp_genesis",
                                        "block/copycat_block"
                                )
                        )
                );

        @SuppressWarnings("deprecation")
        TextureAtlasSprite copycatAltSprite =
                spriteGetter.apply(
                        new Material(
                                net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS,
                                ResourceLocation.fromNamespaceAndPath(
                                        "scp_genesis",
                                        "block/copycat_block_alt"
                                )
                        )
                );

        Function<Material, TextureAtlasSprite> copycatSpriteGetter =
                material -> copycatSprite;

        Function<Material, TextureAtlasSprite> copycatAltSpriteGetter =
                material -> copycatAltSprite;

        /*
         * ============================================================
         * CUBE
         * ============================================================
         */

        if (geometryType == GeometryType.CUBE) {

            @SuppressWarnings("DataFlowIssue")
            BakedModel bakedBaseModel =
                    FabricCopycatModelBakeHelper.bakeModel(
                            baker,
                            baseModel,
                            modelState
                    );

            FabricCopycatGeometry geometry =
                    new FabricCopycatGeometryCube(
                            bakedBaseModel
                    );

            return new FabricCopycatBakedModel(
                    geometry
            );
        }

        /*
         * ============================================================
         * SLAB
         * ============================================================
         */

        if (geometryType == GeometryType.SLAB) {

            BakedModel bottomModel =
                    FabricCopycatModelBakeHelper.bakeModel(
                            baker,
                            Objects.requireNonNull(baseModels).get("bottom"),
                            modelState
                    );

            BakedModel topModel =
                    FabricCopycatModelBakeHelper.bakeModel(
                            baker,
                            Objects.requireNonNull(baseModels).get("top"),
                            modelState
                    );

            BakedModel doubleModel =
                    FabricCopycatModelBakeHelper.bakeModel(
                            baker,
                            Objects.requireNonNull(baseModels).get("double"),
                            modelState
                    );

            BakedModel doubleSecondaryModel =
                    FabricCopycatModelBakeHelper.bakeModel(
                            baker,
                            Objects.requireNonNull(baseModels).get("double_secondary"),
                            modelState
                    );

            FabricCopycatGeometry geometry =
                    new FabricCopycatGeometrySlab(
                            bottomModel,
                            topModel,
                            doubleSecondaryModel,
                            doubleModel
                    );

            return new FabricCopycatBakedModel(
                    geometry
            );
        }

        /*
         * ============================================================
         * STAIRS
         * ============================================================
         */

        if (geometryType == GeometryType.STAIRS) {

            Map<
                    FabricCopycatStairsModelHelper.StairModelKey,
                    BakedModel
                    > stairsModels =
                    FabricCopycatStairsModelHelper.bakeStairsModels(
                            baker,
                            Objects.requireNonNull(baseModels),
                            copycatSpriteGetter
                    );

            FabricCopycatGeometry geometry =
                    new FabricCopycatGeometryStairs(
                            stairsModels
                    );

            return new FabricCopycatBakedModel(
                    geometry
            );
        }

        throw new IllegalStateException(
                "Copycat model has no base model"
        );
    }
}