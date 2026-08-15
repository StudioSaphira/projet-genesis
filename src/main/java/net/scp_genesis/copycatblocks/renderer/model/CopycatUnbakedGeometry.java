package net.scp_genesis.copycatblocks.renderer.model;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.scp_genesis.constants.ModConstants;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public final class CopycatUnbakedGeometry
        implements IUnbakedGeometry<CopycatUnbakedGeometry> {

    private BlockModel parent;

    @Override
    public void resolveParents(
            @NotNull Function<ResourceLocation, UnbakedModel> modelGetter,
            @NotNull IGeometryBakingContext context
    ) {
        ResourceLocation parentLocation =
                ResourceLocation.withDefaultNamespace("block/cube_all");

        UnbakedModel model = modelGetter.apply(parentLocation);

        if (!(model instanceof BlockModel blockModel)) {
            throw new IllegalStateException(
                    "Copycat parent is not a BlockModel: " + parentLocation
            );
        }

        this.parent = blockModel;

        this.parent.resolveParents(modelGetter);
    }

    private final ResourceLocation baseModel;

    public CopycatUnbakedGeometry(ResourceLocation baseModel) {
        this.baseModel = baseModel;
    }

    @Override
    public @NotNull BakedModel bake(
            @NotNull IGeometryBakingContext context,
            @NotNull ModelBaker baker,
            @NotNull Function<Material, TextureAtlasSprite> spriteGetter,
            @NotNull ModelState modelState,
            @NotNull ItemOverrides overrides
    ) {
        ModConstants.LOGGER.info(
                "[COPYCAT] Baking Copycat Model: {}",
                context.getModelName()
        );

        TextureAtlasSprite copycatSprite =
                spriteGetter.apply(context.getMaterial("all"));

        Function<Material, TextureAtlasSprite> copycatSpriteGetter =
                material -> copycatSprite;

        BakedModel baseModelBaked = baker.bake(
                baseModel,
                modelState,
                copycatSpriteGetter
        );

        if (baseModelBaked == null) {
            throw new IllegalStateException(
                    "Failed to bake Copycat base model: " + baseModel
            );
        }

        return new CopycatBakedModel(baseModelBaked);
    }
}