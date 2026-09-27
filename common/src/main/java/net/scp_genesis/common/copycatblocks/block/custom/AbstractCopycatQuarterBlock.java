package net.scp_genesis.common.copycatblocks.block.custom;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.util.abstracts.*;
/** Common material and tool behavior for rectangular quarter-block families. */
public abstract class AbstractCopycatQuarterBlock extends AbstractCopycatHalfBlock {
    protected AbstractCopycatQuarterBlock(Properties properties,boolean slab) {
        super(properties,CopycatHalfForm.SINGLE,CopycatQuarterBehavior.forSlab(slab));
        BlockState initial=stateDefinition.any().setValue(FACING,Direction.NORTH)
                .setValue(slab?CopycatQuarterState.SLAB_JOIN:CopycatQuarterState.PANEL_JOIN,CopycatBoxJoin.NONE);
        if(slab) initial=initial.setValue(HALF,Half.BOTTOM);
        registerDefaultState(initial);
    }
    @Override public CopycatHalfForm form(BlockState state) {
        return switch(CopycatQuarterState.join(state)) {
            case NONE -> CopycatHalfForm.SINGLE;
            case STACK -> CopycatHalfForm.VERTICAL;
            default -> CopycatHalfForm.HORIZONTAL;
        };
    }
}
