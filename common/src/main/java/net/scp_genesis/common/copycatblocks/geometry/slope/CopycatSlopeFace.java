package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a face of a Copycat Slope geometry.
 *
 * @param direction the nominal direction of the face
 * @param vertices the vertices composing the face
 */
public record CopycatSlopeFace(
        @NotNull Direction direction,
        @NotNull CopycatSlopeVertex[] vertices
) {
}