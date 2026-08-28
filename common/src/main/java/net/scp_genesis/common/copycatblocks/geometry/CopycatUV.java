package net.scp_genesis.common.copycatblocks.geometry;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a normalized UV texture coordinate.
 *
 * <p>This class is part of the common Copycat geometry system and
 * does not depend on Fabric, NeoForge, or any rendering API.</p>
 *
 * <p>Both coordinates are normalized to the range {@code 0.0F}
 * to {@code 1.0F}.</p>
 *
 * @param u horizontal texture coordinate
 * @param v vertical texture coordinate
 */
public record CopycatUV(
        float u,
        float v
) {

    /**
     * Returns a UV coordinate with both components inverted.
     *
     * @return the inverted UV coordinate
     */
    public @NotNull CopycatUV invert() {
        return new CopycatUV(
                1.0F - u,
                1.0F - v
        );
    }

    /**
     * Returns a UV coordinate with its U component inverted.
     *
     * @return the U-inverted coordinate
     */
    public @NotNull CopycatUV invertU() {
        return new CopycatUV(
                1.0F - u,
                v
        );
    }

    /**
     * Returns a UV coordinate with its V component inverted.
     *
     * @return the V-inverted coordinate
     */
    public @NotNull CopycatUV invertV() {
        return new CopycatUV(
                u,
                1.0F - v
        );
    }
}