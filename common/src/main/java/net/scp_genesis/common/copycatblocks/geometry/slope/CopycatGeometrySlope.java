package net.scp_genesis.common.copycatblocks.geometry.slope;

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
 * <p>The canonical geometry is a right triangular prism composed of:
 *
 * <ul>
 *     <li>one bottom square</li>
 *     <li>one inclined upper face</li>
 *     <li>one south vertical square</li>
 *     <li>one west right triangle</li>
 *     <li>one east right triangle</li>
 * </ul>
 */
public final class CopycatGeometrySlope {

    private CopycatGeometrySlope() {
    }

    /*
     * ================================================================
     * CANONICAL VERTICES
     * ================================================================
     */

    private static final CopycatSlopeVertex NORTH_BOTTOM_WEST =
            new CopycatSlopeVertex(
                    0.0F,
                    0.0F,
                    0.0F
            );

    private static final CopycatSlopeVertex NORTH_TOP_WEST =
            new CopycatSlopeVertex(
                    0.0F,
                    1.0F,
                    0.0F
            );

    private static final CopycatSlopeVertex NORTH_BOTTOM_EAST =
            new CopycatSlopeVertex(
                    1.0F,
                    0.0F,
                    0.0F
            );

    private static final CopycatSlopeVertex NORTH_TOP_EAST =
            new CopycatSlopeVertex(
                    1.0F,
                    1.0F,
                    0.0F
            );

    private static final CopycatSlopeVertex SOUTH_EAST =
            new CopycatSlopeVertex(
                    1.0F,
                    0.0F,
                    1.0F
            );

    private static final CopycatSlopeVertex SOUTH_WEST =
            new CopycatSlopeVertex(
                    0.0F,
                    0.0F,
                    1.0F
            );

    /*
     * ================================================================
     * DEFAULT GEOMETRY
     * ================================================================
     */

    /**
     * Returns the canonical NORTH/BOTTOM geometry.
     *
     * <p>The vertex winding is defined so that the face orientation
     * remains consistent with the corresponding nominal direction.</p>
     */
    public static CopycatSlopeFace[] getDefaultFaces() {
        return new CopycatSlopeFace[] {

                /*
                 * ----------------------------------------------------
                 * DOWN
                 * ----------------------------------------------------
                 */

                new CopycatSlopeFace(
                        Direction.DOWN,
                        new CopycatSlopeVertex[] {
                                NORTH_BOTTOM_WEST,
                                NORTH_BOTTOM_EAST,
                                SOUTH_EAST,
                                SOUTH_WEST
                        }
                ),

                /*
                 * ----------------------------------------------------
                 * INCLINED FACE
                 * ----------------------------------------------------
                 *
                 * Nominally represented by Direction.UP.
                 *
                 * This is NOT a geometrically horizontal UP face.
                 */

                new CopycatSlopeFace(
                        Direction.UP,
                        new CopycatSlopeVertex[] {
                                NORTH_TOP_WEST,
                                NORTH_TOP_EAST,
                                SOUTH_EAST,
                                SOUTH_WEST
                        }
                ),

                /*
                 * ----------------------------------------------------
                 * SOUTH
                 * ----------------------------------------------------
                 */

                new CopycatSlopeFace(
                        Direction.NORTH,
                        new CopycatSlopeVertex[] {
                                NORTH_BOTTOM_WEST,
                                NORTH_BOTTOM_EAST,
                                NORTH_TOP_EAST,
                                NORTH_TOP_WEST
                        }
                ),

                /*
                 * ----------------------------------------------------
                 * WEST TRIANGLE
                 * ----------------------------------------------------
                 */

                new CopycatSlopeFace(
                        Direction.WEST,
                        new CopycatSlopeVertex[] {
                                NORTH_TOP_WEST,
                                SOUTH_WEST,
                                NORTH_BOTTOM_WEST
                        }
                ),

                /*
                 * ----------------------------------------------------
                 * EAST TRIANGLE
                 * ----------------------------------------------------
                 */

                new CopycatSlopeFace(
                        Direction.EAST,
                        new CopycatSlopeVertex[] {
                                NORTH_TOP_EAST,
                                SOUTH_EAST,
                                NORTH_BOTTOM_EAST
                        }
                )
        };
    }

