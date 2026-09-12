package net.scp_genesis.common.furnitures.seating;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.AABB;
import net.scp_genesis.common.registry.ModEntities;
import org.jetbrains.annotations.NotNull;

public abstract class ChairBlock extends Block {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private final double seatHeight;

    protected ChairBlock(Properties properties, double seatHeight) {
        super(properties);
        this.seatHeight = seatHeight;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos,
                                                        Player player, BlockHitResult hit) {
        if (player.isShiftKeyDown() || player.isPassenger() || player.isSpectator()) return InteractionResult.PASS;
        if (level.isClientSide) return InteractionResult.SUCCESS;
        for (ChairSeatEntity seat : level.getEntitiesOfClass(ChairSeatEntity.class, new AABB(pos))) {
            if (seat.isVehicle()) return InteractionResult.CONSUME;
            seat.discard();
        }
        if (!level.getBlockState(pos.above()).getCollisionShape(level, pos.above()).isEmpty()) {
            return InteractionResult.CONSUME;
        }
        ChairSeatEntity seat = ModEntities.CHAIR_SEAT.get().create(level);
        if (seat == null) return InteractionResult.PASS;
        seat.setPos(pos.getX() + 0.5, pos.getY() + seatHeight, pos.getZ() + 0.5);
        seat.setYRot(state.getValue(FACING).toYRot());
        if (level.addFreshEntity(seat)) {
            if (!player.startRiding(seat)) seat.discard();
        }
        return InteractionResult.CONSUME;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState replacement, boolean moving) {
        if (!level.isClientSide && state.getBlock() != replacement.getBlock()) {
            for (ChairSeatEntity seat : level.getEntitiesOfClass(ChairSeatEntity.class, new AABB(pos))) {
                seat.ejectPassengers();
                seat.discard();
            }
        }
        super.onRemove(state, level, pos, replacement, moving);
    }
}
