package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.scp_genesis.common.copycatblocks.geometry.CopycatOrientedShapes;

/** Owns the slope's stepped collision shape and separate, inset light-occlusion shape. */
public final class CopycatSlopeShapes {
    private static final CopycatOrientedShapes COLLISION = new CopycatOrientedShapes(Shapes.or(
            Block.box(0, 0, 0, 16, 16, 4), Block.box(0, 0, 4, 16, 12, 8),
            Block.box(0, 0, 8, 16, 8, 12), Block.box(0, 0, 12, 16, 4, 16)));
    private static final CopycatOrientedShapes OCCLUSION = new CopycatOrientedShapes(Shapes.or(
            Block.box(0, 0, 0, 16, 0.5, 16), Block.box(0, 0.5, 0, 16, 4, 15.5),
            Block.box(0, 4, 0, 16, 8, 12), Block.box(0, 8, 0, 16, 12, 8),
            Block.box(0, 12, 0, 16, 15.5, 4), Block.box(0, 15.5, 0, 16, 16, 0.5)));

    private CopycatSlopeShapes() {}

    /** Returns the four-step approximation used for collision and block selection. */
    public static VoxelShape collision(Direction facing, Half half) {
        return COLLISION.get(facing, half);
    }

    /** Returns the inset shape used only when the copied material occludes light. */
    public static VoxelShape occlusion(Direction facing, Half half) {
        return OCCLUSION.get(facing, half);
    }
}
