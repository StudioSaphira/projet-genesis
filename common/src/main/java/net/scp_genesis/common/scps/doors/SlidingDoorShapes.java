package net.scp_genesis.common.scps.doors;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.*;

/** Centered four-pixel slab and half-pixel fixed rails, shared by all orientations. */
public final class SlidingDoorShapes {
    private SlidingDoorShapes() {}

    public static VoxelShape collision(BlockState state) {
        boolean upper = state.getValue(ScpSlidingDoorBlock.HALF) == DoubleBlockHalf.UPPER;
        double minY = state.getValue(ScpSlidingDoorBlock.OPEN) && upper ? 15.5 : 0;
        double maxY = state.getValue(ScpSlidingDoorBlock.OPEN) && !upper ? 0.5 : 16;
        return state.getValue(ScpSlidingDoorBlock.FACING).getAxis() == Direction.Axis.Z
                ? Block.box(0, minY, 6, 16, maxY, 10) : Block.box(6, minY, 0, 10, maxY, 16);
    }

    /** Whole-door outline; when open only the two actual rails remain selectable. */
    public static VoxelShape selection(BlockState state) {
        BlockState lower = state.setValue(ScpSlidingDoorBlock.HALF, DoubleBlockHalf.LOWER);
        VoxelShape whole = Shapes.or(collision(lower), collision(lower.setValue(
                ScpSlidingDoorBlock.HALF, DoubleBlockHalf.UPPER)).move(0, 1, 0));
        return state.getValue(ScpSlidingDoorBlock.HALF) == DoubleBlockHalf.UPPER ? whole.move(0, -1, 0) : whole;
    }
}
