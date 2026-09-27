package net.scp_genesis.common.copycatblocks.block.custom.half;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatQuarterBlock;
import net.scp_genesis.common.copycatblocks.util.abstracts.CopycatQuarterState;
/** 8 by 8 by 16 corner pillar with two possible adjacent doubles. */
public final class CopycatHalfPanelBlock extends AbstractCopycatQuarterBlock {
    public static final MapCodec<CopycatHalfPanelBlock> CODEC=simpleCodec(CopycatHalfPanelBlock::new);
    public CopycatHalfPanelBlock(Properties properties) { super(properties,false); }
    @Override protected MapCodec<? extends CopycatHalfPanelBlock> codec() { return CODEC; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder) {
        builder.add(FACING,CopycatQuarterState.PANEL_JOIN);
    }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        var point=context.getClickLocation().subtract(net.minecraft.world.phys.Vec3.atLowerCornerOf(context.getClickedPos()));
        Direction facing=point.z<0.5?(point.x<0.5?Direction.NORTH:Direction.EAST):(point.x<0.5?Direction.WEST:Direction.SOUTH);
        return defaultBlockState().setValue(FACING,facing);
    }
}
