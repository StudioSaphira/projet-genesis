package net.scp_genesis.common.copycatblocks.util.abstracts;

import net.minecraft.core.*;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.util.CopycatItemHelper;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import static net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock.*;

/** Reversible same-block transitions; material slots are replaced together to prevent stale copies. */
public final class CopycatFullSlopeTransitions {
    private CopycatFullSlopeTransitions() {}
    /** Only the exposed diagonal accepts another component; exterior faces place adjacent blocks. */
    public static @Nullable BlockState mergedState(BlockState state, Direction clicked) {
        if (state.getValue(DOUBLE)) return null;
        Direction facing = state.getValue(FACING);
        boolean vertical = net.scp_genesis.common.copycatblocks.geometry.slope.CopycatFullSlopeGeometry.vertical(state);
        boolean diagonal = clicked == facing.getOpposite() || clicked == (vertical ? facing.getClockWise()
                : state.getValue(HALF) == net.minecraft.world.level.block.state.properties.Half.BOTTOM ? Direction.UP : Direction.DOWN);
        return diagonal ? state.setValue(DOUBLE,true) : null;
    }
    /** Retains the original material in its primary diagonal slot. */
    public static CopycatPart originalPart(BlockState state) {
        return CopycatPart.BOTTOM;
    }
    /** Resolves the surviving single prism after the remover separates a double. */
    public static BlockState remainingState(BlockState state, CopycatPart remaining) {
        var single=state.setValue(DOUBLE,false);
        return remaining==CopycatPart.BOTTOM ? single : net.scp_genesis.common.copycatblocks.geometry.slope.CopycatFullSlopeGeometry.complement(single);
    }
    /** Completes a half block after permission/collision checks, consuming one item only on success. */
    public static ItemInteractionResult merge(Level level, BlockPos pos, BlockState state,
            Player player, ItemStack stack, BlockHitResult hit) {
        var target = mergedState(state,hit.getDirection());
        if (target == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!level.mayInteract(player,pos) || !player.mayUseItemAt(pos,hit.getDirection(),stack)
                || !level.isUnobstructed(target,pos,CollisionContext.of(player))) return ItemInteractionResult.FAIL;
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof CopycatBlockEntity entity)) return ItemInteractionResult.FAIL;
        var material = entity.getCopiedState(CopycatPart.MAIN);
        if (!replace(level,pos,target,originalPart(state),material)) return ItemInteractionResult.FAIL;
        if (!player.isCreative()) stack.shrink(1);
        var sound = target.getSoundType();
        level.playSound(null,pos,sound.getPlaceSound(),SoundSource.BLOCKS,(sound.getVolume()+1)/2,sound.getPitch()*0.8F);
        level.gameEvent(GameEvent.BLOCK_PLACE,pos,GameEvent.Context.of(player,target));
        return ItemInteractionResult.SUCCESS;
    }
    /** Returns one component and its material, preserving the other component as MAIN. */
    public static InteractionResult remove(Level level, BlockPos pos, BlockState state,
            CopycatPart removed, Player player, ItemStack copied) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof CopycatBlockEntity entity)) return InteractionResult.PASS;
        var remaining = removed == CopycatPart.BOTTOM ? CopycatPart.TOP : CopycatPart.BOTTOM;
        var material = entity.getCopiedState(remaining);
        if (!replace(level,pos,remainingState(state,remaining),CopycatPart.MAIN,material)) return InteractionResult.FAIL;
        if (player != null && !player.isCreative()) {
            CopycatItemHelper.giveOrDrop(player,new ItemStack(((net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock) state.getBlock()).behavior().componentItem()));
            CopycatItemHelper.giveOrDrop(player,copied);
        }
        return InteractionResult.SUCCESS;
    }
    private static boolean replace(Level level, BlockPos pos, BlockState target, CopycatPart part, BlockState material) {
        if (!level.setBlock(pos,target,Block.UPDATE_ALL)) return false;
        if (!(level.getBlockEntity(pos) instanceof CopycatBlockEntity entity)) {
            throw new IllegalStateException("Half block is missing its Copycat block entity");
        }
        entity.replaceCopiedStates(Map.of(part,material));
        ((net.scp_genesis.common.copycatblocks.block.custom.basic.CopycatSlopeBlock) target.getBlock()).updateOcclusionState(entity);
        return true;
    }
}
