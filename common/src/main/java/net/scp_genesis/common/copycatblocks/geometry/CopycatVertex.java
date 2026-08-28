package net.scp_genesis.common.copycatblocks.geometry;

/**
 * Represents a vertex of a Copycat Slope geometry.
 *
 * @param x the X coordinate in block-local space
 * @param y the Y coordinate in block-local space
 * @param z the Z coordinate in block-local space
 */
public record CopycatVertex(
        float x,
        float y,
        float z
) {
}