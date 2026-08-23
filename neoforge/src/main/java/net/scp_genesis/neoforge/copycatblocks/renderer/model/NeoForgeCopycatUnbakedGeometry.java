package net.scp_genesis.neoforge.copycatblocks.renderer.model;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.scp_genesis.neoforge.copycatblocks.renderer.geometry.NeoForgeCopycatGeometry;
import net.scp_genesis.neoforge.copycatblocks.renderer.geometry.NeoForgeCopycatGeometryCube;
import net.scp_genesis.neoforge.copycatblocks.renderer.geometry.NeoForgeCopycatGeometrySlab;
import net.scp_genesis.neoforge.copycatblocks.renderer.geometry.NeoForgeCopycatGeometryStairs;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatModelBakeHelper;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatStairsModelHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public final class NeoForgeCopycatUnbakedGeometry
        implements IUnbakedGeometry<NeoForgeCopycatUnbakedGeometry> {

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
    public NeoForgeCopycatUnbakedGeometry(
            ResourceLocation baseModel
    ) {
        this.geometryType = GeometryType.CUBE;
        this.baseModel = baseModel;
        this.baseModels = null;
    }

    /**
     * Constructor used by multipart Copycat Blocks such as the Slab.
     */
    public NeoForgeCopycatUnbakedGeometry(
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

            BakedModel bakedBaseModel = NeoForgeCopycatModelBakeHelper.bakeModel(
                    baker,
                    baseModel,
                    modelState,
                    copycatSpriteGetter
            );

            NeoForgeCopycatGeometry geometry =
                    new NeoForgeCopycatGeometryCube(
                            bakedBaseModel
                    );

            return new NeoForgeCopycatBakedModel(
                    geometry
            );
        }

        /*
         * Multipart Copycat model.
         *
         * Used by Copycat Slab and future multipart blocks.
         */
        if (geometryType == GeometryType.SLAB) {

            BakedModel bottomModel = NeoForgeCopycatModelBakeHelper.bakeModel(
                    baker,
                    Objects.requireNonNull(baseModels).get("bottom"),
                    modelState,
                    copycatSpriteGetter
            );

            BakedModel topModel = NeoForgeCopycatModelBakeHelper.bakeModel(
                    baker,
                    Objects.requireNonNull(baseModels).get("top"),
                    modelState,
                    copycatSpriteGetter
            );

            BakedModel doubleModel = NeoForgeCopycatModelBakeHelper.bakeModel(
                    baker,
                    Objects.requireNonNull(baseModels).get("double"),
                    modelState,
                    copycatSpriteGetter
            );

            ResourceLocation doubleSecondaryLocation =
                    Objects.requireNonNull(baseModels).get("double_secondary");

            BakedModel doubleSecondaryModel = NeoForgeCopycatModelBakeHelper.bakeModel(
                    baker,
                    doubleSecondaryLocation,
                    modelState,
                    copycatAltSpriteGetter
            );

            NeoForgeCopycatGeometry geometry =
                    new NeoForgeCopycatGeometrySlab(
                            bottomModel,
                            topModel,
                            doubleSecondaryModel,
                            doubleModel
                    );

            return new NeoForgeCopycatBakedModel(
                    geometry
            );
        }

        if (geometryType == GeometryType.STAIRS) {

            Map<NeoForgeCopycatStairsModelHelper.StairModelKey, BakedModel> stairsModels =
                    NeoForgeCopycatStairsModelHelper.bakeStairsModels(
                            baker,
                            Objects.requireNonNull(baseModels),
                            copycatSpriteGetter
                    );

            NeoForgeCopycatGeometry geometry =
                    new NeoForgeCopycatGeometryStairs(
                            stairsModels
                    );

            return new NeoForgeCopycatBakedModel(
                    geometry
            );
        }

        throw new IllegalStateException(
                "Copycat model has no base model"
        );
    }
}