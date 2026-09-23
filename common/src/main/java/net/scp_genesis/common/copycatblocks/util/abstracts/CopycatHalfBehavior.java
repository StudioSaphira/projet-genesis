package net.scp_genesis.common.copycatblocks.util.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import org.jetbrains.annotations.Nullable;

/**
 * Shape-family strategy for generic half Copycats. Implementations supply geometry and transitions;
 * material storage, tools and client prediction stay in the abstract block and common helpers.
 * Doubles use BOTTOM/TOP as stable primary/secondary slots, irrespective of their world orientation.
 */
public interface CopycatHalfBehavior {
    /** Returns the family's cached physical shape. */
    VoxelShape shape(BlockState state);
    /** Selects MAIN for a single component or BOTTOM/TOP for a double. */
    CopycatPart target(BlockState state, BlockHitResult hit);
    /** Resolves the construction item lazily, after item registration. */
    Item componentItem();
    /** Changes orientation without transferring material slots. Override for a different cycle. */
    default BlockState next(BlockState state) { return CopycatHalfState.next(state); }
    /** Attempts to add a second component; failure must not consume an item or lose a material. */
    ItemInteractionResult merge(Level level, BlockPos pos, BlockState state, Player player,
                                ItemStack stack, BlockHitResult hit);
    /** Removes only the selected component, preserving the surviving material and shape. */
    InteractionResult remove(Level level, BlockPos pos, BlockState state, CopycatPart removed,
                              @Nullable Player player, ItemStack copied);
}
