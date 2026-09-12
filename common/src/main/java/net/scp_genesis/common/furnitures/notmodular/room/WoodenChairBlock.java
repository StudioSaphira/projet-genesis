package net.scp_genesis.common.furnitures.notmodular.room;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.*;
import net.scp_genesis.common.furnitures.seating.ChairBlock;
import org.jetbrains.annotations.NotNull;

public class WoodenChairBlock extends ChairBlock {
    private static final VoxelShape BASE = Shapes.or(
            box(0, 7, 0, 16, 9, 16),
            box(1, 0, 1, 3, 7, 3),
            box(13, 0, 1, 15, 7, 3),
            box(1, 0, 13, 3, 7, 15),
            box(13, 0, 13, 15, 7, 15));
    private static final VoxelShape NORTH = Shapes.or(BASE, box(0, 9, 14, 16, 23, 16));
    private static final VoxelShape EAST = Shapes.or(BASE, box(0, 9, 0, 2, 23, 16));
    private static final VoxelShape SOUTH = Shapes.or(BASE, box(0, 9, 0, 16, 23, 2));
    private static final VoxelShape WEST = Shapes.or(BASE, box(14, 9, 0, 16, 23, 16));

    protected WoodenChairBlock(Block planks) {
        super(Properties.ofFullCopy(planks).noOcclusion(), 9.0 / 16.0);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> NORTH;
        };
    }

}
