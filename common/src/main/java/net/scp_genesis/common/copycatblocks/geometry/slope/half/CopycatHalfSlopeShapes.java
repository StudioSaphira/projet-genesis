package net.scp_genesis.common.copycatblocks.geometry.slope.half;

import net.scp_genesis.common.copycatblocks.util.abstracts.CopycatHalfState;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.*;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.geometry.CopycatShapeTransforms;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatSlopeShapes;

import java.util.concurrent.ConcurrentHashMap;
import static net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock.FACING;

/** Caches the slope's stepped collision and exact vertical-double silhouette. */
public final class CopycatHalfSlopeShapes {
    private static final ConcurrentHashMap<BlockState, VoxelShape> CACHE = new ConcurrentHashMap<>();
    private CopycatHalfSlopeShapes() {}
    /** Returns a cached physical shape for any of the 32 valid states. */
    public static VoxelShape get(BlockState state) { return CACHE.computeIfAbsent(state, CopycatHalfSlopeShapes::create); }
    private static VoxelShape create(BlockState state) {
        var form = CopycatHalfState.form(state);
        var facing = state.getValue(FACING);
        if (form == CopycatHalfForm.HORIZONTAL) {
            return CopycatSlopeShapes.collision(facing, CopycatHalfState.half(state));
        }
        double x = CopycatHalfState.side(state) == CopycatHalfSide.LEFT ? 0 : 0.5;
        VoxelShape shape = Shapes.box(x, 0, 0, x + 0.5, 1, 1);
        if (form == CopycatHalfForm.SINGLE) {
            shape = Shapes.empty();
            for (int i = 0; i < 4; i++) {
                shape = Shapes.or(shape, Shapes.box(x, 0, i * 0.25, x + 0.5, 1 - i * 0.25, (i + 1) * 0.25));
            }
            if (CopycatHalfState.half(state) == Half.TOP) shape = CopycatShapeTransforms.mirrorVertical(shape);
        }
        return CopycatShapeTransforms.rotateHorizontal(shape, facing);
    }
}
