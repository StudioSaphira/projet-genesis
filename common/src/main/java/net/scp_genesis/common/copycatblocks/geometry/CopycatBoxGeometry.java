package net.scp_genesis.common.copycatblocks.geometry;

import net.minecraft.core.Direction;
import net.scp_genesis.common.copycatblocks.util.records.CopycatFace;
import net.scp_genesis.common.copycatblocks.util.records.CopycatVertex;
import java.util.Arrays;
import org.jetbrains.annotations.Nullable;

/** Creates outward-wound rectangular faces in normalized block coordinates. */
public final class CopycatBoxGeometry {
    private CopycatBoxGeometry() {}
    /** Builds a box; an optional internal face can be omitted when combining components. */
    public static CopycatFace[] faces(float x1, float y1, float z1, float x2, float y2, float z2,
                                     @Nullable Direction omitted) {
        var a = new CopycatVertex(x1,y1,z1); var b = new CopycatVertex(x2,y1,z1);
        var c = new CopycatVertex(x1,y1,z2); var d = new CopycatVertex(x2,y1,z2);
        var e = new CopycatVertex(x1,y2,z1); var f = new CopycatVertex(x2,y2,z1);
        var g = new CopycatVertex(x1,y2,z2); var h = new CopycatVertex(x2,y2,z2);
        CopycatFace[] faces = {
                new CopycatFace(Direction.DOWN, new CopycatVertex[]{a,b,d,c}),
                new CopycatFace(Direction.UP, new CopycatVertex[]{e,g,h,f}),
                new CopycatFace(Direction.NORTH, new CopycatVertex[]{a,e,f,b}),
                new CopycatFace(Direction.SOUTH, new CopycatVertex[]{c,d,h,g}),
                new CopycatFace(Direction.WEST, new CopycatVertex[]{a,c,g,e}),
                new CopycatFace(Direction.EAST, new CopycatVertex[]{b,f,h,d})
        };
        return Arrays.stream(faces).filter(face -> face.direction() != omitted).toArray(CopycatFace[]::new);
    }
}
