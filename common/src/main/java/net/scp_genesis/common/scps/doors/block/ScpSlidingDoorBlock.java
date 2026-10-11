package net.scp_genesis.common.scps.doors.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.scp_genesis.common.registry.ModBlockEntities;
import net.scp_genesis.common.scps.doors.blockentity.ScpSlidingDoorBlockEntity;
import net.scp_genesis.common.scps.doors.util.SlidingDoorShapes;
import net.scp_genesis.common.scps.readers.item.ReaderConfiguratorItem;
import net.scp_genesis.common.scps.readers.storage.ReaderDirectory;
import net.scp_genesis.common.scps.readers.util.ReaderDoorController;
import org.jetbrains.annotations.NotNull;

/**
 * Two-block steel door whose front and rear leaves slide in opposite directions. Vanilla DoorBlock
 * owns placement, paired halves, redstone and destruction.
 */
public final class ScpSlidingDoorBlock extends DoorBlock implements EntityBlock {

    public static final MapCodec<ScpSlidingDoorBlock> CODEC = simpleCodec(ScpSlidingDoorBlock::new);

    public ScpSlidingDoorBlock() {
        this(
                Properties.of()
                        .strength(5.0F, 6.0F)
                        .sound(SoundType.METAL)
                        .requiresCorrectToolForDrops()
                        .noOcclusion());
    }

    private ScpSlidingDoorBlock(Properties properties) {
        super(BlockSetType.IRON, properties);
    }

    @Override
    public @NotNull MapCodec<ScpSlidingDoorBlock> codec() {
        return CODEC;
    }

    /** Faces the exterior plate toward the player placing the door. */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);

        return state == null
                ? null
                : state.setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(
            BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (ReaderConfiguratorItem.isHeld(player)) {
            return InteractionResult.PASS;
        }

        if (level instanceof ServerLevel server) {
            BlockPos base = state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;

            if (ReaderDirectory.get(server).controls(base)) {
                return InteractionResult.SUCCESS;
            }

            setOpen(player, level, state, pos, !state.getValue(OPEN));
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /** Linked doors accept reader commands rather than direct redstone bypasses. */
    @Override
    protected void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block source,
            BlockPos sourcePos,
            boolean moving) {
        BlockPos base = state.getValue(HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;

        if (level instanceof ServerLevel server && ReaderDirectory.get(server).controls(base)) {
            return;
        }

        super.neighborChanged(state, level, pos, source, sourcePos, moving);
    }

    @Override
    protected @NotNull VoxelShape getShape(
            BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SlidingDoorShapes.selection(state);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(
            BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SlidingDoorShapes.collision(state);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER
                ? new ScpSlidingDoorBlockEntity(pos, state)
                : null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {
        if (type != ModBlockEntities.SCP_SLIDING_DOOR.get()) {
            return null;
        }

        return (world, pos, current, entity) -> {
            if (world.isClientSide) {
                ScpSlidingDoorBlockEntity.tick((ScpSlidingDoorBlockEntity) entity);
            } else if (world instanceof ServerLevel server
                    && ReaderDirectory.get(server).controls(pos)) {
                ReaderDoorController.refresh(server, pos);
            }
        };
    }
}
