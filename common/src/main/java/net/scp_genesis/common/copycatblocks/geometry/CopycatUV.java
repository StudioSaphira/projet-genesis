package net.scp_genesis.common.copycatblocks.geometry;

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
}