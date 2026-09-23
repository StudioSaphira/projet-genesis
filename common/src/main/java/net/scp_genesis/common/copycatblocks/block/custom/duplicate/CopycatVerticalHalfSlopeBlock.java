package net.scp_genesis.common.copycatblocks.block.custom.duplicate;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.block.custom.half.AbstractCopycatHalfSlopeBlock;
import net.scp_genesis.common.copycatblocks.data.*;
/** A two-material vertical arrangement, obtained by combining two half-slopes. */
public final class CopycatVerticalHalfSlopeBlock extends AbstractCopycatHalfSlopeBlock {
    public static final MapCodec<CopycatVerticalHalfSlopeBlock> CODEC = simpleCodec(CopycatVerticalHalfSlopeBlock::new);
    public CopycatVerticalHalfSlopeBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SIDE, CopycatSlopeSide.LEFT));
    }
    @Override public CopycatHalfSlopeForm form() { return CopycatHalfSlopeForm.VERTICAL; }
    @Override protected MapCodec<? extends CopycatVerticalHalfSlopeBlock> codec() { return CODEC; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SIDE);
    }
}
