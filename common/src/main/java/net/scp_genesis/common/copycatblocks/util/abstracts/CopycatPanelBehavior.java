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
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.geometry.CopycatOrientedShapes;
import net.scp_genesis.common.registry.ModItems;
import static net.scp_genesis.common.copycatblocks.block.custom.basic.CopycatPanelBlock.*;

/** Panel-specific shape, targeting and rotation on the generic half-block framework. */
public final class CopycatPanelBehavior implements CopycatHalfBehavior {
    public static final CopycatPanelBehavior INSTANCE = new CopycatPanelBehavior();
    private static final CopycatOrientedShapes LEFT = new CopycatOrientedShapes(Shapes.box(0,0,0,0.5,1,1));
    private static final CopycatOrientedShapes RIGHT = new CopycatOrientedShapes(Shapes.box(0.5,0,0,1,1,1));
    private CopycatPanelBehavior() {}
    @Override public Item componentItem() { return ModItems.COPYCAT_PANEL.get(); }
    @Override public VoxelShape shape(BlockState state) {
        if (state.getValue(DOUBLE)) return Shapes.block();
        return (state.getValue(SIDE) == CopycatHalfSide.LEFT ? LEFT : RIGHT).get(state.getValue(FACING), Half.BOTTOM);
    }
    /** Doubles ignore SIDE, including manually supplied noncanonical RIGHT states. */
    @Override public BlockState next(BlockState state) {
        return state.getValue(DOUBLE)
                ? state.setValue(FACING, state.getValue(FACING).getClockWise()).setValue(SIDE, CopycatHalfSide.LEFT)
                : CopycatHalfState.next(state);
    }
    @Override public CopycatPart target(BlockState state, BlockHitResult hit) {
        if (!state.getValue(DOUBLE)) return CopycatPart.MAIN;
        Direction normal = hit.getDirection();
        Vec3 point = hit.getLocation().subtract(Vec3.atLowerCornerOf(hit.getBlockPos()))
                .subtract(normal.getStepX()*1e-5, normal.getStepY()*1e-5, normal.getStepZ()*1e-5);
        return CopycatHalfState.local(point, state.getValue(FACING)).x < 0.5 ? CopycatPart.BOTTOM : CopycatPart.TOP;
    }
    @Override public ItemInteractionResult merge(Level level, BlockPos pos, BlockState state,
            Player player, ItemStack stack, BlockHitResult hit) {
        return CopycatPanelTransitions.merge(level,pos,state,player,stack,hit);
    }
    @Override public InteractionResult remove(Level level, BlockPos pos, BlockState state,
            CopycatPart removed, Player player, ItemStack copied) {
        return CopycatPanelTransitions.remove(level,pos,state,removed,player,copied);
    }
}
