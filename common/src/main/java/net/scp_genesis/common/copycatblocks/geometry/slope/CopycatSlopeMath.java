package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.scp_genesis.common.copycatblocks.geometry.CopycatGeometryMath;
import net.scp_genesis.common.copycatblocks.geometry.CopycatVertex;
import org.jetbrains.annotations.NotNull;

/**
 * Provides mathematical operations used by Copycat Slope geometry.
 *
 * <p>This class contains only platform-independent geometry
 * calculations. It does not depend on Fabric, NeoForge, or any
 * rendering API.</p>
 *
 * <p>All positions use normalized block-local coordinates in the
 * range {@code 0.0F} to {@code 1.0F}.</p>
 */
public final class CopycatSlopeMath {

    private CopycatSlopeMath() {}

    /**
     * Calculates the Y coordinate of the diagonal cutting plane.
     *
     * <p>The canonical Slope uses the plane:</p>
     *
     * <pre>
     * Y = 1 - Z
     * </pre>
     *
     * @param z normalized Z coordinate
     * @return the corresponding Y coordinate
     */
    public static float diagonalHeight(
            float z
    ) {
        return 1.0F - z;
    }

    /**
     * Creates a point located on the canonical diagonal plane.
     *
     * <p>The plane is defined by:</p>
     *
     * <pre>
     * Y = 1 - Z
     * </pre>
     *
     * <p>The X coordinate is unaffected by the diagonal.</p>
     *
     * @param x normalized X coordinate
     * @param z normalized Z coordinate
     * @return the point on the diagonal plane
     */
    public static @NotNull CopycatVertex diagonalPoint(
            float x,
            float z
    ) {
        return new CopycatVertex(
                x,
                diagonalHeight(z),
                z
        );
    }

    /**
     * Intersects a vertical edge with the canonical diagonal plane.
     *
     * <p>The edge must have a constant X and Z coordinate while
     * extending along the Y axis.</p>
     *
     * <p>The resulting point is:</p>
     *
     * <pre>
     * X' = X
     * Y' = 1 - Z
     * Z' = Z
     * </pre>
     *
     * @param x normalized X coordinate of the edge
     * @param z normalized Z coordinate of the edge
     * @return the intersection point
     */
    public static @NotNull CopycatVertex cutVerticalEdge(
            float x,
            float z
    ) {
        return diagonalPoint(
                x,
                z
        );
    }

    /**
     * Interpolates a point along an edge according to the diagonal
     * cutting plane.
     *
     * <p>This is useful when the original cube edge is represented by
     * two vertices and the exact intersection with the diagonal needs
     * to be calculated.</p>
     *
     * @param bottom the lower endpoint of the edge
     * @param top the upper endpoint of the edge
     * @return the intersection point with the diagonal plane
     */
    public static @NotNull CopycatVertex cutVerticalEdge(
            @NotNull CopycatVertex bottom,
            @NotNull CopycatVertex top
    ) {
        float targetY =
                diagonalHeight(
                        bottom.z()
                );

        float dy =
                top.y() - bottom.y();

        if (Math.abs(dy) < 1.0E-6F) {
            return bottom;
        }

        float t =
                (targetY - bottom.y())
                        / dy;

        return CopycatGeometryMath.lerp(
                bottom,
                top,
                t
        );
    }
}