package net.scp_genesis.common.copycatblocks.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.util.abstracts.CopycatMaterialActions;
import net.scp_genesis.common.copycatblocks.util.abstracts.CopycatRemovalActions;
import net.scp_genesis.common.copycatblocks.util.abstracts.CopycatInteractionActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Vanilla-only Copycat extension points. Dedicated helpers implement interactions;
 * subclasses retain control over targeted parts, partial removal and material-change hooks.
 */
public abstract class AbstractCopycatBlock extends BaseEntityBlock implements EntityBlock {
    protected AbstractCopycatBlock(Properties properties) { super(properties); }

    @Override public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CopycatBlockEntity(pos, state);
    }
    @Override public @NotNull RenderShape getRenderShape(@NotNull BlockState state) { return RenderShape.MODEL; }

    /** Whether the wrench can rotate this block. */
    public boolean isWrenchable() { return false; }
    /** Whether the remover must preserve another component at this position. */
    protected boolean isMultipartState(@NotNull BlockState state) { return false; }
    /** Updates state properties derived from the copied material, when required by the block. */
    public void updateOcclusionState(@NotNull CopycatBlockEntity blockEntity) {}
    /** Optional block-specific wrench operation. */
    public InteractionResult onWrench(Level level, BlockPos pos, BlockState state) { return InteractionResult.PASS; }

    /** Scrapes only the part selected by the block's targeting hook. */
    public InteractionResult onScrape(Level level, BlockPos pos, @NotNull BlockState state,
                                      @NotNull BlockHitResult hit, @Nullable Player player) {
        var entity = getCopycatBlockEntity(level, pos);
        if (entity == null) return InteractionResult.PASS;
        return CopycatMaterialActions.scrape(level, pos, entity, getCopycatPart(state, hit), player, this::afterClear);
    }
    /** Compatibility overload for callers targeting the main material. */
    public InteractionResult onScrape(Level level, BlockPos pos, @Nullable Player player) {
        return CopycatMaterialActions.scrape(level, pos, getCopycatBlockEntity(level, pos),
                CopycatPart.MAIN, player, this::afterClear);
    }

    /** Subclasses implement shape changes and refunds when removing one component. */
    protected InteractionResult onRemovePart(Level level, BlockPos pos, @NotNull BlockState state,
            @NotNull CopycatPart removedPart, @Nullable Player player,
            @NotNull ItemStack copycatItem, @NotNull ItemStack copiedBlockItem) {
        return InteractionResult.PASS;
    }
    /** Delegates whole-block removal or invokes the multipart removal hook. */
    public InteractionResult onRemove(Level level, BlockPos pos, @NotNull BlockState state,
                                       @NotNull BlockHitResult hit, @Nullable Player player) {
        var entity = getCopycatBlockEntity(level, pos);
        if (entity == null) return InteractionResult.PASS;
        return CopycatRemovalActions.remove(level, pos, state, entity,
                getCopycatPart(state, hit), player, isMultipartState(state),
                data -> onRemovePart(level, pos, state, data.part(), player, data.copycatItem(), data.copiedBlockItem()));
    }

    /** Copies through the common API and preserves the post-copy subclass hook. */
    protected boolean copyState(Level level, BlockPos pos, CopycatPart part, BlockState copiedState) {
        return CopycatMaterialActions.copy(level, pos, part, copiedState,
                () -> getCopycatBlockEntity(level, pos), this::afterCopy);
    }
    /** Selects the material affected by a click; single-component blocks use MAIN. */
    protected CopycatPart getCopycatPart(@NotNull BlockState state, @NotNull BlockHitResult hit) {
        return CopycatPart.MAIN;
    }
    @Override public @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state,
            @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
            @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        return CopycatInteractionActions.use(this, stack, state, level, pos, player, hit,
                () -> getCopycatPart(state, hit), (part, material) -> copyState(level, pos, part, material));
    }

    /** Returns the entity if present; override only when supplying a compatible entity lookup. */
    protected @Nullable CopycatBlockEntity getCopycatBlockEntity(Level level, BlockPos pos) {
        return CopycatMaterialActions.entity(level, pos);
    }
    /** Called after successful material copying. */
    protected void afterCopy(@NotNull CopycatBlockEntity blockEntity) {}
    /** Called after a material has been scraped. */
    protected void afterClear(@NotNull CopycatBlockEntity blockEntity) {}
}
