package net.scp_genesis.common.furnitures.notmodular.office;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.scp_genesis.common.furnitures.seating.ChairBlock;
import org.jetbrains.annotations.NotNull;

public class FurnitureOfficeChair extends ChairBlock {
    private static final VoxelShape NORTH = Shapes.or(
            // Simplified envelope for the five angled legs and their casters.
            box(3.5, 0, 3.5, 12.5, 4, 12.5),
            box(7.25, 1.5, 7.25, 8.75, 4, 8.75),
            box(7.5, 2.5, 7.5, 8.5, 6, 8.5),
            box(7, 5.5, 7, 9, 7, 9),
            box(4, 6.25, 4, 12, 7, 12),
            box(2.5, 6.925, 2.5, 13.5, 8, 13.5),
            box(2, 7, 2, 14, 9, 14),
            box(3, 8, 3, 13, 9.2, 13),
            box(7, 9, 12.25, 9, 13, 13.75),
            box(1.5, 13.5, 11.9, 14.5, 22.5, 12.4),
            box(1, 13, 12, 15, 23, 14)
    );
    private static final VoxelShape EAST = rotateClockwise(NORTH);
    private static final VoxelShape SOUTH = rotateClockwise(EAST);
    private static final VoxelShape WEST = rotateClockwise(SOUTH);

    public FurnitureOfficeChair() {
        super(Properties.ofFullCopy(Blocks.IRON_BLOCK).strength(2.0F, 6.0F).noOcclusion(), 9.2 / 16.0);
    }

    private static VoxelShape rotateClockwise(VoxelShape shape) {
        VoxelShape result = Shapes.empty();
        for (var bounds : shape.toAabbs()) {
            result = Shapes.or(result, Shapes.box(1 - bounds.maxZ, bounds.minY, bounds.minX,
                    1 - bounds.minZ, bounds.maxY, bounds.maxX));
        }
        return result;
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos,
                                           CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST -> EAST;
            case SOUTH -> SOUTH;
            case WEST -> WEST;
            default -> NORTH;
        };
    }
}
