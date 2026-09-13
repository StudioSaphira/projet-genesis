package net.scp_genesis.common.furnitures.notmodular;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import net.scp_genesis.common.furnitures.storage.LockerBlockEntity;
import net.scp_genesis.common.registry.ModBlockEntities;

public class LockerBlock extends BaseEntityBlock {
    public static final MapCodec<LockerBlock> CODEC = simpleCodec(LockerBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    private final boolean shelves;
    private final VoxelShape[][][] shapes = new VoxelShape[2][2][4];

    private LockerBlock(Properties properties) { this(properties, false); }

    protected LockerBlock(Properties properties, boolean shelves) {
        super(properties);
        this.shelves = shelves;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(HALF, DoubleBlockHalf.LOWER).setValue(OPEN, false));
        VoxelShape housing = Shapes.or(
                box(0, 2, 0, 16, 3, 16), box(0, 31, 0, 16, 32, 16),
                box(0, 3, 0, 1, 31, 16), box(15, 3, 0, 16, 31, 16),
                box(1, 3, 15, 15, 31, 16),
                box(0, 0, 0, 2, 2, 2), box(14, 0, 0, 16, 2, 2),
                box(0, 0, 14, 2, 2, 16), box(14, 0, 14, 16, 2, 16));
        if (shelves) housing = Shapes.or(housing, box(1, 13, 1, 15, 14, 15), box(1, 22, 1, 15, 23, 15));
        for (int open = 0; open < 2; open++) {
            VoxelShape whole = Shapes.or(housing, open == 0 ? box(1, 3, 0, 15, 31, 1) : box(1, 3, -14, 2, 31, 0));
            for (int half = 0; half < 2; half++) {
                VoxelShape part = Shapes.join(whole, box(-16, half * 16, -16, 32, (half + 1) * 16, 32),
                        BooleanOp.AND).move(0, -half, 0).optimize();
                for (int rotation = 0; rotation < 4; rotation++) {
                    shapes[half][open][rotation] = part;
                    VoxelShape rotated = Shapes.empty();
                    for (var b : part.toAabbs()) rotated = Shapes.or(rotated,
                            Shapes.box(1 - b.maxZ, b.minY, b.minX, 1 - b.minZ, b.maxY, b.maxX));
                    part = rotated.optimize();
                }
            }
        }
    }

    public boolean hasShelves() { return shelves; }

    @Override protected MapCodec<? extends LockerBlock> codec() { return CODEC; }

    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, OPEN);
    }

    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        if (pos.getY() >= context.getLevel().getMaxBuildHeight() - 1
                || !context.getLevel().getBlockState(pos.above()).canBeReplaced(context)) return null;
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        level.setBlock(pos.above(), state.setValue(HALF, DoubleBlockHalf.UPPER), UPDATE_ALL);
    }

    @Override protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) return level.getBlockState(pos.below()).is(this);
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP);
    }

    @Override protected BlockState updateShape(BlockState state, Direction direction, BlockState neighbor,
                                               LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        boolean lower = state.getValue(HALF) == DoubleBlockHalf.LOWER;
        if (direction == (lower ? Direction.UP : Direction.DOWN)) {
            return neighbor.is(this) && neighbor.getValue(HALF) != state.getValue(HALF)
                    ? state.setValue(FACING, neighbor.getValue(FACING)).setValue(OPEN, neighbor.getValue(OPEN))
                    : Blocks.AIR.defaultBlockState();
        }
        if (lower && direction == Direction.DOWN && !state.canSurvive(level, pos)) return Blocks.AIR.defaultBlockState();
        return super.updateShape(state, direction, neighbor, level, pos, neighborPos);
    }

    @Override public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && state.getValue(HALF) == DoubleBlockHalf.UPPER
                && (player.isCreative() || !player.hasCorrectToolForDrops(state))) {
            BlockPos base = pos.below();
            BlockState lower = level.getBlockState(base);
            if (lower.is(this)) {
                level.setBlock(base, Blocks.AIR.defaultBlockState(), UPDATE_ALL | UPDATE_SUPPRESS_DROPS);
                level.levelEvent(player, 2001, base, Block.getId(lower));
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    public static BlockPos bottom(BlockState state, BlockPos pos) {
        return state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
    }

    public static void setOpen(Level level, BlockPos pos, boolean open) {
        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof LockerBlock) || state.getValue(OPEN) == open) return;
        pos = bottom(state, pos);
        state = level.getBlockState(pos);
        level.setBlock(pos, state.setValue(OPEN, open), UPDATE_ALL);
        level.playSound(null, pos, open ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE,
                SoundSource.BLOCKS, 0.6F, 0.9F);
        level.gameEvent(null, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
    }

    @Override protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                                          Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockPos base = bottom(state, pos);
            if (shelves) {
                if (level.getBlockEntity(base) instanceof LockerBlockEntity locker) player.openMenu(locker);
            } else setOpen(level, base, !state.getValue(OPEN));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? new LockerBlockEntity(pos, state) : null;
    }

    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.LOCKER.get(),
                level.isClientSide ? LockerBlockEntity::clientTick : LockerBlockEntity::serverTick);
    }

    @Override protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getBlockEntity(pos) instanceof LockerBlockEntity locker) locker.recheckOpeners();
    }

    @Override protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState replacement, boolean moving) {
        if (!state.is(replacement.getBlock()) && level.getBlockEntity(pos) instanceof LockerBlockEntity locker) {
            Containers.dropContents(level, pos, locker);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, replacement, moving);
    }

    @Override protected RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int facing = switch (state.getValue(FACING)) { case EAST -> 1; case SOUTH -> 2; case WEST -> 3; default -> 0; };
        return shapes[state.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1][state.getValue(OPEN) ? 1 : 0][facing];
    }

    @Override protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation(state.getValue(FACING))); }
    @Override protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
}
