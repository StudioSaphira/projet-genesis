package net.scp_genesis.common.copycatblocks.util.records;

import net.minecraft.core.Direction;

/**
 * One outline edge in normalized block coordinates, shared by both rendering platforms.
 * @param start first endpoint
 * @param end second endpoint
 * @param direction nominal face direction used by the line shader
 */
public record CopycatOutlineLine(CopycatVertex start, CopycatVertex end, Direction direction) {
    /** Returns the nominal face normal, preserving the existing outline lighting. */
    public CopycatVector normal() {
        return new CopycatVector(direction.getStepX(), direction.getStepY(), direction.getStepZ());
    }
}
