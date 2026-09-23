package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.Vec3;
import net.scp_genesis.common.copycatblocks.block.custom.half.AbstractCopycatHalfSlopeBlock;
import net.scp_genesis.common.copycatblocks.data.CopycatHalfSlopeForm;
import net.scp_genesis.common.copycatblocks.data.CopycatSlopeSide;
import static net.scp_genesis.common.copycatblocks.block.custom.half.AbstractCopycatHalfSlopeBlock.*;

/** Shared orientation rules for placement, rendering, targeting and wrench rotation. */
public final class CopycatHalfSlopeState {
    private CopycatHalfSlopeState() {}
    /** Returns the physical arrangement represented by this block. */
    public static CopycatHalfSlopeForm form(BlockState state) {
        return ((AbstractCopycatHalfSlopeBlock) state.getBlock()).form();
    }
    /** Vertical doubles deliberately have no HALF property. */
    public static Half half(BlockState state) { return state.hasProperty(HALF) ? state.getValue(HALF) : Half.BOTTOM; }
    /** Horizontal doubles deliberately have no SIDE property. */
    public static CopycatSlopeSide side(BlockState state) {
        return state.hasProperty(SIDE) ? state.getValue(SIDE) : CopycatSlopeSide.LEFT;
    }
    /** Inverse horizontal rotation from block-local coordinates into the NORTH frame. */
    public static Vec3 local(Vec3 point, Direction facing) {
        return switch (facing) {
            case NORTH -> point;
            case EAST -> new Vec3(point.z, point.y, 1 - point.x);
            case SOUTH -> new Vec3(1 - point.x, point.y, 1 - point.z);
            case WEST -> new Vec3(1 - point.z, point.y, point.x);
            default -> throw new IllegalArgumentException("Horizontal facing required");
        };
    }
    /** Advances LEFT, RIGHT, then facing, then HALF; absent properties are skipped. */
    public static BlockState next(BlockState state) {
        if (state.hasProperty(SIDE) && state.getValue(SIDE) == CopycatSlopeSide.LEFT) {
            return state.setValue(SIDE, CopycatSlopeSide.RIGHT);
        }
        if (state.hasProperty(SIDE)) state = state.setValue(SIDE, CopycatSlopeSide.LEFT);
        Direction facing = state.getValue(FACING);
        state = state.setValue(FACING, facing.getClockWise());
        if (facing == Direction.WEST && state.hasProperty(HALF)) {
            state = state.setValue(HALF, half(state) == Half.BOTTOM ? Half.TOP : Half.BOTTOM);
        }
        return state;
    }
}
