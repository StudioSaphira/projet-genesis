package net.scp_genesis.common.furnitures.notmodular.room.mangrove;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class FurnitureTableMangrove extends Block {

    // Same dimensions as assets/scp_genesis/models/custom/table.json.
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(0, 13, 0, 16, 16, 16),
            Block.box(0, 0, 0, 3, 13, 3),
            Block.box(0, 0, 13, 3, 13, 16),
            Block.box(13, 0, 0, 16, 13, 3),
            Block.box(13, 0, 13, 16, 13, 16)
    );

    public FurnitureTableMangrove() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PLANKS).noOcclusion());
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
