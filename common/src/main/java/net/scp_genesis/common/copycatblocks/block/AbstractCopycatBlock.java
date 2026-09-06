package net.scp_genesis.common.copycatblocks.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.scp_genesis.common.copycatblocks.api.CopycatBlocksAPI;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.item.CopycatRemoverItem;
import net.scp_genesis.common.copycatblocks.item.CopycatScraperItem;
import net.scp_genesis.common.copycatblocks.item.CopycatWrenchItem;
import net.scp_genesis.common.copycatblocks.util.CopycatItemHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base class for every Copycat Block.
 *
 * <p>This class contains all gameplay logic shared by every Copycat Block.
 * Rendering is handled separately by the Copycat renderer.</p>
 */
public abstract class AbstractCopycatBlock
        extends BaseEntityBlock
        implements EntityBlock {

    protected AbstractCopycatBlock(Properties properties) {
        super(properties);
    }

    // ------------------------------------------------------------------------
    // Block Entity
    // ------------------------------------------------------------------------

    @Override
    public BlockEntity newBlockEntity(
            @NotNull BlockPos pos,
            @NotNull BlockState state
    ) {
        return new CopycatBlockEntity(pos, state);
    }

    // ------------------------------------------------------------------------
    // Rendering
    // ------------------------------------------------------------------------

    @Override
    public @NotNull RenderShape getRenderShape(
            @NotNull BlockState state
    ) {
        return RenderShape.MODEL;
    }

    // ------------------------------------------------------------------------
    // Copycat capabilities
    // ------------------------------------------------------------------------

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isWrenchable() {
        return false;
    }

    protected boolean isMultipartState(
            @NotNull BlockState state
    ) {
        return false;
    }

    // ------------------------------------------------------------------------
    // Occlusion
    // ------------------------------------------------------------------------

    /**
     * Updates any dynamic block-state properties derived from the
     * contents of the Copycat BlockEntity.
     *
     * <p>Blocks that need dynamic occlusion can override this method.</p>
     */
    public void updateOcclusionState(
            @NotNull CopycatBlockEntity blockEntity
    ) {
    }

    // ------------------------------------------------------------------------
    // Wrench
    // ------------------------------------------------------------------------

    public InteractionResult onWrench(
            Level level,
            BlockPos pos,
            BlockState state
    ) {
        return InteractionResult.PASS;
    }

    // ------------------------------------------------------------------------
    // Scraper
    // ------------------------------------------------------------------------

    public InteractionResult onScrape(
            Level level,
            BlockPos pos,
            @NotNull BlockState state,
            @NotNull BlockHitResult hitResult,
            @Nullable Player player
    ) {
        CopycatBlockEntity blockEntity =
                getCopycatBlockEntity(level, pos);

        if (blockEntity == null) {
            return InteractionResult.PASS;
        }

        CopycatPart part =
                getCopycatPart(state, hitResult);

        if (!blockEntity.hasCopiedState(part)) {
            return InteractionResult.PASS;
        }

        ItemStack copiedBlockItem =
                CopycatItemHelper.getCopiedBlockItem(
                        blockEntity,
                        part
                );

        CopycatBlocksAPI.clear(
                level,
                pos,
                part
        );

        if (player != null && !player.isCreative()) {
            CopycatItemHelper.giveOrDrop(
                    player,
                    copiedBlockItem
            );
        }

        afterClear(blockEntity);

        return InteractionResult.SUCCESS;
    }

    public InteractionResult onScrape(
            Level level,
            BlockPos pos,
            @Nullable Player player
    ) {
        CopycatBlockEntity blockEntity =
                getCopycatBlockEntity(level, pos);

        if (blockEntity == null || !blockEntity.hasCopiedState()) {
            return InteractionResult.PASS;
        }

        ItemStack copiedBlockItem =
                CopycatItemHelper.getCopiedBlockItem(
                        blockEntity,
                        CopycatPart.MAIN
                );

        CopycatBlocksAPI.clear(
                level,
                pos,
                CopycatPart.MAIN
        );

        if (player != null && !player.isCreative()) {
            CopycatItemHelper.giveOrDrop(
                    player,
                    copiedBlockItem
            );
        }

        afterClear(blockEntity);

        return InteractionResult.SUCCESS;
    }

    // ------------------------------------------------------------------------
    // Remover
    // ------------------------------------------------------------------------

    protected InteractionResult onRemovePart(
            Level level,
            BlockPos pos,
            @NotNull BlockState state,
            @NotNull CopycatPart removedPart,
            @Nullable Player player,
            @NotNull ItemStack copycatItem,
            @NotNull ItemStack copiedBlockItem
    ) {
        return InteractionResult.PASS;
    }

    public InteractionResult onRemove(
            Level level,
            BlockPos pos,
            @NotNull BlockState state,
            @NotNull BlockHitResult hitResult,
            @Nullable Player player
    ) {
        CopycatBlockEntity blockEntity =
                getCopycatBlockEntity(level, pos);

        if (blockEntity == null) {
            return InteractionResult.PASS;
        }

        CopycatPart part =
                getCopycatPart(state, hitResult);

        ItemStack copycatItem =
                CopycatItemHelper.getCopycatItem(state);

        ItemStack copiedBlockItem =
                CopycatItemHelper.getCopiedBlockItem(
                        blockEntity,
                        part
                );

        if (isMultipartState(state)) {
            return onRemovePart(
                    level,
                    pos,
                    state,
                    part,
                    player,
                    copycatItem,
                    copiedBlockItem
            );
        }

        CopycatBlocksAPI.clear(
                level,
                pos,
                part
        );

        level.removeBlock(pos, false);

        if (player != null && !player.isCreative()) {
            CopycatItemHelper.giveOrDrop(
                    player,
                    copycatItem
            );

            CopycatItemHelper.giveOrDrop(
                    player,
                    copiedBlockItem
            );
        }

        return InteractionResult.SUCCESS;
    }

    // ------------------------------------------------------------------------
    // Copying
    // ------------------------------------------------------------------------

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

    // ------------------------------------------------------------------------
    // Parts
    // ------------------------------------------------------------------------

    protected CopycatPart getCopycatPart(
            @NotNull BlockState state,
            @NotNull BlockHitResult hitResult
    ) {
        return CopycatPart.MAIN;
    }

    // ------------------------------------------------------------------------
    // Interaction
    // ------------------------------------------------------------------------

    @SuppressWarnings("IfCanBeSwitch")
    @Override
    public @NotNull ItemInteractionResult useItemOn(
            @NotNull ItemStack stack,
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand hand,
            @NotNull BlockHitResult hitResult
    ) {
        if (stack.getItem() instanceof CopycatScraperItem) {
            return onScrape(
                    level,
                    pos,
                    state,
                    hitResult,
                    player
            ).consumesAction()
                    ? ItemInteractionResult.SUCCESS
                    : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (stack.getItem() instanceof CopycatRemoverItem) {
            return onRemove(
                    level,
                    pos,
                    state,
                    hitResult,
                    player
            ).consumesAction()
                    ? ItemInteractionResult.SUCCESS
                    : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (stack.getItem() instanceof CopycatWrenchItem) {
            if (!isWrenchable()) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }

            return onWrench(
                    level,
                    pos,
                    state
            ).consumesAction()
                    ? ItemInteractionResult.SUCCESS
                    : ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() instanceof AbstractCopycatBlock) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        BlockState copiedState =
                CopycatBlocksAPI.getBlockStateFromItem(stack);

        if (copiedState.isAir()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        CopycatPart part =
                getCopycatPart(state, hitResult);

        if (!copyState(
                level,
                pos,
                part,
                copiedState
        )) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!player.isCreative()) {
            stack.shrink(1);
        }

        return ItemInteractionResult.SUCCESS;
    }

    // ------------------------------------------------------------------------
    // Utilities
    // ------------------------------------------------------------------------

    @Nullable
    protected CopycatBlockEntity getCopycatBlockEntity(
            Level level,
            BlockPos pos
    ) {
        if (level.getBlockEntity(pos)
                instanceof CopycatBlockEntity blockEntity) {
            return blockEntity;
        }

        return null;
    }

    protected void afterCopy(
            @NotNull CopycatBlockEntity blockEntity
    ) {
    }

    protected void afterClear(
            @NotNull CopycatBlockEntity blockEntity
    ) {
    }
}