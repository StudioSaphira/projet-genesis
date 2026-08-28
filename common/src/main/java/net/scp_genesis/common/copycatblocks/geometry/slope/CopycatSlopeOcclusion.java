package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.minecraft.core.Direction;
import net.scp_genesis.common.copycatblocks.geometry.CopycatFace;
import net.scp_genesis.common.copycatblocks.geometry.CopycatVertex;
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
            @NotNull CopycatFace face
    ) {
        if (!isQuad(face)) {
            return false;
        }

        CopycatVertex[] vertices =
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
            @NotNull CopycatVertex[] vertices
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
            @NotNull CopycatVertex[] vertices
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
            @NotNull CopycatVertex[] vertices
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
            @NotNull CopycatVertex[] vertices
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
            @NotNull CopycatVertex[] vertices
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
            @NotNull CopycatFace face
    ) {
        return face.vertices().length == 3;
    }

    /**
     * Returns whether the specified face is a quad.
     */
    public static boolean isQuad(
            @NotNull CopycatFace face
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
            @NotNull CopycatFace face
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
    public static CopycatFace findFace(
            @NotNull CopycatFace[] faces,
            @NotNull Direction direction
    ) {
        for (CopycatFace face : faces) {

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
            @NotNull CopycatVertex[] vertices,
            float value
    ) {
        for (CopycatVertex vertex : vertices) {

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
            @NotNull CopycatVertex[] vertices,
            float value
    ) {
        for (CopycatVertex vertex : vertices) {

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
            @NotNull CopycatVertex[] vertices,
            float value
    ) {
        for (CopycatVertex vertex : vertices) {

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
            @NotNull CopycatVertex[] vertices
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
            @NotNull CopycatVertex[] vertices
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
            @NotNull CopycatVertex[] vertices
    ) {
        return containsYZ(vertices, 0.0F, 0.0F)
                && containsYZ(vertices, 1.0F, 0.0F)
                && containsYZ(vertices, 1.0F, 1.0F)
                && containsYZ(vertices, 0.0F, 1.0F);
    }

    private static boolean containsXZ(
            @NotNull CopycatVertex[] vertices,
            float x,
            float z
    ) {
        for (CopycatVertex vertex : vertices) {

            if (equals(vertex.x(), x)
                    && equals(vertex.z(), z)) {
                return true;
            }
        }

        return false;
    }

    private static boolean containsXY(
            @NotNull CopycatVertex[] vertices,
            float x,
            float y
    ) {
        for (CopycatVertex vertex : vertices) {

            if (equals(vertex.x(), x)
                    && equals(vertex.y(), y)) {
                return true;
            }
        }

        return false;
    }

    private static boolean containsYZ(
            @NotNull CopycatVertex[] vertices,
            float y,
            float z
    ) {
        for (CopycatVertex vertex : vertices) {

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