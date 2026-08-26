package net.scp_genesis.common.copycatblocks.geometry.stairs;

import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

/**
 * Defines the rotation of a Copycat Stairs model.
 *
 * @param key the directions and shape defined in CopycatStairsModelKey.
 */
public record CopycatStairsRotation(
        CopycatStairsModelKey key
) {
    /**
     * Returns the Minecraft model rotation corresponding
     * to this stair configuration.
     */
    @SuppressWarnings("DuplicateBranchesInSwitch")
    public BlockModelRotation getRotation() {
        Direction facing = key.facing();
        Half half = key.half();
        StairsShape shape = key.shape();
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
}