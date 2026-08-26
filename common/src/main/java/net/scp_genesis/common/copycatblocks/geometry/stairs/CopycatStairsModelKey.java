package net.scp_genesis.common.copycatblocks.geometry.stairs;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

/**
 * Identifies a Copycat Stairs geometry model.
 *
 * @param facing the horizontal facing direction
 * @param half the vertical half occupied by the stairs
 * @param shape the stairs shape
 */
public record CopycatStairsModelKey(
        Direction facing,
        Half half,
        StairsShape shape
) {
}