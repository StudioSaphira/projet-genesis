package net.scp_genesis.common.copycatblocks.geometry;

import org.jetbrains.annotations.NotNull;

/**
 * Provides common operations for transforming normalized Copycat UV
 * coordinates.
 *
 * <p>This class contains only platform-independent UV calculations.
 * It does not depend on Fabric, NeoForge, texture sprites, or any
 * rendering API.</p>
 *
 * <p>All UV coordinates use the normalized range
 * {@code 0.0F} to {@code 1.0F}.</p>
 */
public final class CopycatUVMapping {

    private CopycatUVMapping() {
    }

    /**
     * Flips a UV coordinate horizontally.
     *
     * <p>The transformation is:</p>
     *
     * <pre>
     * U' = 1 - U
     * V' = V
     * </pre>
     *
     * @param uv the original UV coordinate
     * @return the horizontally flipped UV coordinate
     */
    public static @NotNull CopycatUV flipU(
            @NotNull CopycatUV uv
    ) {
        return new CopycatUV(
                1.0F - uv.u(),
                uv.v()
        );
    }

    /**
     * Flips a UV coordinate vertically.
     *
     * <p>The transformation is:</p>
     *
     * <pre>
     * U' = U
     * V' = 1 - V
     * </pre>
     *
     * @param uv the original UV coordinate
     * @return the vertically flipped UV coordinate
     */
    public static @NotNull CopycatUV flipV(
            @NotNull CopycatUV uv
    ) {
        return new CopycatUV(
                uv.u(),
                1.0F - uv.v()
        );
    }

    /**
     * Rotates a UV coordinate by 90-degree increments around the
     * center of the normalized UV square.
     *
     * <p>The rotation is clockwise:</p>
     *
     * <ul>
     *     <li>{@code 0} = 0°</li>
     *     <li>{@code 1} = 90°</li>
     *     <li>{@code 2} = 180°</li>
     *     <li>{@code 3} = 270°</li>
     * </ul>
     *
     * @param uv the original UV coordinate
     * @param rotations the number of clockwise 90-degree rotations
     * @return the rotated UV coordinate
     */
    public static @NotNull CopycatUV rotate(
            @NotNull CopycatUV uv,
            int rotations
    ) {
        int normalizedRotations =
                Math.floorMod(
                        rotations,
                        4
                );

        float u = uv.u();
        float v = uv.v();

        for (int i = 0; i < normalizedRotations; i++) {

            float previousU = u;

            u = 1.0F - v;
            v = previousU;
        }

        return new CopycatUV(
                u,
                v
        );
    }

    /**
     * Rotates a UV coordinate clockwise by 90 degrees.
     *
     * @param uv the original UV coordinate
     * @return the rotated UV coordinate
     */
    public static @NotNull CopycatUV rotateClockwise(
            @NotNull CopycatUV uv
    ) {
        return rotate(
                uv,
                1
        );
    }

    /**
     * Rotates a UV coordinate counter-clockwise by 90 degrees.
     *
     * @param uv the original UV coordinate
     * @return the rotated UV coordinate
     */
    public static @NotNull CopycatUV rotateCounterClockwise(
            @NotNull CopycatUV uv
    ) {
        return rotate(
                uv,
                3
        );
    }

    /**
     * Clamps a UV coordinate to the normalized range.
     *
     * <p>This is useful when calculations involving transformed
     * geometry produce values that are only marginally outside
     * the expected range because of floating-point precision.</p>
     *
     * @param uv the UV coordinate to clamp
     * @return the clamped UV coordinate
     */
    public static @NotNull CopycatUV clamp(
            @NotNull CopycatUV uv
    ) {
        return new CopycatUV(
                clamp(uv.u()),
                clamp(uv.v())
        );
    }

    /**
     * Clamps a normalized UV component to {@code 0.0F..1.0F}.
     *
     * @param value the UV component
     * @return the clamped value
     */
    private static float clamp(float value) {
        return Math.clamp(
                value,
                0.0F,
                1.0F);
    }
}