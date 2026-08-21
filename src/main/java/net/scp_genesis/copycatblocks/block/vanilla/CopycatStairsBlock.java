package net.scp_genesis.copycatblocks.block.vanilla;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.scp_genesis.copycatblocks.block.AbstractCopycatBlock;
import org.jetbrains.annotations.NotNull;

public class CopycatStairsBlock extends AbstractCopycatBlock {

    public static final MapCodec<CopycatStairsBlock> CODEC =
            Block.simpleCodec(CopycatStairsBlock::new);

    private static StairsShape getStairsShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos
    ) {
        Direction direction =
                state.getValue(
                        BlockStateProperties.HORIZONTAL_FACING
                );

        BlockState frontState =
                level.getBlockState(
                        pos.relative(direction)
                );

        if (isStairs(frontState)
                && state.getValue(BlockStateProperties.HALF)
                == frontState.getValue(BlockStateProperties.HALF)) {

            Direction frontDirection =
                    frontState.getValue(
                            BlockStateProperties.HORIZONTAL_FACING
                    );

            if (frontDirection.getAxis()
                    != direction.getAxis()
                    && canTakeShape(
                    state,
                    level,
                    pos,
                    frontDirection.getOpposite()
            )) {

                if (frontDirection
                        == direction.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }

                return StairsShape.OUTER_RIGHT;
            }
        }

        BlockState backState =
                level.getBlockState(
                        pos.relative(direction.getOpposite())
                );

        if (isStairs(backState)
                && state.getValue(BlockStateProperties.HALF)
                == backState.getValue(BlockStateProperties.HALF)) {

            Direction backDirection =
                    backState.getValue(
                            BlockStateProperties.HORIZONTAL_FACING
                    );

            if (backDirection.getAxis()
                    != direction.getAxis()
                    && canTakeShape(
                    state,
                    level,
                    pos,
                    backDirection
            )) {

                if (backDirection
                        == direction.getCounterClockWise()) {
                    return StairsShape.INNER_LEFT;
                }

                return StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private static boolean canTakeShape(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            Direction direction
    ) {
        BlockState neighborState =
                level.getBlockState(
                        pos.relative(direction)
                );

        return !isStairs(neighborState)
                || neighborState.getValue(
                BlockStateProperties.HORIZONTAL_FACING
        ) != state.getValue(
                BlockStateProperties.HORIZONTAL_FACING
        )
                || neighborState.getValue(
                BlockStateProperties.HALF
        ) != state.getValue(
                BlockStateProperties.HALF
        );
    }

    private static boolean isStairs(BlockState state) {
        return state.getBlock() instanceof CopycatStairsBlock;
    }

    public CopycatStairsBlock(Properties properties) {
        super(properties);

        registerDefaultState(
                stateDefinition.any()
                        .setValue(
                                BlockStateProperties.HORIZONTAL_FACING,
                                Direction.NORTH
                        )
                        .setValue(
                                BlockStateProperties.HALF,
                                Half.BOTTOM
                        )
                        .setValue(
                                BlockStateProperties.STAIRS_SHAPE,
                                StairsShape.STRAIGHT
                        )
                        .setValue(
                                BlockStateProperties.WATERLOGGED,
                                false
                        )
        );
    }

    @Override
    protected void createBlockStateDefinition(
            @NotNull StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(
                BlockStateProperties.HORIZONTAL_FACING,
                BlockStateProperties.HALF,
                BlockStateProperties.STAIRS_SHAPE,
                BlockStateProperties.WATERLOGGED
        );
    }

    @Override
    public @NotNull BlockState getStateForPlacement(
            @NotNull BlockPlaceContext context
    ) {
        Direction direction = context.getClickedFace();
        BlockPos pos = context.getClickedPos();
        FluidState fluidState =
                context.getLevel().getFluidState(pos);

        BlockState state =
                defaultBlockState()
                        .setValue(
                                BlockStateProperties.HORIZONTAL_FACING,
                                context.getHorizontalDirection()
                        )
                        .setValue(
                                BlockStateProperties.HALF,
                                direction != Direction.DOWN
                                        && (
                                        direction == Direction.UP
                                                || !(context.getClickLocation().y - pos.getY() > 0.5D)
                                )
                                        ? Half.BOTTOM
                                        : Half.TOP
                        )
                        .setValue(
                                BlockStateProperties.WATERLOGGED,
                                fluidState.getType() == Fluids.WATER
                        );

        return state.setValue(
                BlockStateProperties.STAIRS_SHAPE,
                getStairsShape(
                        state,
                        context.getLevel(),
                        pos
                )
        );
    }

    @Override
    protected @NotNull BlockState updateShape(
            @NotNull BlockState state,
            @NotNull Direction direction,
            @NotNull BlockState neighborState,
            @NotNull net.minecraft.world.level.LevelAccessor level,
            @NotNull BlockPos pos,
            @NotNull BlockPos neighborPos
    ) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(
                    pos,
                    Fluids.WATER,
                    Fluids.WATER.getTickDelay(level)
            );
        }

        return direction.getAxis().isHorizontal()
                ? state.setValue(
                BlockStateProperties.STAIRS_SHAPE,
                getStairsShape(
                        state,
                        level,
                        pos
                )
        )
                : super.updateShape(
                state,
                direction,
                neighborState,
                level,
                pos,
                neighborPos
        );
    }

    private static final VoxelShape TOP_AABB =
            Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    private static final VoxelShape BOTTOM_AABB =
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    private static final VoxelShape OCTET_NNN =
            Block.box(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 8.0D);

    private static final VoxelShape OCTET_NNP =
            Block.box(0.0D, 0.0D, 8.0D, 8.0D, 8.0D, 16.0D);

    private static final VoxelShape OCTET_NPN =
            Block.box(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 8.0D);

    private static final VoxelShape OCTET_NPP =
            Block.box(0.0D, 8.0D, 8.0D, 8.0D, 16.0D, 16.0D);

    private static final VoxelShape OCTET_PNN =
            Block.box(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D);

    private static final VoxelShape OCTET_PNP =
            Block.box(8.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D);

    private static final VoxelShape OCTET_PPN =
            Block.box(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 8.0D);

    private static final VoxelShape OCTET_PPP =
            Block.box(8.0D, 8.0D, 8.0D, 16.0D, 16.0D, 16.0D);

    private static final VoxelShape[] TOP_SHAPES =
            makeShapes(
                    TOP_AABB,
                    OCTET_NNN,
                    OCTET_PNN,
                    OCTET_NNP,
                    OCTET_PNP
            );

    private static final VoxelShape[] BOTTOM_SHAPES =
            makeShapes(
                    BOTTOM_AABB,
                    OCTET_NPN,
                    OCTET_PPN,
                    OCTET_NPP,
                    OCTET_PPP
            );

    private static final int[] SHAPE_BY_STATE = {
            12, 5, 3, 10,
            14, 13, 7, 11,
            13, 7, 11, 14,
            8, 4, 1, 2,
            4, 1, 2, 8
    };

    private static VoxelShape[] makeShapes(
            VoxelShape base,
            VoxelShape octet1,
            VoxelShape octet2,
            VoxelShape octet3,
            VoxelShape octet4
    ) {
        VoxelShape[] shapes = new VoxelShape[16];

        for (int i = 0; i < 16; i++) {
            shapes[i] = makeStairShape(
                    i,
                    base,
                    octet1,
                    octet2,
                    octet3,
                    octet4
            );
        }

        return shapes;
    }

    private static VoxelShape makeStairShape(
            int index,
            VoxelShape base,
            VoxelShape octet1,
            VoxelShape octet2,
            VoxelShape octet3,
            VoxelShape octet4
    ) {
        VoxelShape shape = base;

        if ((index & 1) != 0) {
            shape = Shapes.or(shape, octet1);
        }

        if ((index & 2) != 0) {
            shape = Shapes.or(shape, octet2);
        }

        if ((index & 4) != 0) {
            shape = Shapes.or(shape, octet3);
        }

        if ((index & 8) != 0) {
            shape = Shapes.or(shape, octet4);
        }

        return shape;
    }

    @Override
    protected @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context
    ) {
        int index =
                state.getValue(
                        BlockStateProperties.STAIRS_SHAPE
                ).ordinal() * 4
                        + state.getValue(
                        BlockStateProperties.HORIZONTAL_FACING
                ).get2DDataValue();

        return (
                state.getValue(BlockStateProperties.HALF) == Half.TOP
                        ? TOP_SHAPES
                        : BOTTOM_SHAPES
        )[SHAPE_BY_STATE[index]];
    }

    @Override
    protected @NotNull MapCodec<? extends CopycatStairsBlock> codec() {
        return CODEC;
    }
}