package net.scp_genesis.common.copycatblocks.block.custom.basic;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.util.abstracts.CopycatHalfState;
import net.scp_genesis.common.copycatblocks.util.abstracts.CopycatPanelBehavior;

/** Vertical slab with eight single orientations and four double orientations. */
public final class CopycatPanelBlock extends AbstractCopycatHalfBlock {
    public static final MapCodec<CopycatPanelBlock> CODEC = simpleCodec(CopycatPanelBlock::new);
    public static final BooleanProperty DOUBLE = BooleanProperty.create("double");
    public CopycatPanelBlock(Properties properties) {
        super(properties, CopycatHalfForm.SINGLE, CopycatPanelBehavior.INSTANCE);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(SIDE, CopycatHalfSide.LEFT).setValue(DOUBLE, false));
    }
    @Override public CopycatHalfForm form(BlockState state) {
        return state.getValue(DOUBLE) ? CopycatHalfForm.HORIZONTAL : CopycatHalfForm.SINGLE;
    }
    @Override protected MapCodec<? extends CopycatPanelBlock> codec() { return CODEC; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SIDE, DOUBLE);
    }
    /** Chooses the occupied side using the hit position in the player's horizontal frame. */
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection();
        Vec3 local = CopycatHalfState.local(context.getClickLocation()
                .subtract(Vec3.atLowerCornerOf(context.getClickedPos())), facing);
        return defaultBlockState().setValue(FACING, facing)
                .setValue(SIDE, local.x < 0.5 ? CopycatHalfSide.LEFT : CopycatHalfSide.RIGHT);
    }
}
