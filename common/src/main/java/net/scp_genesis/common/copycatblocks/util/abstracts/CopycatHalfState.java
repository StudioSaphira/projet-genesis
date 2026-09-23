package net.scp_genesis.common.copycatblocks.util.abstracts;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.Vec3;
import net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock;
import net.scp_genesis.common.copycatblocks.data.CopycatHalfForm;
import net.scp_genesis.common.copycatblocks.data.CopycatHalfSide;
import static net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock.*;

/** Generic HALF/FACING/SIDE orientation rules shared by half-block families. */
public final class CopycatHalfState {
    private CopycatHalfState() {}
    /** Returns the physical arrangement represented by this block. */
    public static CopycatHalfForm form(BlockState state) {
        return ((AbstractCopycatHalfBlock) state.getBlock()).form();
    }
    /** Vertical doubles deliberately have no HALF property. */
    public static Half half(BlockState state) { return state.hasProperty(HALF) ? state.getValue(HALF) : Half.BOTTOM; }
    /** Horizontal doubles deliberately have no SIDE property. */
    public static CopycatHalfSide side(BlockState state) {
        return state.hasProperty(SIDE) ? state.getValue(SIDE) : CopycatHalfSide.LEFT;
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
        if (state.hasProperty(SIDE) && state.getValue(SIDE) == CopycatHalfSide.LEFT) {
            return state.setValue(SIDE, CopycatHalfSide.RIGHT);
        }
        if (state.hasProperty(SIDE)) state = state.setValue(SIDE, CopycatHalfSide.LEFT);
        Direction facing = state.getValue(FACING);
        state = state.setValue(FACING, facing.getClockWise());
        if (facing == Direction.WEST && state.hasProperty(HALF)) {
            state = state.setValue(HALF, half(state) == Half.BOTTOM ? Half.TOP : Half.BOTTOM);
        }
        return state;
    }
}
