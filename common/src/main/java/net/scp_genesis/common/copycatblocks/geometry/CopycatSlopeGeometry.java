package net.scp_genesis.common.copycatblocks.geometry;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Half;
import org.jetbrains.annotations.NotNull;

/**
 * Defines the geometry of a Copycat Slope.
 *
 * <p>The canonical orientation is:
 *
 * <ul>
 *     <li>FACING = NORTH</li>
 *     <li>HALF = BOTTOM</li>
 *     <li>NORTH = low side of the slope</li>
 *     <li>SOUTH = high side of the slope</li>
 * </ul>
 *
 * <p>The geometry is a right triangular prism composed of:
 *
 * <ul>
 *     <li>one bottom square</li>
 *     <li>one inclined upper face</li>
 *     <li>one south square</li>
 *     <li>one west right triangle</li>
 *     <li>one east right triangle</li>
 * </ul>
 */
public final class CopycatSlopeGeometry {

    private CopycatSlopeGeometry() {
    }

    /**
     * A vertex expressed in block-local coordinates.
     */
    public record Vertex(
            float x,
            float y,
            float z
    ) {
    }

    /**
     * A face of the slope geometry.
     */
    public record Face(
            @NotNull Direction direction,
            @NotNull Vertex[] vertices
    ) {
    }

    /*
     * ============================================================
     * CANONICAL GEOMETRY
     * ============================================================
     *
     * NORTH = low
     * SOUTH = high
     *
     *              SOUTH
     *          ┌──────────┐
     *          │          │
     *          │          │
     *          └──────────┘
     *         ╱
     *        ╱
     *       ╱
     *      ╱
     * NORTH
     *
     * The slope rises from NORTH -> SOUTH.
     */

    private static final Vertex NORTH_WEST =
            new Vertex(0.0F, 0.0F, 0.0F);

    private static final Vertex NORTH_EAST =
            new Vertex(1.0F, 0.0F, 0.0F);

    private static final Vertex SOUTH_BOTTOM_WEST =
            new Vertex(0.0F, 0.0F, 1.0F);

    private static final Vertex SOUTH_BOTTOM_EAST =
            new Vertex(1.0F, 0.0F, 1.0F);

    private static final Vertex SOUTH_TOP_WEST =
            new Vertex(0.0F, 1.0F, 1.0F);

    private static final Vertex SOUTH_TOP_EAST =
            new Vertex(1.0F, 1.0F, 1.0F);

    /**
     * Returns the canonical NORTH/BOTTOM geometry.
     */
    public static Face[] getDefaultFaces() {

        return new Face[] {

                /*
                 * ====================================================
                 * DOWN
                 * ====================================================
                 *
                 * Full square.
                 */

                new Face(
                        Direction.DOWN,
                        new Vertex[] {
                                NORTH_WEST,
                                SOUTH_BOTTOM_WEST,
                                SOUTH_BOTTOM_EAST,
                                NORTH_EAST
                        }
                ),

                /*
                 * ====================================================
                 * UP
                 * ====================================================
                 *
                 * Inclined surface.
                 */

                new Face(
                        Direction.UP,
                        new Vertex[] {
                                NORTH_WEST,
                                NORTH_EAST,
                                SOUTH_TOP_EAST,
                                SOUTH_TOP_WEST
                        }
                ),

                /*
                 * ====================================================
                 * SOUTH
                 * ====================================================
                 *
                 * Full square.
                 */

                new Face(
                        Direction.SOUTH,
                        new Vertex[] {
                                SOUTH_BOTTOM_WEST,
                                SOUTH_TOP_WEST,
                                SOUTH_TOP_EAST,
                                SOUTH_BOTTOM_EAST
                        }
                ),

                /*
                 * ====================================================
                 * WEST
                 * ====================================================
                 *
                 * Right triangle.
                 */

                new Face(
                        Direction.WEST,
                        new Vertex[] {
                                NORTH_WEST,
                                SOUTH_BOTTOM_WEST,
                                SOUTH_TOP_WEST
                        }
                ),

                /*
                 * ====================================================
                 * EAST
                 * ====================================================
                 *
                 * Right triangle.
                 */

                new Face(
                        Direction.EAST,
                        new Vertex[] {
                                NORTH_EAST,
                                SOUTH_TOP_EAST,
                                SOUTH_BOTTOM_EAST
                        }
                )
        };
    }

