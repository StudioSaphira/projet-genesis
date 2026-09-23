package net.scp_genesis.common.copycatblocks.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import net.scp_genesis.common.copycatblocks.block.AbstractCopycatBlock;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.util.abstracts.CopycatHalfActions;
import net.scp_genesis.common.copycatblocks.util.abstracts.CopycatHalfBehavior;
import org.jetbrains.annotations.NotNull;

/**
 * Generic base for single and combined half Copycats. Concrete blocks select their form and
 * shape-family strategy; this base has no slope geometry, item registry or platform dependency.
 */
public abstract class AbstractCopycatHalfBlock extends AbstractCopycatBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<CopycatHalfSide> SIDE = EnumProperty.create("side", CopycatHalfSide.class);
    private final CopycatHalfForm form;
    private final CopycatHalfBehavior behavior;

    protected AbstractCopycatHalfBlock(Properties properties, CopycatHalfForm form, CopycatHalfBehavior behavior) {
        super(properties);
        this.form = java.util.Objects.requireNonNull(form);
        this.behavior = java.util.Objects.requireNonNull(behavior);
    }
    /** Returns the arrangement; concrete classes only register the properties used by that form. */
    public final CopycatHalfForm form() { return form; }
    /** Resolves families whose single/double arrangement is stored in a block-state property. */
    public CopycatHalfForm form(BlockState state) { return form; }
    /** Returns the shape-family strategy, also used to select a compatible custom renderer. */
    public final CopycatHalfBehavior behavior() { return behavior; }

    @Override protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                                    @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return behavior.shape(state);
    }
    /** A partially opaque multipart block must not occlude the entire neighboring face. */
    @Override protected @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state,
                                                             @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }
    @Override public boolean isWrenchable() { return true; }
    @Override protected boolean isMultipartState(@NotNull BlockState state) { return form(state) != CopycatHalfForm.SINGLE; }
    @Override public InteractionResult onWrench(Level level, BlockPos pos, BlockState state) {
        return CopycatHalfActions.wrench(behavior, level, pos, state);
    }
    @Override public CopycatPart getCopycatPart(@NotNull BlockState state, @NotNull BlockHitResult hit) {
        return behavior.target(state, hit);
    }
    @Override public @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state,
            @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand,
            @NotNull BlockHitResult hit) {
        return CopycatHalfActions.use(behavior, form(state), stack, state, level, pos, player, hit,
                () -> getCopycatBlockEntity(level, pos), () -> getCopycatPart(state, hit),
                () -> super.useItemOn(stack, state, level, pos, player, hand, hit));
    }
    @Override protected InteractionResult onRemovePart(Level level, BlockPos pos, @NotNull BlockState state,
            @NotNull CopycatPart removed, Player player, @NotNull ItemStack ignored, @NotNull ItemStack copied) {
        return behavior.remove(level, pos, state, removed, player, copied);
    }
    /** Picking a combined shape gives the family's construction component. */
    @Override public @NotNull ItemStack getCloneItemStack(@NotNull LevelReader level, @NotNull BlockPos pos,
                                                         @NotNull BlockState state) {
        return new ItemStack(behavior.componentItem());
    }
}
