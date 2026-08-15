package net.scp_genesis.copycatblocks.block;

import net.minecraft.world.item.BlockItem;
import net.scp_genesis.copycatblocks.data.CopycatPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.scp_genesis.copycatblocks.api.CopycatBlocksAPI;
import net.scp_genesis.copycatblocks.item.CopycatRemoverItem;
import net.scp_genesis.copycatblocks.item.CopycatWrenchItem;
import net.scp_genesis.copycatblocks.blockentity.CopycatBlockEntity;

/**
 * Base class for every Copycat Block.
 *
 * <p>This class contains all gameplay logic shared by every Copycat Block.
 * Rendering is handled separately by the Copycat renderer.</p>
 */
public abstract class AbstractCopycatBlock extends BaseEntityBlock implements EntityBlock {

    protected AbstractCopycatBlock(Properties properties) {
        super(properties);
    }

    /**
     * Creates the BlockEntity used by this Copycat Block.
     */
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CopycatBlockEntity(pos, state);
    }

    /**
     * Uses the model renderer.
     */
    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }



    /**
     * Returns whether this Copycat supports the Copycat Wrench.
     *
     * <p>Override this method in blocks that can change their
     * orientation or geometry.</p>
     */
    public boolean isWrenchable() {
        return false;
    }

    /**
     * Called when the Copycat Wrench is used.
     *
     * <p>Override this method in subclasses.</p>
     */
    public InteractionResult onWrench(Level level, BlockPos pos, BlockState state) {
        return InteractionResult.PASS;
    }

    /**
     * Clears the copied BlockState.
     */
    public InteractionResult onRemover(Level level, BlockPos pos) {

        CopycatBlockEntity blockEntity = getCopycatBlockEntity(level, pos);
        
        if (blockEntity == null) {
            return InteractionResult.PASS;
        }

        if (!blockEntity.hasCopiedState()) {
            return InteractionResult.PASS;
        }

        CopycatBlocksAPI.clear(level, pos);

        afterClear(blockEntity);

        return InteractionResult.SUCCESS;
    }

    /**
     * Copies the given BlockState into this Copycat Block.
     *
     * @return true if the copy operation succeeded.
     */
    protected boolean copyState(
            Level level,
            BlockPos pos,
            CopycatPart part,
            BlockState copiedState
    ) {
        if (!CopycatBlocksAPI.copy(
                level,
                pos,
                part,
                copiedState
        )) {
            return false;
        }

        CopycatBlockEntity blockEntity =
                getCopycatBlockEntity(level, pos);

        if (blockEntity != null) {
            afterCopy(blockEntity);
        }

        return true;
    }

    /**
     * Returns the Copycat part targeted by the interaction.
     *
     * <p>Single-part Copycat Blocks use the MAIN part by default.
     * Specialized Copycat Blocks can override this method.</p>
     */
    protected CopycatPart getCopycatPart(
            BlockState state,
            BlockHitResult hitResult
    ) {
        return CopycatPart.MAIN;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(
            ItemStack stack,
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hitResult
    ) {
        if (stack.getItem() instanceof CopycatRemoverItem) {
            return onRemover(level, pos).consumesAction()
                    ? ItemInteractionResult.SUCCESS
                    : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (stack.getItem() instanceof CopycatWrenchItem) {
            if (!isWrenchable()) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            return onWrench(level, pos, state).consumesAction()
                    ? ItemInteractionResult.SUCCESS
                    : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof AbstractCopycatBlock) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        BlockState copiedState = CopycatBlocksAPI.getBlockStateFromItem(stack);
        if (copiedState.isAir()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        CopycatPart part = getCopycatPart(
                state,
                hitResult
        );

        if (!copyState(
                level,
                pos,
                part,
                copiedState
        )) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        return ItemInteractionResult.SUCCESS;
    }

    /**
     * Returns the Copycat BlockEntity at the given position.
     *
     * @return the Copycat BlockEntity, or {@code null} if none exists.
     */
    @Nullable
    protected CopycatBlockEntity getCopycatBlockEntity(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof CopycatBlockEntity blockEntity) {
            return blockEntity;
        }

        return null;
    }

    /**
     * Called after a successful copy.
     */
    protected void afterCopy(CopycatBlockEntity blockEntity) {

    }

    /**
     * Called after the Copycat has been cleared.
     */
    protected void afterClear(CopycatBlockEntity blockEntity) {

    }

}