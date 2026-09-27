package net.scp_genesis.common.copycatblocks.util.abstracts;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.scp_genesis.common.copycatblocks.data.*;
import static net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock.*;
/** Orientation and reversible assembly rules shared by Half-Panel and Half-Slab. */
public final class CopycatQuarterState {
    public static final EnumProperty<CopycatBoxJoin> PANEL_JOIN=EnumProperty.create("double",CopycatBoxJoin.class,CopycatBoxJoin.NONE,CopycatBoxJoin.SIDE,CopycatBoxJoin.DEPTH);
    public static final EnumProperty<CopycatBoxJoin> SLAB_JOIN=EnumProperty.create("double",CopycatBoxJoin.class,CopycatBoxJoin.NONE,CopycatBoxJoin.SIDE,CopycatBoxJoin.STACK);
    private CopycatQuarterState() {}
    public static boolean slab(BlockState state) { return state.hasProperty(HALF); }
    public static EnumProperty<CopycatBoxJoin> property(BlockState state) { return slab(state)?SLAB_JOIN:PANEL_JOIN; }
    public static CopycatBoxJoin join(BlockState state) { return state.getValue(property(state)); }
    /** Exterior faces place a neighbor; only a face bordering vacant space accepts a second component. */
    public static BlockState merged(BlockState state,Direction face) {
        if(join(state)!=CopycatBoxJoin.NONE) return null;
        Direction facing=state.getValue(FACING);
        CopycatBoxJoin joined;
        if(face==facing.getClockWise()) joined=CopycatBoxJoin.SIDE;
        else if(!slab(state)&&face==facing.getOpposite()) joined=CopycatBoxJoin.DEPTH;
        else if(slab(state)&&face==(state.getValue(HALF)==Half.BOTTOM?Direction.UP:Direction.DOWN)) joined=CopycatBoxJoin.STACK;
        else return null;
        return state.setValue(property(state),joined);
    }
    /** Recovers the occupied quarter exactly, including the orientation of the secondary component. */
    public static BlockState remaining(BlockState state,CopycatPart part) {
        CopycatBoxJoin joined=join(state);
        BlockState single=state.setValue(property(state),CopycatBoxJoin.NONE);
        if(part==CopycatPart.BOTTOM) return single;
        Direction facing=state.getValue(FACING);
        return switch(joined) {
            case SIDE -> single.setValue(FACING,slab(state)?facing.getOpposite():facing.getClockWise());
            case DEPTH -> single.setValue(FACING,facing.getCounterClockWise());
            case STACK -> single.setValue(HALF,state.getValue(HALF)==Half.BOTTOM?Half.TOP:Half.BOTTOM);
            case NONE -> throw new IllegalArgumentException("A single component has no secondary half");
        };
    }
    /** Vertical pairs keep the primary half anchored; the other forms use the requested 4/8-step cycle. */
    public static BlockState next(BlockState state) {
        return join(state)==CopycatBoxJoin.STACK ? state.setValue(FACING,state.getValue(FACING).getClockWise()) : CopycatHalfState.next(state);
    }
}
