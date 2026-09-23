package net.scp_genesis.common.copycatblocks.util.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock;
import net.scp_genesis.common.copycatblocks.data.CopycatHalfForm;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.slope.half.CopycatHalfSlopeShapes;
import net.scp_genesis.common.registry.ModItems;
import static net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock.FACING;

/** The half-slope family's shapes, diagonal targeting and material-preserving transitions. */
public final class CopycatHalfSlopeBehavior implements CopycatHalfBehavior {
    public static final CopycatHalfSlopeBehavior INSTANCE = new CopycatHalfSlopeBehavior();
    private CopycatHalfSlopeBehavior() {}

    /** Renderers must recognize the slope strategy, not all future half-block families. */
    public static boolean isSlope(BlockState state) {
        return state.getBlock() instanceof AbstractCopycatHalfBlock block && block.behavior() == INSTANCE;
    }
    @Override public VoxelShape shape(BlockState state) { return CopycatHalfSlopeShapes.get(state); }
    @Override public Item componentItem() { return ModItems.COPYCAT_HALF_SLOPE.get(); }

    /** Nudges the hit inward before distinguishing lateral halves or complementary diagonal regions. */
    @Override public CopycatPart target(BlockState state, BlockHitResult hit) {
        var form = CopycatHalfState.form(state);
        if (form == CopycatHalfForm.SINGLE) return CopycatPart.MAIN;
        Direction normal = hit.getDirection();
        Vec3 point = hit.getLocation().subtract(Vec3.atLowerCornerOf(hit.getBlockPos()))
                .subtract(normal.getStepX() * 1.0e-5, normal.getStepY() * 1.0e-5, normal.getStepZ() * 1.0e-5);
        Vec3 local = CopycatHalfState.local(point, state.getValue(FACING));
        return (form == CopycatHalfForm.HORIZONTAL ? local.x < 0.5 : local.y + local.z <= 1)
                ? CopycatPart.BOTTOM : CopycatPart.TOP;
    }
    @Override public ItemInteractionResult merge(Level level, BlockPos pos, BlockState state,
            Player player, ItemStack stack, BlockHitResult hit) {
        return CopycatHalfSlopeTransitions.merge(level, pos, state, player, stack, hit);
    }
    @Override public InteractionResult remove(Level level, BlockPos pos, BlockState state,
            CopycatPart removed, Player player, ItemStack copied) {
        return CopycatHalfSlopeTransitions.remove(level, pos, state, removed, player, copied);
    }
}
