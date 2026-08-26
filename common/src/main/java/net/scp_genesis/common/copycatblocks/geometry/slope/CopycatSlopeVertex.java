package net.scp_genesis.common.copycatblocks.geometry.slope;

/**
 * Represents a vertex of a Copycat Slope geometry.
 *
 * @param x the X coordinate in block-local space
 * @param y the Y coordinate in block-local space
 * @param z the Z coordinate in block-local space
 */
public record CopycatSlopeVertex(
        float x,
        float y,
        float z
) {
}