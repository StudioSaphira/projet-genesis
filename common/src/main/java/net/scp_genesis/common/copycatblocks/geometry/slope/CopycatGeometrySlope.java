package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.saphyra.common.api.geometry.GeometryTransforms;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Half;
import net.saphyra.common.api.geometry.GeometryFace;
import net.saphyra.common.api.geometry.GeometryUV;
import net.saphyra.common.api.geometry.GeometryVertex;
import net.saphyra.common.api.geometry.UVMapping;
import net.scp_genesis.common.copycatblocks.geometry.*;

/**
 * Triangular prism bounded by Y = 1 - Z in its NORTH/BOTTOM orientation.
 * The north edge is high, the south edge low. Collision is defined separately.
 */
public final class CopycatGeometrySlope {
    private CopycatGeometrySlope() {}

    /** Returns fresh canonical faces, preserving texture directions and vertex winding. */
    public static GeometryFace[] getDefaultFaces() {
        GeometryVertex nw = new GeometryVertex(0, 0, 0);
        GeometryVertex ne = new GeometryVertex(1, 0, 0);
        GeometryVertex sw = new GeometryVertex(0, 0, 1);
        GeometryVertex se = new GeometryVertex(1, 0, 1);
        GeometryVertex topNW = CopycatSlopeMath.cutVerticalEdge(0, 0);
        GeometryVertex topNE = CopycatSlopeMath.cutVerticalEdge(1, 0);
        return new GeometryFace[] {
                new GeometryFace(Direction.DOWN, new GeometryVertex[] {nw, ne, se, sw}),
                new GeometryFace(Direction.NORTH, new GeometryVertex[] {nw, topNW, topNE, ne}),
                new GeometryFace(Direction.SOUTH, new GeometryVertex[] {
                        topNW, CopycatSlopeMath.cutVerticalEdge(0, 1),
                        CopycatSlopeMath.cutVerticalEdge(1, 1), topNE}),
                new GeometryFace(Direction.WEST, new GeometryVertex[] {nw, sw, topNW}),
                new GeometryFace(Direction.EAST, new GeometryVertex[] {ne, topNE, se})
        };
    }

    /** Returns oriented faces with outward winding, including for the mirrored upper half. */
    public static GeometryFace[] getFaces(Direction facing, Half half) {
        return GeometryTransforms.orient(getDefaultFaces(), facing, half);
    }

    /** Returns normalized UVs in exactly the same face and vertex order as {@link #getFaces}. */
    public static GeometryUV[][] getUV(Direction facing, Half half) {
        GeometryFace[] faces = getFaces(facing, half);
        GeometryUV[][] result = new GeometryUV[faces.length][];
        for (int i = 0; i < faces.length; i++) {
            GeometryUV[] uv = CopycatSlopeUV.calculate(faces[i]);
            result[i] = new GeometryUV[uv.length];
            for (int j = 0; j < uv.length; j++) result[i][j] = UVMapping.clamp(uv[j]);
        }
        return result;
    }
}
