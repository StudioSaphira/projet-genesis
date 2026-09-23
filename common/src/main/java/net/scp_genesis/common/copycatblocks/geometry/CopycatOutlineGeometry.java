package net.scp_genesis.common.copycatblocks.geometry;

import net.scp_genesis.common.copycatblocks.util.records.CopycatFace;
import net.scp_genesis.common.copycatblocks.util.records.CopycatOutlineLine;
import net.scp_genesis.common.copycatblocks.util.records.CopycatVertex;

import java.util.ArrayList;
import java.util.List;

/** Extracts polygon edges independently of Fabric, NeoForge and client rendering classes. */
public final class CopycatOutlineGeometry {
    private static final float EPSILON = 1.0E-5F;
    private CopycatOutlineGeometry() {}

    /**
     * Extracts nonzero edges in face order. Shared edges are deliberately preserved:
     * removing edges between faces with different normals would erase silhouette edges.
     */
    public static List<CopycatOutlineLine> lines(CopycatFace[] faces) {
        List<CopycatOutlineLine> lines = new ArrayList<>();
        for (CopycatFace face : faces) {
            CopycatVertex[] vertices = face.vertices();
            if (vertices.length < 3) continue;
            for (int i = 0; i < vertices.length; i++) {
                CopycatVertex start = vertices[i];
                CopycatVertex end = vertices[(i + 1) % vertices.length];
                if (Math.abs(start.x() - end.x()) < EPSILON
                        && Math.abs(start.y() - end.y()) < EPSILON
                        && Math.abs(start.z() - end.z()) < EPSILON) continue;
                lines.add(new CopycatOutlineLine(start, end, face.direction()));
            }
        }
        return lines;
    }
}
