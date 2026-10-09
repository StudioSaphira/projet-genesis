package net.scp_genesis.common.scps.doors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import net.scp_genesis.common.registry.ModBlockEntities;

/** Two-block steel door whose front and rear leaves slide in opposite directions.
 * Vanilla DoorBlock owns placement, paired halves, redstone and destruction.
 */
public final class ScpSlidingDoorBlock extends DoorBlock implements EntityBlock {
    public static final MapCodec<ScpSlidingDoorBlock> CODEC = simpleCodec(ScpSlidingDoorBlock::new);

    public ScpSlidingDoorBlock() {
        this(Properties.of().strength(5.0F, 6.0F).sound(SoundType.METAL)
                .requiresCorrectToolForDrops().noOcclusion());
    }

    private ScpSlidingDoorBlock(Properties properties) { super(BlockSetType.IRON, properties); }

    @Override public MapCodec<ScpSlidingDoorBlock> codec() { return CODEC; }

    /** Faces the exterior plate toward the player placing the door. */
    @Override public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        return state == null ? null : state.setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                                         Player player, BlockHitResult hit) {
        if (!level.isClientSide) setOpen(player, level, state, pos, !state.getValue(OPEN));
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SlidingDoorShapes.selection(state);
    }

    @Override protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SlidingDoorShapes.collision(state);
    }

    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER ? new ScpSlidingDoorBlockEntity(pos, state) : null;
    }

    @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide && type == ModBlockEntities.SCP_SLIDING_DOOR.get()
                ? (world, pos, current, entity) -> ScpSlidingDoorBlockEntity.tick((ScpSlidingDoorBlockEntity) entity) : null;
    }
}
