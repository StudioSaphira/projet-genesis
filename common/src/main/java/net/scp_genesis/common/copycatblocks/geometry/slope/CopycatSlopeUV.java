package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.scp_genesis.common.copycatblocks.geometry.CopycatFace;
import net.scp_genesis.common.copycatblocks.geometry.CopycatUV;
import net.scp_genesis.common.copycatblocks.geometry.CopycatVertex;
import org.jetbrains.annotations.NotNull;

/**
 * Provides UV coordinate calculations for Copycat Slope faces.
 *
 * <p>This class contains only platform-independent UV calculations.
 * It does not depend on Fabric, NeoForge, texture sprites, or any
 * rendering API.</p>
 *
 * <p>All UV coordinates are normalized to the range {@code 0.0F}
 * to {@code 1.0F}.</p>
 */
public final class CopycatSlopeUV {

    private CopycatSlopeUV() {}

    /**
     * Calculates the UV coordinates for every vertex of a Slope face.
     *
     * <p>The returned UV array has the same length and vertex order
     * as the face's vertex array.</p>
     *
     * @param face the Slope face
     * @return the normalized UV coordinates for each vertex
     */
    public static @NotNull CopycatUV[] calculate(
            @NotNull CopycatFace face
    ) {
        CopycatVertex[] vertices =
                face.vertices();

        CopycatUV[] result =
                new CopycatUV[vertices.length];

        for (int i = 0; i < vertices.length; i++) {
            result[i] =
                    calculate(
                            face,
                            vertices[i]
                    );
        }

        return result;
    }

    /**
     * Calculates the UV coordinate of a single Slope vertex.
     *
     * <p>The UV mapping is selected according to the nominal
     * direction of the face.</p>
     *
     * @param face the face containing the vertex
     * @param vertex the vertex to map
     * @return the normalized UV coordinate
     */
    public static @NotNull CopycatUV calculate(
            @NotNull CopycatFace face,
            @NotNull CopycatVertex vertex
    ) {
        return switch (face.direction()) {

            case DOWN ->
                    mapDown(vertex);

            case UP ->
                    mapSlope(vertex);

            case NORTH ->
                    mapNorth(vertex);

            case SOUTH ->
                    mapSouth(vertex);

            case EAST ->
                    mapEast(vertex);

            case WEST ->
                    mapWest(vertex);
        };
    }

    /**
     * Maps a DOWN face.
     *
     * <p>The X axis becomes U and the Z axis becomes V.</p>
     */
    private static @NotNull CopycatUV mapDown(
            @NotNull CopycatVertex vertex
    ) {
        return new CopycatUV(
                vertex.x(),
                vertex.z()
        );
    }

    /**
     * Maps the inclined Slope face.
     *
     * <p>The X axis becomes U while the Z axis becomes V.
     *
     * <p>The face itself is inclined, therefore Y is not used as
     * the second texture axis. Using Z preserves a continuous
     * texture coordinate along the diagonal plane.</p>
     */
    private static @NotNull CopycatUV mapSlope(
            @NotNull CopycatVertex vertex
    ) {
        return new CopycatUV(
                vertex.x(),
                vertex.z()
        );
    }

    /**
     * Maps a NORTH face.
     *
     * <p>The X axis becomes U and the Y axis becomes V.</p>
     */
    private static @NotNull CopycatUV mapNorth(
            @NotNull CopycatVertex vertex
    ) {
        return new CopycatUV(
                vertex.x(),
                1.0F - vertex.y()
        );
    }

    /**
     * Maps a SOUTH face.
     *
     * <p>The X axis becomes U and the Y axis becomes V.
     *
     * <p>The U axis is reversed relative to NORTH so that the
     * texture maintains a consistent world-facing orientation.</p>
     */
    private static @NotNull CopycatUV mapSouth(
            @NotNull CopycatVertex vertex
    ) {
        return new CopycatUV(
                1.0F - vertex.x(),
                1.0F - vertex.y()
        );
    }

    /**
     * Maps an EAST face.
     *
     * <p>The Z axis becomes U and the Y axis becomes V.</p>
     */
    private static @NotNull CopycatUV mapEast(
            @NotNull CopycatVertex vertex
    ) {
        return new CopycatUV(
                1.0F - vertex.z(),
                1.0F - vertex.y()
        );
    }

    /**
     * Maps a WEST face.
     *
     * <p>The Z axis becomes U and the Y axis becomes V.</p>
     */
    private static @NotNull CopycatUV mapWest(
            @NotNull CopycatVertex vertex
    ) {
        return new CopycatUV(
                vertex.z(),
                1.0F - vertex.y()
        );
    }
}