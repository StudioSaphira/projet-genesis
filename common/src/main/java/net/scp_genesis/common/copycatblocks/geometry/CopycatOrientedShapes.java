package net.scp_genesis.common.copycatblocks.geometry;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.VoxelShape;

/** Precomputes the four horizontal orientations and their vertical reflections. */
public final class CopycatOrientedShapes {
    private final VoxelShape[][] shapes = new VoxelShape[2][4];

    /** Builds a cache from a canonical NORTH/BOTTOM shape. */
    public CopycatOrientedShapes(VoxelShape canonical) {
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            int index = CopycatGeometryTransforms.rotationCount(facing);
            shapes[0][index] = CopycatShapeTransforms.rotateHorizontal(canonical, facing);
            shapes[1][index] = CopycatShapeTransforms.mirrorVertical(shapes[0][index]);
        }
    }

    /** Returns a cached shape; vertical facing directions are not supported. */
    public VoxelShape get(Direction facing, Half half) {
        return shapes[half == Half.TOP ? 1 : 0][CopycatGeometryTransforms.rotationCount(facing)];
    }
}
