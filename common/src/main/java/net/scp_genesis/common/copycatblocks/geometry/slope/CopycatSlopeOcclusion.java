package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.minecraft.core.Direction;
import net.saphyra.common.api.geometry.FaceBounds;
import net.saphyra.common.api.geometry.GeometryFace;
import org.jetbrains.annotations.Nullable;

/** Slope-specific culling policy, separated from reusable geometric boundary queries. */
public final class CopycatSlopeOcclusion {
    private CopycatSlopeOcclusion() {}

    /** Retains the existing slope policy: full boundaries except UP participate in culling. */
    public static boolean isFullFace(GeometryFace face) {
        return face.direction() != Direction.UP && FaceBounds.isFullBoundary(face);
    }

    /** Returns whether the face is triangular. */
    public static boolean isTriangle(GeometryFace face) {
        return face.vertices().length == 3;
    }

    /** Returns whether the face is quadrilateral. */
    public static boolean isQuad(GeometryFace face) {
        return face.vertices().length == 4;
    }

    /** Tests the slope's full-face culling rule, not just membership of a boundary plane. */
    public static boolean supportsFaceCulling(GeometryFace face) {
        return isFullFace(face);
    }

    /** Returns the first face with the requested nominal direction, or null. */
    public static @Nullable GeometryFace findFace(GeometryFace[] faces, Direction direction) {
        for (GeometryFace face : faces) if (face.direction() == direction) return face;
        return null;
    }
}
