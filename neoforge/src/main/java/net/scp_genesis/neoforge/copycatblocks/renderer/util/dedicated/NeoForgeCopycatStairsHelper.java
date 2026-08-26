package net.scp_genesis.neoforge.copycatblocks.renderer.util.dedicated;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.scp_genesis.common.copycatblocks.geometry.stairs.CopycatGeometryStairs;
import net.scp_genesis.common.copycatblocks.geometry.stairs.CopycatStairsModelKey;
import net.scp_genesis.common.copycatblocks.geometry.stairs.CopycatStairsRotation;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatModelBakeHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class NeoForgeCopycatStairsHelper {

    private NeoForgeCopycatStairsHelper() {
    }

    public static Map<CopycatStairsModelKey, BakedModel> bakeStairsModels(
            ModelBaker baker,
            Map<String, ResourceLocation> baseModels,
            Function<Material, TextureAtlasSprite> spriteGetter
    ) {
        Map<CopycatStairsModelKey, BakedModel> models =
                new HashMap<>();

        for (Direction facing : Direction.Plane.HORIZONTAL) {
            for (Half half : Half.values()) {
                for (StairsShape shape : StairsShape.values()) {

                    CopycatStairsModelKey key =
                            new CopycatStairsModelKey(
                                    facing,
                                    half,
                                    shape
                            );

                    CopycatStairsRotation rotation =
                            new CopycatStairsRotation(key);

                    String modelName =
                            CopycatGeometryStairs.getModelName(key.shape());

                    ModelState modelState =
                            new SimpleModelState(
                                    rotation.getRotation().getRotation(),
                                    true
                            );

                    BakedModel model =
                            NeoForgeCopycatModelBakeHelper.bakeModel(
                                    baker,
                                    baseModels.get(modelName),
                                    modelState,
                                    spriteGetter
                            );

                    models.put(key, model);
                }
            }
        }

        return models;
    }
}