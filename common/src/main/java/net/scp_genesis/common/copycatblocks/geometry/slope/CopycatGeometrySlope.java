package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.geometry.*;

/**
 * Triangular prism bounded by Y = 1 - Z in its NORTH/BOTTOM orientation.
 * The north edge is high, the south edge low. Collision is defined separately.
 */
public final class CopycatGeometrySlope {
    private CopycatGeometrySlope() {}

    /** Returns fresh canonical faces, preserving texture directions and vertex winding. */
    public static CopycatFace[] getDefaultFaces() {
        CopycatVertex nw = new CopycatVertex(0, 0, 0);
        CopycatVertex ne = new CopycatVertex(1, 0, 0);
        CopycatVertex sw = new CopycatVertex(0, 0, 1);
        CopycatVertex se = new CopycatVertex(1, 0, 1);
        CopycatVertex topNW = CopycatSlopeMath.cutVerticalEdge(0, 0);
        CopycatVertex topNE = CopycatSlopeMath.cutVerticalEdge(1, 0);
        return new CopycatFace[] {
                new CopycatFace(Direction.DOWN, new CopycatVertex[] {nw, ne, se, sw}),
                new CopycatFace(Direction.NORTH, new CopycatVertex[] {nw, topNW, topNE, ne}),
                new CopycatFace(Direction.SOUTH, new CopycatVertex[] {
                        topNW, CopycatSlopeMath.cutVerticalEdge(0, 1),
                        CopycatSlopeMath.cutVerticalEdge(1, 1), topNE}),
                new CopycatFace(Direction.WEST, new CopycatVertex[] {nw, sw, topNW}),
                new CopycatFace(Direction.EAST, new CopycatVertex[] {ne, topNE, se})
        };
    }

    /** Returns oriented faces with outward winding, including for the mirrored upper half. */
    public static CopycatFace[] getFaces(Direction facing, Half half) {
        return CopycatGeometryTransforms.orient(getDefaultFaces(), facing, half);
    }

    /** Returns normalized UVs in exactly the same face and vertex order as {@link #getFaces}. */
    public static CopycatUV[][] getUV(Direction facing, Half half) {
        CopycatFace[] faces = getFaces(facing, half);
        CopycatUV[][] result = new CopycatUV[faces.length][];
        for (int i = 0; i < faces.length; i++) {
            CopycatUV[] uv = CopycatSlopeUV.calculate(faces[i]);
            result[i] = new CopycatUV[uv.length];
            for (int j = 0; j < uv.length; j++) result[i][j] = CopycatUVMapping.clamp(uv[j]);
        }
        return result;
    }
}
