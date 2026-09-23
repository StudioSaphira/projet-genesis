package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.minecraft.core.Direction;
import net.scp_genesis.common.copycatblocks.geometry.CopycatFace;
import net.scp_genesis.common.copycatblocks.geometry.CopycatFaceBounds;
import org.jetbrains.annotations.Nullable;

/** Slope-specific culling policy, separated from reusable geometric boundary queries. */
public final class CopycatSlopeOcclusion {
    private CopycatSlopeOcclusion() {}

    /** Retains the existing slope policy: full boundaries except UP participate in culling. */
    public static boolean isFullFace(CopycatFace face) {
        return face.direction() != Direction.UP && CopycatFaceBounds.isFullBoundary(face);
    }

    /** Returns whether the face is triangular. */
    public static boolean isTriangle(CopycatFace face) {
        return face.vertices().length == 3;
    }

    /** Returns whether the face is quadrilateral. */
    public static boolean isQuad(CopycatFace face) {
        return face.vertices().length == 4;
    }

    /** Tests the slope's full-face culling rule, not just membership of a boundary plane. */
    public static boolean supportsFaceCulling(CopycatFace face) {
        return isFullFace(face);
    }

    /** Returns the first face with the requested nominal direction, or null. */
    public static @Nullable CopycatFace findFace(CopycatFace[] faces, Direction direction) {
        for (CopycatFace face : faces) if (face.direction() == direction) return face;
        return null;
    }
}