    /*
     * ================================================================
     * ORIENTATION
     * ================================================================
     */

    /**
     * Returns the geometry for the requested orientation.
     *
     * @param facing horizontal orientation of the slope
     * @param half vertical orientation of the slope
     * @return the transformed slope faces
     */
    public static CopycatSlopeFace[] getFaces(
            @NotNull Direction facing,
            @NotNull Half half
    ) {
        CopycatSlopeFace[] source =
                getDefaultFaces();

        CopycatSlopeFace[] result =
                new CopycatSlopeFace[source.length];

        int rotations =
                getRotationCount(facing);

        for (int i = 0; i < source.length; i++) {

            CopycatSlopeFace sourceFace =
                    source[i];

            CopycatSlopeVertex[] vertices =
                    new CopycatSlopeVertex[
                            sourceFace.vertices().length
                            ];

            for (int j = 0; j < vertices.length; j++) {

                vertices[j] =
                        transformVertex(
                                sourceFace.vertices()[j],
                                rotations,
                                half
                        );
            }

            /*
             * The vertical reflection used for HALF.TOP reverses
             * the winding of every face.
             */
            if (half == Half.TOP) {
                reverseWinding(vertices);
            }

            Direction direction =
                    transformDirection(
                            sourceFace.direction(),
                            rotations,
                            half
                    );

            result[i] =
                    new CopycatSlopeFace(
                            direction,
                            vertices
                    );
        }

        return result;
    }

    /**
     * Returns the number of clockwise 90° rotations required to
     * transform the canonical NORTH orientation into the requested
     * horizontal facing.
     */
    private static int getRotationCount(
            @NotNull Direction facing
    ) {
        return switch (facing) {
            case NORTH -> 0;
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;

            case UP, DOWN ->
                    throw new IllegalArgumentException(
                            "Copycat Slope facing must be horizontal: "
                                    + facing
                    );
        };
    }

    /**
     * Transforms a canonical vertex according to FACING and HALF.
     */
    private static CopycatSlopeVertex transformVertex(
            @NotNull CopycatSlopeVertex vertex,
            int rotations,
            @NotNull Half half
    ) {
        float x = vertex.x();
        float y = vertex.y();
        float z = vertex.z();

        /*
         * HALF.TOP is the vertical mirror of HALF.BOTTOM.
         */
        if (half == Half.TOP) {
            y = 1.0F - y;
        }

        /*
         * Rotate around the center of the block.
         */
        float localX =
                x - 0.5F;

        float localZ =
                z - 0.5F;

        float rotatedX =
                localX;

        float rotatedZ =
                localZ;

        for (int i = 0; i < rotations; i++) {

            float previousX =
                    rotatedX;

            rotatedX =
                    -rotatedZ;

            rotatedZ =
                    previousX;
        }

        return new CopycatSlopeVertex(
                rotatedX + 0.5F,
                y,
                rotatedZ + 0.5F
        );
    }

    /**
     * Transforms a face direction according to FACING and HALF.
     */
    private static Direction transformDirection(
            @NotNull Direction direction,
            int rotations,
            @NotNull Half half
    ) {
        Direction result =
                direction;

        /*
         * Rotate horizontal directions.
         */
        for (int i = 0; i < rotations; i++) {

            if (result.getAxis().isHorizontal()) {
                result =
                        result.getClockWise();
            }
        }

        /*
         * A vertical reflection swaps UP and DOWN.
         *
         * Horizontal directions remain unchanged by the reflection.
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

    /**
     * Reverses the winding of a face.
     *
     * <p>Mirroring a geometry along the Y axis reverses its orientation.
     * Reversing the vertex order restores the expected winding.</p>
     */
    private static void reverseWinding(
            @NotNull CopycatSlopeVertex[] vertices
    ) {
        for (
                int first = 0,
                last = vertices.length - 1;
                first < last;
                first++,
                        last--
        ) {
            CopycatSlopeVertex temporary =
                    vertices[first];

            vertices[first] =
                    vertices[last];

            vertices[last] =
                    temporary;
        }
    }
}