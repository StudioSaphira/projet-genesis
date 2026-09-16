package net.scp_genesis.common.furnitures.notmodular.room;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.*;
import net.scp_genesis.common.furnitures.storage.*;

public class WoodenDrawerBlock extends BaseEntityBlock {
    public static final MapCodec<WoodenDrawerBlock> CODEC = simpleCodec(WoodenDrawerBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape[] SHAPES = createShapes();

    protected WoodenDrawerBlock(Block wood) { this(Properties.ofFullCopy(wood).noOcclusion()); }
    protected WoodenDrawerBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    @Override protected MapCodec<? extends WoodenDrawerBlock> codec() { return CODEC; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(FACING); }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new DrawerBlockEntity(pos, state); }
    @Override protected RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }
    @Override protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!hitsDrawerFront(state, pos, hit)) return InteractionResult.PASS;
        if (!level.isClientSide && level.getBlockEntity(pos) instanceof DrawerBlockEntity drawer) {
            // The model's drawer fronts meet at y=8/16, independently of horizontal rotation.
            boolean upper = hit.getLocation().y - pos.getY() >= 0.5;
            player.openMenu(new SimpleMenuProvider((id, inventory, user) ->
                    new DrawerMenu(id, inventory, drawer.compartment(upper)),
                    Component.translatable(upper ? "container.scp_genesis.drawer.upper" : "container.scp_genesis.drawer.lower")));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static boolean hitsDrawerFront(BlockState state, BlockPos pos, BlockHitResult hit) {
        Direction facing = state.getValue(FACING);
        if (hit.isInside() || hit.getDirection() != facing) return false;
        double x = hit.getLocation().x - pos.getX();
        double y = hit.getLocation().y - pos.getY();
        double z = hit.getLocation().z - pos.getZ();
        // Inverse of the model's horizontal rotation, back to its north-facing coordinates.
        double localX = switch (facing) { case EAST -> z; case SOUTH -> 1-x; case WEST -> 1-z; default -> x; };
        double localZ = switch (facing) { case EAST -> 1-x; case SOUTH -> 1-z; case WEST -> x; default -> z; };
        // Drawer panels: x=2..14, y=3..13, front at z=1 (model pixels).
        boolean panel = localX > 2.0/16 && localX < 14.0/16
                && y > 3.0/16 && y < 13.0/16 && Math.abs(localZ - 1.0/16) < 1.0e-5;
        // Handles protrude one pixel in front of the panels.
        boolean handle = localX >= 6.0/16 && localX <= 10.0/16 && Math.abs(localZ) < 1.0e-5
                && ((y >= 5.0/16 && y <= 6.0/16) || (y >= 10.0/16 && y <= 11.0/16));
        return panel || handle;
    }
    @Override protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState replacement, boolean moving) {
        if (!state.is(replacement.getBlock()) && level.getBlockEntity(pos) instanceof DrawerBlockEntity drawer) {
            Containers.dropContents(level, pos, drawer);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, level, pos, replacement, moving);
    }
    @Override protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[switch (state.getValue(FACING)) { case EAST -> 1; case SOUTH -> 2; case WEST -> 3; default -> 0; }];
    }
    @Override protected BlockState rotate(BlockState state, Rotation rotation) { return state.setValue(FACING, rotation.rotate(state.getValue(FACING))); }
    @Override protected BlockState mirror(BlockState state, Mirror mirror) { return state.rotate(mirror.getRotation(state.getValue(FACING))); }
    @Override protected boolean isPathfindable(BlockState state, PathComputationType type) { return false; }
    private static VoxelShape[] createShapes() {
        VoxelShape shape = Shapes.or(box(0,14,0,16,16,16), box(1,2,1,15,3,15),
                box(1,3,1,2,14,15), box(14,3,1,15,14,15), box(2,3,14,14,14,15),
                box(2,3,1,14,14,2), box(6,5,0,10,6,1), box(6,10,0,10,11,1),
                box(1,0,1,3,2,3), box(13,0,1,15,2,3), box(1,0,13,3,2,15), box(13,0,13,15,2,15));
        VoxelShape[] result = new VoxelShape[4];
        for (int i=0;i<4;i++) {
            result[i]=shape.optimize();
            VoxelShape rotated=Shapes.empty();
            for (var b:shape.toAabbs()) rotated=Shapes.or(rotated,Shapes.box(1-b.maxZ,b.minY,b.minX,1-b.minZ,b.maxY,b.maxX));
            shape=rotated;
        }
        return result;
    }
}
