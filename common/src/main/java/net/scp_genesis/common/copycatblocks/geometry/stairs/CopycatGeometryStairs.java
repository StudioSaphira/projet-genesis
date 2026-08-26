package net.scp_genesis.common.copycatblocks.geometry.stairs;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.StairsShape;

/**
 * Provides the common Minecraft logic used by Copycat Stairs geometries.
 *
 * <p>This class contains no Fabric or NeoForge specific code.
 * Platform implementations are responsible only for baking and rendering
 * the models described by this geometry.</p>
 */
public final class CopycatGeometryStairs {

    private CopycatGeometryStairs() {
    }

    /**
     * Creates the model key corresponding to the given Minecraft Stairs
     * block state.
     *
     * @param state the stairs block state
     * @return the corresponding model key
     */
    public static CopycatStairsModelKey getModelKey(
            BlockState state
    ) {
        return new CopycatStairsModelKey(
                state.getValue(BlockStateProperties.HORIZONTAL_FACING),
                state.getValue(BlockStateProperties.HALF),
                state.getValue(BlockStateProperties.STAIRS_SHAPE)
        );
    }

    /**
     * Returns the base model name associated with a stairs shape.
     *
     * <p>Minecraft uses three model families for stairs:</p>
     * <ul>
     *     <li>{@code straight}</li>
     *     <li>{@code inner}</li>
     *     <li>{@code outer}</li>
     * </ul>
     *
     * @param shape the stairs shape
     * @return the corresponding model family name
     */
    public static String getModelName(
            StairsShape shape
    ) {
        return switch (shape) {
            case STRAIGHT -> "straight";
            case INNER_LEFT, INNER_RIGHT -> "inner";
            case OUTER_LEFT, OUTER_RIGHT -> "outer";
        };
    }
}