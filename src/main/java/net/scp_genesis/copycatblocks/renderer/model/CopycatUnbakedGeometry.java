package net.scp_genesis.copycatblocks.renderer.model;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.scp_genesis.copycatblocks.renderer.geometry.CopycatGeometry;
import net.scp_genesis.copycatblocks.renderer.geometry.CopycatGeometryCube;
import net.scp_genesis.copycatblocks.renderer.geometry.CopycatGeometrySlab;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Function;

public final class CopycatUnbakedGeometry
        implements IUnbakedGeometry<CopycatUnbakedGeometry> {

    private final ResourceLocation baseModel;
    private final Map<String, ResourceLocation> baseModels;

    /**
     * Constructor used by simple Copycat Blocks such as the Cube.
     */
    public CopycatUnbakedGeometry(
            ResourceLocation baseModel
    ) {
        this.baseModel = baseModel;
        this.baseModels = null;
    }

    /**
     * Constructor used by multipart Copycat Blocks such as the Slab.
     */
    public CopycatUnbakedGeometry(
            Map<String, ResourceLocation> baseModels
    ) {
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
        if (baseModel != null) {

            BakedModel bakedBaseModel = bakeModel(
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
        if (baseModels != null) {

            BakedModel bottomModel = bakeModel(
                    baker,
                    baseModels.get("bottom"),
                    modelState,
                    copycatSpriteGetter
            );

            BakedModel topModel = bakeModel(
                    baker,
                    baseModels.get("top"),
                    modelState,
                    copycatSpriteGetter
            );

            ResourceLocation doubleSecondaryLocation =
                    baseModels.get("double_secondary");

            BakedModel doubleSecondaryModel = bakeModel(
                    baker,
                    doubleSecondaryLocation,
                    modelState,
                    copycatAltSpriteGetter
            );

            BakedModel doubleModel = bakeModel(
                    baker,
                    baseModels.get("double"),
                    modelState,
                    copycatSpriteGetter
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

        throw new IllegalStateException(
                "Copycat model has no base model"
        );
    }

    private static BakedModel bakeModel(
            ModelBaker baker,
            ResourceLocation modelLocation,
            ModelState modelState,
            Function<Material, TextureAtlasSprite> spriteGetter
    ) {
        if (modelLocation == null) {
            throw new IllegalStateException(
                    "Missing Copycat base model"
            );
        }

        BakedModel model = baker.bake(
                modelLocation,
                modelState,
                spriteGetter
        );

        if (model == null) {
            throw new IllegalStateException(
                    "Failed to bake Copycat base model: "
                            + modelLocation
            );
        }

        return model;
    }
}