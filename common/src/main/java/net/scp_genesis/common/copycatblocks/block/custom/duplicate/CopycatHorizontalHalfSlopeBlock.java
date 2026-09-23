package net.scp_genesis.common.copycatblocks.block.custom.duplicate;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock;
import net.scp_genesis.common.copycatblocks.data.*;
/** A two-material horizontal arrangement, obtained by combining two half-slopes. */
public final class CopycatHorizontalHalfSlopeBlock extends AbstractCopycatHalfBlock {
    public static final MapCodec<CopycatHorizontalHalfSlopeBlock> CODEC = simpleCodec(CopycatHorizontalHalfSlopeBlock::new);
    public CopycatHorizontalHalfSlopeBlock(Properties properties) {
        super(properties, CopycatHalfForm.HORIZONTAL, net.scp_genesis.common.copycatblocks.util.abstracts.CopycatHalfSlopeBehavior.INSTANCE);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HALF, Half.BOTTOM));
    }
    @Override protected MapCodec<? extends CopycatHorizontalHalfSlopeBlock> codec() { return CODEC; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }
}
