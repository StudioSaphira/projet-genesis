package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.scp_genesis.common.copycatblocks.util.records.CopycatFace;
import net.scp_genesis.common.copycatblocks.util.records.CopycatUV;
import net.scp_genesis.common.copycatblocks.geometry.CopycatUVMapping;
import net.scp_genesis.common.copycatblocks.util.records.CopycatVertex;

/** Selects nominal-face UV projection for the slope, independently of texture sprites and loaders. */
public final class CopycatSlopeUV {
    private CopycatSlopeUV() {}

    /** Returns one normalized UV coordinate per face vertex, in the original vertex order. */
    public static CopycatUV[] calculate(CopycatFace face) {
        CopycatUV[] result = new CopycatUV[face.vertices().length];
        for (int i = 0; i < result.length; i++) result[i] = calculate(face, face.vertices()[i]);
        return result;
    }

    /** Projects a vertex using the face's nominal texture direction. */
    public static CopycatUV calculate(CopycatFace face, CopycatVertex vertex) {
        return CopycatUVMapping.project(face.direction(), vertex);
    }
}
