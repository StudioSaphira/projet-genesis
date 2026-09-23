package net.scp_genesis.common.copycatblocks.geometry;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.util.records.CopycatFace;
import net.scp_genesis.common.copycatblocks.util.records.CopycatVertex;

/** Transforms custom Copycat meshes without depending on a block or rendering API. */
public final class CopycatGeometryTransforms {
    private CopycatGeometryTransforms() {}

    /** Returns clockwise quarter turns from NORTH; rejects vertical directions. */
    public static int rotationCount(Direction facing) {
        return switch (facing) {
            case NORTH -> 0;
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> throw new IllegalArgumentException("Copycat facing must be horizontal: " + facing);
        };
    }

    /**
     * Copies and orients canonical NORTH/BOTTOM faces. Reflection reverses vertex
     * order to preserve outward winding; neither the input faces nor arrays are modified.
     */
    public static CopycatFace[] orient(CopycatFace[] source, Direction facing, Half half) {
        int rotations = rotationCount(facing);
        CopycatFace[] result = new CopycatFace[source.length];
        for (int i = 0; i < source.length; i++) {
            CopycatVertex[] original = source[i].vertices();
            CopycatVertex[] vertices = new CopycatVertex[original.length];
            for (int j = 0; j < original.length; j++) {
                CopycatVertex vertex = original[j];
                if (half == Half.TOP) vertex = CopycatGeometryMath.mirrorY(vertex);
                if (rotations != 0) vertex = CopycatGeometryMath.rotateY(vertex, rotations);
                vertices[half == Half.TOP ? original.length - 1 - j : j] = vertex;
            }
            Direction direction = source[i].direction();
            for (int j = 0; j < rotations && direction.getAxis().isHorizontal(); j++) {
                direction = direction.getClockWise();
            }
            if (half == Half.TOP && direction.getAxis() == Direction.Axis.Y) {
                direction = direction.getOpposite();
            }
            result[i] = new CopycatFace(direction, vertices);
        }
        return result;
    }
}
