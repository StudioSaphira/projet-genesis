package net.scp_genesis.common.scps.readers;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import net.scp_genesis.common.registry.ModBlockEntities;
import org.jetbrains.annotations.NotNull;

/** One wall-mounted block/item, with appearance selected by its server configuration. */
public final class ReaderBlock extends BaseEntityBlock {
    public static final MapCodec<ReaderBlock> CODEC = simpleCodec(ReaderBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<ReaderType> TYPE = EnumProperty.create("type", ReaderType.class);
    public static final EnumProperty<ReaderLevel> LEVEL = EnumProperty.create("level", ReaderLevel.class);
    public static final EnumProperty<ReaderStatus> STATUS = EnumProperty.create("status", ReaderStatus.class);
    private static final VoxelShape[] SHAPES = {box(4.5,2,13,11.5,14,16), box(0,2,4.5,3,14,11.5),
            box(4.5,2,0,11.5,14,3), box(13,2,4.5,16,14,11.5)};
    public ReaderBlock() { this(Properties.of().strength(2.5F).sound(SoundType.METAL).noOcclusion()); }
    private ReaderBlock(Properties properties) {
        super(properties); registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(TYPE, ReaderType.KEYCARD).setValue(LEVEL, ReaderLevel.ZERO).setValue(STATUS, ReaderStatus.BASE));
    }
    @Override protected @NotNull MapCodec<ReaderBlock> codec() { return CODEC; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(FACING, TYPE, LEVEL, STATUS); }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        var face = context.getClickedFace();
        if (!face.getAxis().isHorizontal()) return null;
        var state = defaultBlockState().setValue(FACING, face);
        return state.canSurvive(context.getLevel(), context.getClickedPos()) ? state : null;
    }
    @Override protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        var facing = state.getValue(FACING); var support = pos.relative(facing.getOpposite());
        return level.getBlockState(support).isFaceSturdy(level, support, facing);
    }
    @Override protected @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return !state.canSurvive(level,pos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state,direction,neighbor,level,pos,neighborPos);
    }
    @Override protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[switch(state.getValue(FACING)) {case EAST -> 1; case SOUTH -> 2; case WEST -> 3; default -> 0;}];
    }
    @Override protected @NotNull RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new ReaderBlockEntity(pos,state); }
    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type,ModBlockEntities.READER.get(),ReaderBlockEntity::serverTick);
    }
    @Override protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof ReaderConfiguratorItem) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (hand == InteractionHand.OFF_HAND) return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        interact(level,pos,player,stack); return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }
    @Override protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (ReaderConfiguratorItem.isHeld(player)) return InteractionResult.PASS;
        interact(level,pos,player,ItemStack.EMPTY); return InteractionResult.sidedSuccess(level.isClientSide);
    }
    private static void interact(Level level, BlockPos pos, Player player, ItemStack stack) {
        if (player instanceof ServerPlayer server && level.getBlockEntity(pos) instanceof ReaderBlockEntity reader)
            ReaderBehaviors.get(reader.configuration().type()).use(reader,server,stack);
    }
    @Override protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState replacement, boolean moving) {
        if (!state.is(replacement.getBlock()) && level.getBlockEntity(pos) instanceof ReaderBlockEntity reader) reader.destroy();
        super.onRemove(state,level,pos,replacement,moving);
    }
    @Override protected @NotNull BlockState rotate(BlockState state, Rotation rotation) { return state.setValue(FACING,rotation.rotate(state.getValue(FACING))); }
    @Override protected @NotNull BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation(state.getValue(FACING))); }
}
