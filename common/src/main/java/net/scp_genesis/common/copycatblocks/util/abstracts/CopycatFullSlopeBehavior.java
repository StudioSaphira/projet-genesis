package net.scp_genesis.common.copycatblocks.util.abstracts;

import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.*;
import net.scp_genesis.common.copycatblocks.geometry.slope.*;
import net.scp_genesis.common.registry.ModItems;
import static net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock.*;

/** Full-slope collisions, diagonal targeting, rotation and reversible combination. */
public final class CopycatFullSlopeBehavior implements CopycatHalfBehavior {
    private static final CopycatFullSlopeBehavior HORIZONTAL = new CopycatFullSlopeBehavior(false);
    private static final CopycatFullSlopeBehavior VERTICAL = new CopycatFullSlopeBehavior(true);
    private static final CopycatOrientedShapes VERTICAL_SHAPE = new CopycatOrientedShapes(verticalShape());
    private final boolean vertical;
    private CopycatFullSlopeBehavior(boolean vertical) { this.vertical=vertical; }
    public static CopycatFullSlopeBehavior forVertical(boolean vertical) { return vertical ? VERTICAL : HORIZONTAL; }
    private static VoxelShape verticalShape() {
        VoxelShape result=Shapes.empty();
        for(var box:CopycatSlopeShapes.collision(Direction.NORTH,Half.BOTTOM).toAabbs())
            result=Shapes.or(result,Shapes.box(box.minY,box.minX,box.minZ,box.maxY,box.maxX,box.maxZ));
        return result;
    }
    @Override public Item componentItem() { return vertical ? ModItems.COPYCAT_VERTICAL_SLOPE.get() : ModItems.COPYCAT_SLOPE.get(); }
    @Override public VoxelShape shape(BlockState state) {
        if(state.getValue(DOUBLE)) return Shapes.block();
        return vertical ? VERTICAL_SHAPE.get(state.getValue(FACING),Half.BOTTOM)
                : CopycatSlopeShapes.collision(state.getValue(FACING),state.getValue(HALF));
    }
    @Override public BlockState next(BlockState state) { return CopycatHalfState.next(state); }
    @Override public CopycatPart target(BlockState state, BlockHitResult hit) {
        if(!state.getValue(DOUBLE)) return CopycatPart.MAIN;
        var normal=hit.getDirection();
        Vec3 p=CopycatHalfState.local(hit.getLocation().subtract(Vec3.atLowerCornerOf(hit.getBlockPos()))
                .subtract(normal.getStepX()*1e-5,normal.getStepY()*1e-5,normal.getStepZ()*1e-5),state.getValue(FACING));
        boolean primary=vertical ? p.x+p.z<=1 : (state.getValue(HALF)==Half.BOTTOM ? p.y+p.z<=1 : p.y>=p.z);
        return primary ? CopycatPart.BOTTOM : CopycatPart.TOP;
    }
    @Override public ItemInteractionResult merge(Level level,BlockPos pos,BlockState state,Player player,ItemStack stack,BlockHitResult hit) {
        return CopycatFullSlopeTransitions.merge(level,pos,state,player,stack,hit);
    }
    @Override public InteractionResult remove(Level level,BlockPos pos,BlockState state,CopycatPart removed,Player player,ItemStack copied) {
        return CopycatFullSlopeTransitions.remove(level,pos,state,removed,player,copied);
    }
}
