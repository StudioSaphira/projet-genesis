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
public final class CopycatGeometrySlope {

    private CopycatGeometrySlope() {
    }

    private static final CopycatSlopeVertex NORTH_WEST =
            new CopycatSlopeVertex(
                    0.0F,
                    0.0F,
                    0.0F
            );

    private static final CopycatSlopeVertex NORTH_EAST =
            new CopycatSlopeVertex(
                    1.0F,
                    0.0F,
                    0.0F
            );

    private static final CopycatSlopeVertex SOUTH_BOTTOM_WEST =
            new CopycatSlopeVertex(
                    0.0F,
                    0.0F,
                    1.0F
            );

    private static final CopycatSlopeVertex SOUTH_BOTTOM_EAST =
            new CopycatSlopeVertex(
                    1.0F,
                    0.0F,
                    1.0F
            );

    private static final CopycatSlopeVertex SOUTH_TOP_WEST =
            new CopycatSlopeVertex(
                    0.0F,
                    1.0F,
                    1.0F
            );

    private static final CopycatSlopeVertex SOUTH_TOP_EAST =
            new CopycatSlopeVertex(
                    1.0F,
                    1.0F,
                    1.0F
            );

    /**
     * Returns the canonical NORTH/BOTTOM geometry.
     */
    public static CopycatSlopeFace[] getDefaultFaces() {
        return new CopycatSlopeFace[] {

                new CopycatSlopeFace(
                        Direction.DOWN,
                        new CopycatSlopeVertex[] {
                                NORTH_WEST,
                                SOUTH_BOTTOM_WEST,
                                SOUTH_BOTTOM_EAST,
                                NORTH_EAST
                        }
                ),

                new CopycatSlopeFace(
                        Direction.UP,
                        new CopycatSlopeVertex[] {
                                NORTH_WEST,
                                NORTH_EAST,
                                SOUTH_TOP_EAST,
                                SOUTH_TOP_WEST
                        }
                ),

                new CopycatSlopeFace(
                        Direction.SOUTH,
                        new CopycatSlopeVertex[] {
                                SOUTH_BOTTOM_WEST,
                                SOUTH_TOP_WEST,
                                SOUTH_TOP_EAST,
                                SOUTH_BOTTOM_EAST
                        }
                ),

                new CopycatSlopeFace(
                        Direction.WEST,
                        new CopycatSlopeVertex[] {
                                NORTH_WEST,
                                SOUTH_BOTTOM_WEST,
                                SOUTH_TOP_WEST
                        }
                ),

                new CopycatSlopeFace(
                        Direction.EAST,
                        new CopycatSlopeVertex[] {
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

        for (int i = 0; i < source.length; i++) {

            CopycatSlopeFace sourceFace =
                    source[i];

            CopycatSlopeVertex[] vertices =
                    new CopycatSlopeVertex[
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
                    new CopycatSlopeFace(
                            direction,
                            vertices
                    );
        }

        return result;
    }

    private static CopycatSlopeVertex transform(
            @NotNull CopycatSlopeVertex vertex,
            @NotNull Direction facing,
            @NotNull Half half
    ) {
        float x = vertex.x();
        float y = vertex.y();
        float z = vertex.z();

        if (half == Half.TOP) {
            y = 1.0F - y;
        }

        float localX = x - 0.5F;
        float localZ = z - 0.5F;

        float rotatedX =
                switch (facing) {
                    case NORTH -> localX;
                    case EAST -> -localZ;
                    case SOUTH -> -localX;
                    case WEST -> localZ;
                    case UP, DOWN -> throw new IllegalArgumentException(
                            "Copycat Slope facing must be horizontal: "
                                    + facing
                    );
                };

        float rotatedZ =
                switch (facing) {
                    case NORTH -> localZ;
                    case EAST -> localX;
                    case SOUTH -> -localZ;
                    case WEST -> -localX;
                    case UP, DOWN -> throw new IllegalArgumentException(
                            "Copycat Slope facing must be horizontal: "
                                    + facing
                    );
                };

        return new CopycatSlopeVertex(
                rotatedX + 0.5F,
                y,
                rotatedZ + 0.5F
        );
    }

    private static Direction transformDirection(
            @NotNull Direction direction,
            @NotNull Direction facing,
            @NotNull Half half
    ) {
        Direction result = direction;

        if (direction.getAxis().isHorizontal()) {

            int rotations =
                    switch (facing) {
                        case NORTH -> 0;
                        case EAST -> 1;
                        case SOUTH -> 2;
                        case WEST -> 3;
                        case UP, DOWN -> throw new IllegalArgumentException(
                                "Copycat Slope facing must be horizontal: "
                                        + facing
                        );
                    };

            for (int i = 0; i < rotations; i++) {
                result = result.getClockWise();
            }
        }

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