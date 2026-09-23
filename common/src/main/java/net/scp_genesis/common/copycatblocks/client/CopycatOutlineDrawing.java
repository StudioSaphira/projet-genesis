package net.scp_genesis.common.copycatblocks.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.scp_genesis.common.copycatblocks.geometry.CopycatOutlineLine;
import net.scp_genesis.common.copycatblocks.geometry.CopycatVector;
import net.scp_genesis.common.copycatblocks.geometry.CopycatVertex;
import java.util.List;

/** Client-only outline drawing using vanilla APIs; no loader-specific dependencies. */
public final class CopycatOutlineDrawing {
    private CopycatOutlineDrawing() {}

    /** Draws translucent black edges with an optional camera-relative translation. */
    public static void draw(PoseStack.Pose pose, VertexConsumer buffer,
                            List<CopycatOutlineLine> lines, float x, float y, float z) {
        for (CopycatOutlineLine line : lines) {
            CopycatVector normal = line.normal();
            vertex(pose, buffer, line.start(), normal, x, y, z);
            vertex(pose, buffer, line.end(), normal, x, y, z);
        }
    }

    private static void vertex(PoseStack.Pose pose, VertexConsumer buffer, CopycatVertex vertex,
                               CopycatVector normal, float x, float y, float z) {
        buffer.addVertex(pose, x + vertex.x(), y + vertex.y(), z + vertex.z())
                .setColor(0, 0, 0, 102).setNormal(pose, normal.x(), normal.y(), normal.z());
    }
}
