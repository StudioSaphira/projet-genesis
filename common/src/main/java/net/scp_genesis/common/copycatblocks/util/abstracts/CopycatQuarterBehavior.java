package net.scp_genesis.common.copycatblocks.util.abstracts;
import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.geometry.CopycatQuarterGeometry;
import net.scp_genesis.common.registry.ModItems;
import java.util.concurrent.ConcurrentHashMap;
import static net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock.*;
/** Cached collision and tool targeting for rectangular quarter components and their doubles. */
public final class CopycatQuarterBehavior implements CopycatHalfBehavior {
    private static final CopycatQuarterBehavior PANEL=new CopycatQuarterBehavior(false),SLAB=new CopycatQuarterBehavior(true);
    private final boolean slab;
    private final ConcurrentHashMap<BlockState,VoxelShape> shapes=new ConcurrentHashMap<>();
    private CopycatQuarterBehavior(boolean slab){this.slab=slab;}
    public static CopycatQuarterBehavior forSlab(boolean slab){return slab?SLAB:PANEL;}
    @Override public Item componentItem(){return (slab?ModItems.COPYCAT_HALF_SLAB:ModItems.COPYCAT_HALF_PANEL).get();}
    @Override public VoxelShape shape(BlockState state){return shapes.computeIfAbsent(state,CopycatQuarterGeometry::shape);}
    @Override public BlockState next(BlockState state){return CopycatQuarterState.next(state);}
    @Override public CopycatPart target(BlockState state,BlockHitResult hit){
        var join=CopycatQuarterState.join(state);if(join==CopycatBoxJoin.NONE)return CopycatPart.MAIN;
        Direction n=hit.getDirection();
        Vec3 p=CopycatHalfState.local(hit.getLocation().subtract(Vec3.atLowerCornerOf(hit.getBlockPos()))
                .subtract(n.getStepX()*1e-5,n.getStepY()*1e-5,n.getStepZ()*1e-5),state.getValue(FACING));
        double y=CopycatHalfState.half(state)==Half.TOP?1-p.y:p.y;
        boolean primary=switch(join){case SIDE->p.x<.5;case DEPTH->p.z<.5;case STACK->y<.5;default->true;};
        return primary?CopycatPart.BOTTOM:CopycatPart.TOP;
    }
    @Override public ItemInteractionResult merge(Level l,BlockPos p,BlockState s,Player player,ItemStack stack,BlockHitResult hit){return CopycatQuarterTransitions.merge(l,p,s,player,stack,hit);}
    @Override public InteractionResult remove(Level l,BlockPos p,BlockState s,CopycatPart part,Player player,ItemStack copied){return CopycatQuarterTransitions.remove(l,p,s,part,player,copied);}
}
