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

    private static final float EPSILON = 0.00001F;

    private CopycatSlopeOcclusion() {
    }

    /**
     * Returns whether the specified face is a complete block boundary
     * face.
     *
     * <p>A face is considered a full face only when:</p>
     *
     * <ul>
     *     <li>it is a quad;</li>
     *     <li>all its vertices lie on the corresponding block boundary;</li>
     *     <li>the face covers the complete 1x1 block boundary.</li>
     * </ul>
     *
     * <p>The inclined face is deliberately not considered a full face,
     * even though it is represented by four vertices.</p>
     */
    public static boolean isFullFace(
            @NotNull CopycatSlopeFace face
    ) {
        if (!isQuad(face)) {
            return false;
        }

        CopycatSlopeVertex[] vertices =
                face.vertices();

        return switch (face.direction()) {

            case DOWN ->
                    isFullDownFace(vertices);

            case UP ->
                    false;

            case NORTH ->
                    isFullNorthFace(vertices);

            case SOUTH ->
                    isFullSouthFace(vertices);

            case WEST ->
                    isFullWestFace(vertices);

            case EAST ->
                    isFullEastFace(vertices);
        };
    }

    /**
     * Returns whether the face is a complete DOWN boundary.
     */
    private static boolean isFullDownFace(
            @NotNull CopycatSlopeVertex[] vertices
    ) {
        if (!allHaveY(vertices, 0.0F)) {
            return false;
        }

        return coversFullXZBoundary(vertices);
    }

    /**
     * Returns whether the face is a complete NORTH boundary.
     */
    private static boolean isFullNorthFace(
            @NotNull CopycatSlopeVertex[] vertices
    ) {
        if (!allHaveZ(vertices, 0.0F)) {
            return false;
        }

        return coversFullXYBoundary(vertices);
    }

    /**
     * Returns whether the face is a complete SOUTH boundary.
     */
    private static boolean isFullSouthFace(
            @NotNull CopycatSlopeVertex[] vertices
    ) {
        if (!allHaveZ(vertices, 1.0F)) {
            return false;
        }

        return coversFullXYBoundary(vertices);
    }

    /**
     * Returns whether the face is a complete WEST boundary.
     */
    private static boolean isFullWestFace(
            @NotNull CopycatSlopeVertex[] vertices
    ) {
        if (!allHaveX(vertices, 0.0F)) {
            return false;
        }

        return coversFullYZBoundary(vertices);
    }

    /**
     * Returns whether the face is a complete EAST boundary.
     */
    private static boolean isFullEastFace(
            @NotNull CopycatSlopeVertex[] vertices
    ) {
        if (!allHaveX(vertices, 1.0F)) {
            return false;
        }

        return coversFullYZBoundary(vertices);
    }

    /*
     * ================================================================
     * FACE TYPE
     * ================================================================
     */

    /**
     * Returns whether the specified face is triangular.
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
     * Returns whether the specified face can participate in normal
     * block-face culling.
     *
     * <p>Only genuine complete block-boundary faces are eligible.</p>
     */
    public static boolean supportsFaceCulling(
            @NotNull CopycatSlopeFace face
    ) {
        return isFullFace(face);
    }

    /*
     * ================================================================
     * FACE LOOKUP
     * ================================================================
     */

    /**
     * Returns the face of the given slope corresponding to the
     * requested direction.
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

    /*
     * ================================================================
     * COORDINATE VALIDATION
     * ================================================================
     */

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean allHaveX(
            @NotNull CopycatSlopeVertex[] vertices,
            float value
    ) {
        for (CopycatSlopeVertex vertex : vertices) {

            if (!equals(
                    vertex.x(),
                    value
            )) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("SameParameterValue")
    private static boolean allHaveY(
            @NotNull CopycatSlopeVertex[] vertices,
            float value
    ) {
        for (CopycatSlopeVertex vertex : vertices) {

            if (!equals(
                    vertex.y(),
                    value
            )) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private static boolean allHaveZ(
            @NotNull CopycatSlopeVertex[] vertices,
            float value
    ) {
        for (CopycatSlopeVertex vertex : vertices) {

            if (!equals(
                    vertex.z(),
                    value
            )) {
                return false;
            }
        }

        return true;
    }

    /*
     * ================================================================
     * FULL BOUNDARY VALIDATION
     * ================================================================
     */

    /**
     * Checks that a horizontal X/Z face contains all four block corners.
     */
    private static boolean coversFullXZBoundary(
            @NotNull CopycatSlopeVertex[] vertices
    ) {
        return containsXZ(vertices, 0.0F, 0.0F)
                && containsXZ(vertices, 1.0F, 0.0F)
                && containsXZ(vertices, 1.0F, 1.0F)
                && containsXZ(vertices, 0.0F, 1.0F);
    }

    /**
     * Checks that a vertical X/Y face contains all four block corners.
     */
    private static boolean coversFullXYBoundary(
            @NotNull CopycatSlopeVertex[] vertices
    ) {
        return containsXY(vertices, 0.0F, 0.0F)
                && containsXY(vertices, 1.0F, 0.0F)
                && containsXY(vertices, 1.0F, 1.0F)
                && containsXY(vertices, 0.0F, 1.0F);
    }

    /**
     * Checks that a vertical Y/Z face contains all four block corners.
     */
    private static boolean coversFullYZBoundary(
            @NotNull CopycatSlopeVertex[] vertices
    ) {
        return containsYZ(vertices, 0.0F, 0.0F)
                && containsYZ(vertices, 1.0F, 0.0F)
                && containsYZ(vertices, 1.0F, 1.0F)
                && containsYZ(vertices, 0.0F, 1.0F);
    }

    private static boolean containsXZ(
            @NotNull CopycatSlopeVertex[] vertices,
            float x,
            float z
    ) {
        for (CopycatSlopeVertex vertex : vertices) {

            if (equals(vertex.x(), x)
                    && equals(vertex.z(), z)) {
                return true;
            }
        }

        return false;
    }

    private static boolean containsXY(
            @NotNull CopycatSlopeVertex[] vertices,
            float x,
            float y
    ) {
        for (CopycatSlopeVertex vertex : vertices) {

            if (equals(vertex.x(), x)
                    && equals(vertex.y(), y)) {
                return true;
            }
        }

        return false;
    }

    private static boolean containsYZ(
            @NotNull CopycatSlopeVertex[] vertices,
            float y,
            float z
    ) {
        for (CopycatSlopeVertex vertex : vertices) {

            if (equals(vertex.y(), y)
                    && equals(vertex.z(), z)) {
                return true;
            }
        }

        return false;
    }

    private static boolean equals(
            float first,
            float second
    ) {
        return Math.abs(first - second) <= EPSILON;
    }
}