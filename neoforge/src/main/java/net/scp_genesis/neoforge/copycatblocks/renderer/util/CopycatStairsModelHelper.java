package net.scp_genesis.neoforge.copycatblocks.renderer.util;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.neoforged.neoforge.client.model.SimpleModelState;

import java.util.Map;
import java.util.function.Function;

public final class CopycatStairsModelHelper {

    private CopycatStairsModelHelper() {
    }

    public static Map<StairModelKey, BakedModel> bakeStairsModels(
            ModelBaker baker,
            Map<String, ResourceLocation> baseModels,
            Function<Material, TextureAtlasSprite> spriteGetter
    ) {
        Map<StairModelKey, BakedModel> models =
                new java.util.HashMap<>();

        for (Direction facing : Direction.Plane.HORIZONTAL) {
            for (Half half : Half.values()) {
                for (StairsShape shape : StairsShape.values()) {

                    String modelKey = switch (shape) {
                        case STRAIGHT -> "straight";
                        case INNER_LEFT, INNER_RIGHT -> "inner";
                        case OUTER_LEFT, OUTER_RIGHT -> "outer";
                    };

                    BlockModelRotation rotation =
                            getStairsRotation(
                                    facing,
                                    half,
                                    shape
                            );

                    ModelState modelState =
                            new SimpleModelState(
                                    rotation.getRotation(),
                                    true
                            );

                    BakedModel model = CopycatModelBakeHelper.bakeModel(
                            baker,
                            baseModels.get(modelKey),
                            modelState,
                            spriteGetter
                    );

                    models.put(
                            new StairModelKey(
                                    facing,
                                    half,
                                    shape
                            ),
                            model
                    );
                }
            }
        }

        return models;
    }

    @SuppressWarnings("all")
    public static BlockModelRotation getStairsRotation(
            Direction facing,
            Half half,
            StairsShape shape
    ) {
        return switch (half) {

            case BOTTOM -> switch (facing) {

                case EAST -> switch (shape) {
                    case STRAIGHT -> BlockModelRotation.X0_Y0;
                    case INNER_LEFT -> BlockModelRotation.X0_Y270;
                    case INNER_RIGHT -> BlockModelRotation.X0_Y0;
                    case OUTER_LEFT -> BlockModelRotation.X0_Y270;
                    case OUTER_RIGHT -> BlockModelRotation.X0_Y0;
                };

                case NORTH -> switch (shape) {
                    case STRAIGHT -> BlockModelRotation.X0_Y270;
                    case INNER_LEFT -> BlockModelRotation.X0_Y180;
                    case INNER_RIGHT -> BlockModelRotation.X0_Y270;
                    case OUTER_LEFT -> BlockModelRotation.X0_Y180;
                    case OUTER_RIGHT -> BlockModelRotation.X0_Y270;
                };

                case SOUTH -> switch (shape) {
                    case STRAIGHT -> BlockModelRotation.X0_Y90;
                    case INNER_LEFT -> BlockModelRotation.X0_Y0;
                    case INNER_RIGHT -> BlockModelRotation.X0_Y90;
                    case OUTER_LEFT -> BlockModelRotation.X0_Y0;
                    case OUTER_RIGHT -> BlockModelRotation.X0_Y90;
                };

                case WEST -> switch (shape) {
                    case STRAIGHT -> BlockModelRotation.X0_Y180;
                    case INNER_LEFT -> BlockModelRotation.X0_Y90;
                    case INNER_RIGHT -> BlockModelRotation.X0_Y180;
                    case OUTER_LEFT -> BlockModelRotation.X0_Y90;
                    case OUTER_RIGHT -> BlockModelRotation.X0_Y180;
                };

                default -> throw new IllegalStateException(
                        "Unexpected stair facing: " + facing
                );
            };

            case TOP -> switch (facing) {

                case EAST -> switch (shape) {
                    case STRAIGHT -> BlockModelRotation.X180_Y0;
                    case INNER_LEFT -> BlockModelRotation.X180_Y0;
                    case INNER_RIGHT -> BlockModelRotation.X180_Y90;
                    case OUTER_LEFT -> BlockModelRotation.X180_Y0;
                    case OUTER_RIGHT -> BlockModelRotation.X180_Y90;
                };

                case NORTH -> switch (shape) {
                    case STRAIGHT -> BlockModelRotation.X180_Y270;
                    case INNER_LEFT -> BlockModelRotation.X180_Y270;
                    case INNER_RIGHT -> BlockModelRotation.X180_Y0;
                    case OUTER_LEFT -> BlockModelRotation.X180_Y270;
                    case OUTER_RIGHT -> BlockModelRotation.X180_Y0;
                };

                case SOUTH -> switch (shape) {
                    case STRAIGHT -> BlockModelRotation.X180_Y90;
                    case INNER_LEFT -> BlockModelRotation.X180_Y90;
                    case INNER_RIGHT -> BlockModelRotation.X180_Y180;
                    case OUTER_LEFT -> BlockModelRotation.X180_Y90;
                    case OUTER_RIGHT -> BlockModelRotation.X180_Y180;
                };

                case WEST -> switch (shape) {
                    case STRAIGHT -> BlockModelRotation.X180_Y180;
                    case INNER_LEFT -> BlockModelRotation.X180_Y180;
                    case INNER_RIGHT -> BlockModelRotation.X180_Y270;
                    case OUTER_LEFT -> BlockModelRotation.X180_Y180;
                    case OUTER_RIGHT -> BlockModelRotation.X180_Y270;
                };

                default -> throw new IllegalStateException(
                        "Unexpected stair facing: " + facing
                );
            };
        };
    }


    public record StairModelKey(
            Direction facing,
            Half half,
            StairsShape shape
    ) {
    }
}