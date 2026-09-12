package net.scp_genesis.common.furnitures.notmodular.room;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class WoodenTableBlock extends Block {
    // Matches the nine elements in assets/scp_genesis/models/custom/table.json.
    private static final VoxelShape SHAPE = Shapes.or(
            box(0, 14, 0, 16, 16, 16),
            box(1, 0, 1, 3, 14, 3),
            box(1, 0, 13, 3, 14, 15),
            box(13, 0, 1, 15, 14, 3),
            box(13, 0, 13, 15, 14, 15),
            box(1, 12.5, 3, 3, 14, 13),
            box(13, 12.5, 3, 15, 14, 13),
            box(3, 12.5, 1, 13, 14, 3),
            box(3, 12.5, 13, 13, 14, 15)
    );

    protected WoodenTableBlock(Block planks) {
        super(Properties.ofFullCopy(planks).noOcclusion());
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
                                           CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }
}
