package net.scp_genesis.common.copycatblocks.block.custom;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;

import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.scp_genesis.common.copycatblocks.block.AbstractCopycatBlock;

import org.jetbrains.annotations.NotNull;

public class CopycatSlopeBlock extends AbstractCopycatBlock {

    public static final MapCodec<CopycatSlopeBlock> CODEC =
            Block.simpleCodec(CopycatSlopeBlock::new);

    public static final DirectionProperty FACING =
            BlockStateProperties.HORIZONTAL_FACING;

    public static final EnumProperty<Half> HALF =
            BlockStateProperties.HALF;

    public CopycatSlopeBlock(
            BlockBehaviour.Properties properties
    ) {
        super(properties);

        registerDefaultState(
                stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(HALF, Half.BOTTOM)
        );
    }

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(
                FACING,
                HALF
        );
    }

    private static final int SHAPE_RESOLUTION = 4;

    private static final VoxelShape[] BOTTOM_SHAPES =
            createBottomShapes();

    private static final VoxelShape[] TOP_SHAPES =
            createTopShapes();

    private static VoxelShape createSlice(
            Direction facing,
            double zMin,
            double zMax,
            double yMin,
            double yMax
    ) {
        return switch (facing) {

            case NORTH ->
                    Block.box(
                            0.0D,
                            yMin * 16.0D,
                            zMin * 16.0D,
                            16.0D,
                            yMax * 16.0D,
                            zMax * 16.0D
                    );

            case EAST ->
                    Block.box(
                            (1.0D - zMax) * 16.0D,
                            yMin * 16.0D,
                            0.0D,
                            (1.0D - zMin) * 16.0D,
                            yMax * 16.0D,
                            16.0D
                    );

            case SOUTH ->
                    Block.box(
                            0.0D,
                            yMin * 16.0D,
                            (1.0D - zMax) * 16.0D,
                            16.0D,
                            yMax * 16.0D,
                            (1.0D - zMin) * 16.0D
                    );

            case WEST ->
                    Block.box(
                            zMin * 16.0D,
                            yMin * 16.0D,
                            0.0D,
                            zMax * 16.0D,
                            yMax * 16.0D,
                            16.0D
                    );

            default ->
                    throw new IllegalArgumentException(
                            "Copycat Slope facing must be horizontal: "
                                    + facing
                    );
        };
    }

    private static VoxelShape[] createBottomShapes() {
        VoxelShape[] shapes = new VoxelShape[4];

        for (int facingIndex = 0; facingIndex < 4; facingIndex++) {

            Direction facing =
                    Direction.from2DDataValue(facingIndex);

            VoxelShape shape =
                    Shapes.empty();

            for (int i = 0; i < SHAPE_RESOLUTION; i++) {

                double zMin =
                        (double) i / SHAPE_RESOLUTION;

                double zMax =
                        (double) (i + 1) / SHAPE_RESOLUTION;

                shape =
                        Shapes.or(
                                shape,
                                createSlice(
                                        facing,
                                        zMin,
                                        zMax,
                                        0.0D,
                                        zMax
                                )
                        );
            }

            shapes[facing.get2DDataValue()] =
                    shape;
        }

        return shapes;
    }

    private static VoxelShape[] createTopShapes() {
        VoxelShape[] shapes = new VoxelShape[4];

        for (int facingIndex = 0; facingIndex < 4; facingIndex++) {

            Direction facing =
                    Direction.from2DDataValue(facingIndex);

            VoxelShape shape =
                    Shapes.empty();

            for (int i = 0; i < SHAPE_RESOLUTION; i++) {

                double zMin =
                        (double) i / SHAPE_RESOLUTION;

                double zMax =
                        (double) (i + 1) / SHAPE_RESOLUTION;

                double minHeight =
                        1.0D - zMax;

                shape =
                        Shapes.or(
                                shape,
                                createSlice(
                                        facing,
                                        zMin,
                                        zMax,
                                        minHeight,
                                        1.0D
                                )
                        );
            }

            shapes[facing.get2DDataValue()] =
                    shape;
        }

        return shapes;
    }

    @Override
    protected @NotNull VoxelShape getShape(
            BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context
    ) {
        Direction facing =
                state.getValue(FACING);

        Half half =
                state.getValue(HALF);

        return half == Half.TOP
                ? TOP_SHAPES[facing.get2DDataValue()]
                : BOTTOM_SHAPES[facing.get2DDataValue()];
    }

    @Override
    public BlockState getStateForPlacement(
            BlockPlaceContext context
    ) {
        Direction facing =
                context.getHorizontalDirection();

        Half half =
                context.getClickLocation().y
                        - context.getClickedPos().getY()
                        >= 0.5D
                        ? Half.TOP
                        : Half.BOTTOM;

        return defaultBlockState()
                .setValue(FACING, facing)
                .setValue(HALF, half);
    }

    @Override
    protected @NotNull MapCodec<? extends CopycatSlopeBlock> codec() {
        return CODEC;
    }
}