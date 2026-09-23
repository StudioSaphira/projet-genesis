package net.scp_genesis.common.copycatblocks.block.custom.half;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.Vec3;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatHalfSlopeState;

/** A half-width slope with the 16 HALF/FACING/SIDE orientations. */
public final class CopycatHalfSlopeBlock extends AbstractCopycatHalfSlopeBlock {
    public static final MapCodec<CopycatHalfSlopeBlock> CODEC = simpleCodec(CopycatHalfSlopeBlock::new);
    public CopycatHalfSlopeBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(HALF, Half.BOTTOM).setValue(SIDE, CopycatSlopeSide.LEFT));
    }
    @Override public CopycatHalfSlopeForm form() { return CopycatHalfSlopeForm.SINGLE; }
    @Override protected MapCodec<? extends CopycatHalfSlopeBlock> codec() { return CODEC; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, SIDE);
    }
    /** Chooses lateral occupancy from click position in the rotated frame, like a vertical slab. */
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        Vec3 local = CopycatHalfSlopeState.local(context.getClickLocation()
                .subtract(Vec3.atLowerCornerOf(context.getClickedPos())), facing);
        return defaultBlockState().setValue(FACING, facing)
                .setValue(HALF, local.y >= 0.5 ? Half.TOP : Half.BOTTOM)
                .setValue(SIDE, local.x < 0.5 ? CopycatSlopeSide.LEFT : CopycatSlopeSide.RIGHT);
    }
}
