package net.scp_genesis.common.copycatblocks.renderer.model;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.scp_genesis.common.copycatblocks.renderer.geometry.CopycatGeometry;
import net.scp_genesis.common.copycatblocks.renderer.geometry.CopycatGeometryCube;
import net.scp_genesis.common.copycatblocks.renderer.geometry.CopycatGeometrySlab;
import net.scp_genesis.common.copycatblocks.renderer.geometry.CopycatGeometryStairs;
import net.scp_genesis.common.copycatblocks.renderer.util.CopycatModelBakeHelper;
import net.scp_genesis.common.copycatblocks.renderer.util.CopycatStairsModelHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class CopycatUnbakedGeometry
        implements IUnbakedGeometry<CopycatUnbakedGeometry> {

    public enum GeometryType {
        CUBE,
        SLAB,
        STAIRS
    }
    private final ResourceLocation baseModel;
    private final Map<String, ResourceLocation> baseModels;
    private final GeometryType geometryType;

    /**
     * Constructor used by simple Copycat Blocks such as the Cube.
     */
    public CopycatUnbakedGeometry(
            ResourceLocation baseModel
    ) {
        this.geometryType = GeometryType.CUBE;
        this.baseModel = baseModel;
        this.baseModels = null;
    }

    /**
     * Constructor used by multipart Copycat Blocks such as the Slab.
     */
    public CopycatUnbakedGeometry(
            GeometryType geometryType,
            Map<String, ResourceLocation> baseModels
    ) {
        this.geometryType = geometryType;
        this.baseModel = null;
        this.baseModels = baseModels;
    }

    @Override
    public @NotNull BakedModel bake(
            @NotNull IGeometryBakingContext context,
            @NotNull ModelBaker baker,
            @NotNull Function<Material, TextureAtlasSprite> spriteGetter,
            @NotNull ModelState modelState,
            @NotNull ItemOverrides overrides
    ) {

        TextureAtlasSprite copycatSprite =
                spriteGetter.apply(
                        context.getMaterial("all")
                );

        TextureAtlasSprite copycatAltSprite =
                spriteGetter.apply(
                        context.getMaterial("all_alt")
                );

        Function<Material, TextureAtlasSprite> copycatSpriteGetter =
                material -> copycatSprite;

        Function<Material, TextureAtlasSprite> copycatAltSpriteGetter =
                material -> copycatAltSprite;

        /*
         * Simple Copycat model.
         *
         * Used by Copycat Cube and other single-part blocks.
         */
        if (geometryType == GeometryType.CUBE) {

            BakedModel bakedBaseModel = CopycatModelBakeHelper.bakeModel(
                    baker,
                    baseModel,
                    modelState,
                    copycatSpriteGetter
            );

            CopycatGeometry geometry =
                    new CopycatGeometryCube(
                            bakedBaseModel
                    );

            return new CopycatBakedModel(
                    geometry
            );
        }

        /*
         * Multipart Copycat model.
         *
         * Used by Copycat Slab and future multipart blocks.
         */
        if (geometryType == GeometryType.SLAB) {

            BakedModel bottomModel = CopycatModelBakeHelper.bakeModel(
                    baker,
                    Objects.requireNonNull(baseModels).get("bottom"),
                    modelState,
                    copycatSpriteGetter
            );

            BakedModel topModel = CopycatModelBakeHelper.bakeModel(
                    baker,
                    Objects.requireNonNull(baseModels).get("top"),
                    modelState,
                    copycatSpriteGetter
            );

            BakedModel doubleModel = CopycatModelBakeHelper.bakeModel(
                    baker,
                    Objects.requireNonNull(baseModels).get("double"),
                    modelState,
                    copycatSpriteGetter
            );

            ResourceLocation doubleSecondaryLocation =
                    Objects.requireNonNull(baseModels).get("double_secondary");

            BakedModel doubleSecondaryModel = CopycatModelBakeHelper.bakeModel(
                    baker,
                    doubleSecondaryLocation,
                    modelState,
                    copycatAltSpriteGetter
            );

            CopycatGeometry geometry =
                    new CopycatGeometrySlab(
                            bottomModel,
                            topModel,
                            doubleSecondaryModel,
                            doubleModel
                    );

            return new CopycatBakedModel(
                    geometry
            );
        }

        if (geometryType == GeometryType.STAIRS) {

            Map<CopycatStairsModelHelper.StairModelKey, BakedModel> stairsModels =
                    CopycatStairsModelHelper.bakeStairsModels(
                            baker,
                            Objects.requireNonNull(baseModels),
                            copycatSpriteGetter
                    );

            CopycatGeometry geometry =
                    new CopycatGeometryStairs(
                            stairsModels
                    );

            return new CopycatBakedModel(
                    geometry
            );
        }

        throw new IllegalStateException(
                "Copycat model has no base model"
        );
    }
}