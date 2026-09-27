package net.scp_genesis.common.copycatblocks.block.custom.basic;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.*;

/** Full-height triangular prism; four horizontal orientations also rotate its two-material double. */
public final class CopycatVerticalSlopeBlock extends CopycatSlopeBlock {
    public static final MapCodec<CopycatVerticalSlopeBlock> CODEC = simpleCodec(CopycatVerticalSlopeBlock::new);
    public CopycatVerticalSlopeBlock(Properties properties) { super(properties, true); }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, DOUBLE, OCCLUDES);
    }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }
    @Override protected MapCodec<? extends CopycatVerticalSlopeBlock> codec() { return CODEC; }
}
