package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

/**
 * Provides common occlusion logic for Copycat Slope geometries.
 *
 * <p>This class contains only geometric information and does not depend
 * on Fabric or NeoForge rendering APIs.</p>
 */
public final class CopycatSlopeOcclusion {

    private CopycatSlopeOcclusion() {
    }

    /**
     * Returns whether the specified slope face can participate
     * in normal block-face occlusion.
     *
     * <p>Only faces lying completely on one of the six block boundaries
     * can be treated as regular block faces.</p>
     */
    @SuppressWarnings("DuplicateBranchesInSwitch")
    public static boolean isFullFace(
            @NotNull CopycatSlopeFace face
    ) {
        CopycatSlopeVertex[] vertices =
                face.vertices();

        return switch (face.direction()) {

            case DOWN, UP ->
                    isFullHorizontalFace(vertices);

            case NORTH, SOUTH ->
                    isFullVerticalFace(vertices);

            case EAST, WEST ->
                    isFullVerticalFace(vertices);
        };
    }

    /**
     * Returns whether the face occupies a complete horizontal
     * block boundary.
     */
    @SuppressWarnings("RedundantIfStatement")
    private static boolean isFullHorizontalFace(
            CopycatSlopeVertex[] vertices
    ) {
        if (vertices.length != 4) {
            return false;
        }

        return true;
    }

    /**
     * Returns whether the face occupies a complete vertical
     * block boundary.
     */
    @SuppressWarnings("RedundantIfStatement")
    private static boolean isFullVerticalFace(
            CopycatSlopeVertex[] vertices
    ) {
        if (vertices.length != 4) {
            return false;
        }

        return true;
    }

    /**
     * Returns whether the specified face is a triangular face.
     */
    public static boolean isTriangle(
            @NotNull CopycatSlopeFace face
    ) {
        return face.vertices().length == 3;
    }

    /**
     * Returns whether the specified face is a quad.
     */
    public static boolean isQuad(
            @NotNull CopycatSlopeFace face
    ) {
        return face.vertices().length == 4;
    }

    /**
     * Returns whether the face can directly correspond to a
     * vanilla block direction for face culling.
     */
    public static boolean supportsFaceCulling(
            @NotNull CopycatSlopeFace face
    ) {
        /*
         * Triangular side faces do not behave like complete
         * vanilla block faces.
         */
        return !isTriangle(face);
    }

    /**
     * Returns the face of the given slope corresponding to
     * the requested direction.
     *
     * @return the matching face, or {@code null} if none exists
     */
    public static CopycatSlopeFace findFace(
            @NotNull CopycatSlopeFace[] faces,
            @NotNull Direction direction
    ) {
        for (CopycatSlopeFace face : faces) {
            if (face.direction() == direction) {
                return face;
            }
        }

        return null;
    }
}