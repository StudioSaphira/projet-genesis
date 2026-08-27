package net.scp_genesis.common.copycatblocks.geometry.slope;

/**
 * Represents a mathematical vector used by Copycat Slope geometry
 * calculations.
 *
 * <p>Unlike {@link CopycatSlopeVertex}, which represents a position
 * in block-local space, this class represents a direction or
 * displacement in three-dimensional space.</p>
 *
 * <p>Vector components use the same normalized coordinate system
 * as {@link CopycatSlopeVertex}:</p>
 *
 * <ul>
 *     <li>{@code x} — horizontal X component</li>
 *     <li>{@code y} — vertical Y component</li>
 *     <li>{@code z} — horizontal Z component</li>
 * </ul>
 *
 * <p>This type is intentionally independent of Fabric and NeoForge
 * so that all Slope geometry calculations can remain in the
 * common module.</p>
 *
 * @param x the X component of the vector
 * @param y the Y component of the vector
 * @param z the Z component of the vector
 */
public record CopycatSlopeVector(
        float x,
        float y,
        float z
) {
}