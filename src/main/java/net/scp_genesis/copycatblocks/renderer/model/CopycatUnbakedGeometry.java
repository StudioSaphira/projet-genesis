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

        Function<Material, TextureAtlasSprite> copycatSpriteGetter =
                material -> copycatSprite;

        TextureAtlasSprite copycatAltSprite =
                spriteGetter.apply(
                        context.getMaterial("all_alt")
                );

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

            return new CopycatBakedModel(
                    bakedBaseModel
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

            BakedModel doubleTopModel = bakeModel(
                    baker,
                    baseModels.get("top"),
                    modelState,
                    copycatAltSpriteGetter
            );

            BakedModel doubleModel = bakeModel(
                    baker,
                    baseModels.get("double"),
                    modelState,
                    copycatSpriteGetter
            );

            return new CopycatBakedModel(
                    bottomModel,
                    topModel,
                    doubleTopModel,
                    doubleModel
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