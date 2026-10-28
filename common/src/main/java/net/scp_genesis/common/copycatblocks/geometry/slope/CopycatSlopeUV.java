package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.saphyra.common.api.geometry.GeometryFace;
import net.saphyra.common.api.geometry.GeometryUV;
import net.saphyra.common.api.geometry.GeometryVertex;
import net.saphyra.common.api.geometry.UVMapping;

/** Selects nominal-face UV projection for the slope, independently of texture sprites and loaders. */
public final class CopycatSlopeUV {
    private CopycatSlopeUV() {}

    /** Returns one normalized UV coordinate per face vertex, in the original vertex order. */
    public static GeometryUV[] calculate(GeometryFace face) {
        GeometryUV[] result = new GeometryUV[face.vertices().length];
        for (int i = 0; i < result.length; i++) result[i] = calculate(face, face.vertices()[i]);
        return result;
    }

    /** Projects a vertex using the face's nominal texture direction. */
    public static GeometryUV calculate(GeometryFace face, GeometryVertex vertex) {
        return UVMapping.project(face.direction(), vertex);
    }
}