    /**
     * Returns the geometry for the requested orientation.
     *
     * @param facing horizontal orientation of the slope
     * @param half vertical orientation of the slope
     */
    public static Face[] getFaces(
            @NotNull Direction facing,
            @NotNull Half half
    ) {
        Face[] source = getDefaultFaces();

        Face[] result =
                new Face[source.length];

        for (int i = 0; i < source.length; i++) {

            Face sourceFace =
                    source[i];

            Vertex[] vertices =
                    new Vertex[
                            sourceFace.vertices().length
                            ];

            for (int j = 0; j < vertices.length; j++) {

                vertices[j] =
                        transform(
                                sourceFace.vertices()[j],
                                facing,
                                half
                        );
            }

            Direction direction =
                    transformDirection(
                            sourceFace.direction(),
                            facing,
                            half
                    );

            result[i] =
                    new Face(
                            direction,
                            vertices
                    );
        }

        return result;
    }

    /**
     * Rotates and vertically mirrors a vertex.
     */
    private static Vertex transform(
            @NotNull Vertex vertex,
            @NotNull Direction facing,
            @NotNull Half half
    ) {
        float x = vertex.x();
        float y = vertex.y();
        float z = vertex.z();

        /*
         * ------------------------------------------------------------
         * VERTICAL MIRROR
         * ------------------------------------------------------------
         */

        if (half == Half.TOP) {
            y = 1.0F - y;
        }

        /*
         * ------------------------------------------------------------
         * HORIZONTAL ROTATION
         * ------------------------------------------------------------
         *
         * Canonical direction:
         *
         *     NORTH -> SOUTH
         */

        float localX =
                x - 0.5F;

        float localZ =
                z - 0.5F;

        float rotatedX =
                switch (facing) {
                    case NORTH -> localX;
                    case EAST -> -localZ;
                    case SOUTH -> -localX;
                    case WEST -> localZ;
                    case UP, DOWN -> throw new IllegalArgumentException(
                            "Copycat Slope facing must be horizontal: " + facing
                    );
                };

        float rotatedZ =
                switch (facing) {
                    case NORTH -> localZ;
                    case EAST -> localX;
                    case SOUTH -> -localZ;
                    case WEST -> -localX;
                    case UP, DOWN -> throw new IllegalArgumentException(
                            "Copycat Slope facing must be horizontal: " + facing
                    );
                };

        return new Vertex(
                rotatedX + 0.5F,
                y,
                rotatedZ + 0.5F
        );
    }

    /**
     * Rotates the direction of a face.
     */
    private static Direction transformDirection(
            @NotNull Direction direction,
            @NotNull Direction facing,
            @NotNull Half half
    ) {
        Direction result =
                direction;

        /*
         * Horizontal rotation.
         */

        if (direction.getAxis().isHorizontal()) {

            int rotations =
                    switch (facing) {
                        case NORTH -> 0;
                        case EAST -> 1;
                        case SOUTH -> 2;
                        case WEST -> 3;
                        case UP, DOWN -> throw new IllegalArgumentException(
                                "Copycat Slope facing must be horizontal: " + facing
                        );
                    };

            for (int i = 0; i < rotations; i++) {
                result =
                        result.getClockWise();
            }
        }

        /*
         * Vertical inversion.
         */

        if (half == Half.TOP) {

            if (result == Direction.UP) {
                result = Direction.DOWN;

            } else if (result == Direction.DOWN) {
                result = Direction.UP;
            }
        }

        return result;
    }
}