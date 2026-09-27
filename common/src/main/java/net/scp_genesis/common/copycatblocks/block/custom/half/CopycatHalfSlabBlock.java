package net.scp_genesis.common.copycatblocks.block.custom.half;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatQuarterBlock;
import net.scp_genesis.common.copycatblocks.util.abstracts.CopycatQuarterState;
/** 8 by 16 by 8 half-slab with lateral and stacked doubles. */
public final class CopycatHalfSlabBlock extends AbstractCopycatQuarterBlock {
    public static final MapCodec<CopycatHalfSlabBlock> CODEC=simpleCodec(CopycatHalfSlabBlock::new);
    public CopycatHalfSlabBlock(Properties properties) { super(properties,true); }
    @Override protected MapCodec<? extends CopycatHalfSlabBlock> codec() { return CODEC; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder) {
        builder.add(FACING,HALF,CopycatQuarterState.SLAB_JOIN);
    }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        double y=context.getClickLocation().y-context.getClickedPos().getY();
        return defaultBlockState().setValue(FACING,context.getHorizontalDirection()).setValue(HALF,
                context.getClickedFace()==Direction.DOWN || (context.getClickedFace()!=Direction.UP&&y>0.5)?Half.TOP:Half.BOTTOM);
    }
}
