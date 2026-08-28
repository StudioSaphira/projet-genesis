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

/**
 * A Copycat Block shaped as a 45-degree slope.
 *
 * <p>The slope has two vertical orientations:</p>
 *
 * <ul>
 *     <li>{@link Half#BOTTOM}: the slope occupies the lower half,
 *     with its high side at the back;</li>
 *     <li>{@link Half#TOP}: the vertical mirror of the bottom slope.</li>
 * </ul>
 *
 * <p>The horizontal orientation is controlled by {@link #FACING}.</p>
 *
 * <p>This class contains only platform-independent block logic and
 * Minecraft's common {@link VoxelShape} API. Rendering geometry and
 * UV calculations are handled separately by the Copycat geometry
 * system.</p>
 */
public class CopycatSlopeBlock extends AbstractCopycatBlock {

    public static final MapCodec<CopycatSlopeBlock> CODEC =
            Block.simpleCodec(CopycatSlopeBlock::new);

    /**
     * Horizontal direction toward which the slope faces.
     */
    public static final DirectionProperty FACING =
            BlockStateProperties.HORIZONTAL_FACING;

    /**
     * Vertical orientation of the slope.
     */
    public static final EnumProperty<Half> HALF =
            BlockStateProperties.HALF;

    /*
     * ================================================================
     * COLLISION / SELECTION SHAPES
     * ================================================================
     *
     * These shapes intentionally use four horizontal steps.
     *
     * This follows the same principle as FramedBlocks' SlopeShapes:
     * the physical VoxelShape is an approximation of the diagonal,
     * while the visual geometry is handled independently.
     *
     * Each step is 4 blocks high and 4 blocks deep.
     */

    private static final VoxelShape NORTH_BOTTOM_SHAPE =
            createBottomShape(Direction.NORTH);

    private static final VoxelShape EAST_BOTTOM_SHAPE =
            createBottomShape(Direction.EAST);

    private static final VoxelShape SOUTH_BOTTOM_SHAPE =
            createBottomShape(Direction.SOUTH);

    private static final VoxelShape WEST_BOTTOM_SHAPE =
            createBottomShape(Direction.WEST);

    private static final VoxelShape NORTH_TOP_SHAPE =
            createTopShape(Direction.NORTH);

    private static final VoxelShape EAST_TOP_SHAPE =
            createTopShape(Direction.EAST);

    private static final VoxelShape SOUTH_TOP_SHAPE =
            createTopShape(Direction.SOUTH);

    private static final VoxelShape WEST_TOP_SHAPE =
            createTopShape(Direction.WEST);

    /*
     * ================================================================
     * OCCLUSION SHAPES
     * ================================================================
     *
     * These are kept separate from the collision/selection shapes.
     *
     * They are intentionally slightly inset at the extreme ends,
     * following the same idea as FramedBlocks' OCCLUSION_SHAPES.
     *
     * This prevents overly aggressive face occlusion at the diagonal
     * boundary while still allowing Minecraft to perform normal
     * block-face culling.
     */

    private static final VoxelShape NORTH_BOTTOM_OCCLUSION =
            createBottomOcclusionShape(Direction.NORTH);

    private static final VoxelShape EAST_BOTTOM_OCCLUSION =
            createBottomOcclusionShape(Direction.EAST);

    private static final VoxelShape SOUTH_BOTTOM_OCCLUSION =
            createBottomOcclusionShape(Direction.SOUTH);

    private static final VoxelShape WEST_BOTTOM_OCCLUSION =
            createBottomOcclusionShape(Direction.WEST);

    private static final VoxelShape NORTH_TOP_OCCLUSION =
            createTopOcclusionShape(Direction.NORTH);

    private static final VoxelShape EAST_TOP_OCCLUSION =
            createTopOcclusionShape(Direction.EAST);

    private static final VoxelShape SOUTH_TOP_OCCLUSION =
            createTopOcclusionShape(Direction.SOUTH);

    private static final VoxelShape WEST_TOP_OCCLUSION =
            createTopOcclusionShape(Direction.WEST);

    /*
     * ================================================================
     * CONSTRUCTOR
     * ================================================================
     */

    public CopycatSlopeBlock(
            BlockBehaviour.Properties properties
    ) {
        super(properties);

        registerDefaultState(
                stateDefinition.any()
                        .setValue(
                                FACING,
                                Direction.NORTH
                        )
                        .setValue(
                                HALF,
                                Half.BOTTOM
                        )
        );
    }

    /*
     * ================================================================
     * BLOCK STATES
     * ================================================================
     */

    @Override
    protected void createBlockStateDefinition(
            StateDefinition.Builder<Block, BlockState> builder
    ) {
        builder.add(
                FACING,
                HALF
        );
    }

    /*
     * ================================================================
     * COLLISION / SELECTION
     * ================================================================
     */

    /**
     * Creates the four-step approximation of a bottom slope.
     *
     * <p>The canonical shape faces NORTH.</p>
     *
     * <p>For the canonical orientation:</p>
     *
     * <pre>
     * height 16 ┌──────────────┐
     *           │              │
     * height 12 └──────────┐   │
     *                      │   │
     * height  8 └──────┐   │   │
     *                   │   │   │
     * height  4 └──┐   │   │   │
     *               │   │   │   │
     * height  0 ────┴───┴───┴───┴
     *              16  12   8   4
     * </pre>
     */
    private static VoxelShape createBottomShape(
            @NotNull Direction facing
    ) {
        VoxelShape shape =
                Shapes.empty();

        /*
         * Four 4x4x4 steps.
         *
         * The canonical NORTH slope is:
         *
         *   Z 0..4   -> Y 0..16
         *   Z 4..8   -> Y 0..12
         *   Z 8..12  -> Y 0..8
         *   Z 12..16 -> Y 0..4
         */
        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                0.0D,
                                4.0D,
                                0.0D,
                                16.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                4.0D,
                                8.0D,
                                0.0D,
                                12.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                8.0D,
                                12.0D,
                                0.0D,
                                8.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                12.0D,
                                16.0D,
                                0.0D,
                                4.0D
                        )
                );

        return shape;
    }

    /**
     * Creates the vertical mirror of the bottom slope.
     */
    private static VoxelShape createTopShape(
            @NotNull Direction facing
    ) {
        VoxelShape shape =
                Shapes.empty();

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                0.0D,
                                4.0D,
                                0.0D,
                                4.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                4.0D,
                                8.0D,
                                0.0D,
                                8.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                8.0D,
                                12.0D,
                                0.0D,
                                12.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                12.0D,
                                16.0D,
                                0.0D,
                                16.0D
                        )
                );

        return shape;
    }

    /**
     * Creates a slice of a slope in the canonical NORTH coordinate
     * system and rotates its horizontal coordinates according to
     * {@code facing}.
     */
    private static VoxelShape createSlice(
            @NotNull Direction facing,
            double zMin,
            double zMax,
            double yMin,
            double yMax
    ) {
        return switch (facing) {

            case NORTH ->
                    Block.box(
                            0.0D,
                            yMin,
                            zMin,
                            16.0D,
                            yMax,
                            zMax
                    );

            case EAST ->
                    Block.box(
                            16.0D - zMax,
                            yMin,
                            0.0D,
                            16.0D - zMin,
                            yMax,
                            16.0D
                    );

            case SOUTH ->
                    Block.box(
                            0.0D,
                            yMin,
                            16.0D - zMax,
                            16.0D,
                            yMax,
                            16.0D - zMin
                    );

            case WEST ->
                    Block.box(
                            zMin,
                            yMin,
                            0.0D,
                            zMax,
                            yMax,
                            16.0D
                    );

            default ->
                    throw new IllegalArgumentException(
                            "Copycat Slope facing must be horizontal: "
                                    + facing
                    );
        };
    }

    /*
     * ================================================================
     * OCCLUSION SHAPES
     * ================================================================
     */

    /**
     * Creates the occlusion shape for a bottom slope.
     *
     * <p>The end caps are slightly inset so that the diagonal slope
     * does not incorrectly behave like a full cube for face culling.</p>
     */
    private static VoxelShape createBottomOcclusionShape(
            @NotNull Direction facing
    ) {
        VoxelShape shape =
                Shapes.empty();

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                0.0D,
                                0.5D,
                                0.0D,
                                0.5D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                0.5D,
                                4.0D,
                                0.0D,
                                4.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                4.0D,
                                8.0D,
                                0.0D,
                                8.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                8.0D,
                                12.0D,
                                0.0D,
                                12.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                12.0D,
                                15.5D,
                                0.0D,
                                15.5D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                15.5D,
                                16.0D,
                                0.0D,
                                16.0D
                        )
                );

        return shape;
    }

    /**
     * Creates the occlusion shape for a top slope.
     */
    private static VoxelShape createTopOcclusionShape(
            @NotNull Direction facing
    ) {
        VoxelShape shape =
                Shapes.empty();

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                0.0D,
                                0.5D,
                                15.5D,
                                16.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                0.5D,
                                4.0D,
                                12.0D,
                                16.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                4.0D,
                                8.0D,
                                8.0D,
                                16.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                8.0D,
                                12.0D,
                                4.0D,
                                16.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                12.0D,
                                15.5D,
                                0.5D,
                                16.0D
                        )
                );

        shape =
                Shapes.or(
                        shape,
                        createSlice(
                                facing,
                                15.5D,
                                16.0D,
                                0.0D,
                                16.0D
                        )
                );

        return shape;
    }

    /**
     * Returns the collision/selection shape.
     *
     * <p>This shape is deliberately the four-step approximation,
     * not the exact rendered diagonal.</p>
     */
    @Override
    protected @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context
    ) {
        Direction facing =
                state.getValue(FACING);

        Half half =
                state.getValue(HALF);

        if (half == Half.TOP) {
            return switch (facing) {
                case NORTH -> NORTH_TOP_SHAPE;
                case EAST -> EAST_TOP_SHAPE;
                case SOUTH -> SOUTH_TOP_SHAPE;
                case WEST -> WEST_TOP_SHAPE;

                case UP, DOWN ->
                        throw new IllegalStateException(
                                "Copycat Slope FACING must be horizontal: "
                                        + facing
                        );
            };
        }

        return switch (facing) {
            case NORTH -> NORTH_BOTTOM_SHAPE;
            case EAST -> EAST_BOTTOM_SHAPE;
            case SOUTH -> SOUTH_BOTTOM_SHAPE;
            case WEST -> WEST_BOTTOM_SHAPE;

            case UP, DOWN ->
                    throw new IllegalStateException(
                            "Copycat Slope FACING must be horizontal: "
                                    + facing
                    );
        };
    }

    /**
     * Returns the dedicated occlusion shape.
     *
     * <p>This is deliberately separate from {@link #getShape} so that
     * collision/selection and face occlusion can evolve independently.</p>
     */
    @Override
    protected @NotNull VoxelShape getOcclusionShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos
    ) {
        Direction facing =
                state.getValue(FACING);

        Half half =
                state.getValue(HALF);

        if (half == Half.TOP) {
            return switch (facing) {
                case NORTH -> NORTH_TOP_OCCLUSION;
                case EAST -> EAST_TOP_OCCLUSION;
                case SOUTH -> SOUTH_TOP_OCCLUSION;
                case WEST -> WEST_TOP_OCCLUSION;

                case UP, DOWN ->
                        throw new IllegalStateException(
                                "Copycat Slope FACING must be horizontal: "
                                        + facing
                        );
            };
        }

        return switch (facing) {
            case NORTH -> NORTH_BOTTOM_OCCLUSION;
            case EAST -> EAST_BOTTOM_OCCLUSION;
            case SOUTH -> SOUTH_BOTTOM_OCCLUSION;
            case WEST -> WEST_BOTTOM_OCCLUSION;

            case UP, DOWN ->
                    throw new IllegalStateException(
                            "Copycat Slope FACING must be horizontal: "
                                    + facing
                    );
        };
    }

    /*
     * ================================================================
     * PLACEMENT
     * ================================================================
     */

    @Override
    public BlockState getStateForPlacement(
            @NotNull BlockPlaceContext context
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
                .setValue(
                        FACING,
                        facing
                )
                .setValue(
                        HALF,
                        half
                );
    }

    /*
     * ================================================================
     * WRENCH
     * ================================================================
     */

    /**
     * Indicates that this slope supports the Copycat Wrench.
     */
    @Override
    public boolean isWrenchable() {
        return true;
    }

    /**
     * Rotates the slope clockwise by 90 degrees.
     *
     * <p>The wrench only changes the horizontal FACING. The vertical
     * orientation represented by {@link #HALF} is preserved.</p>
     */
    @Override
    public net.minecraft.world.InteractionResult onWrench(
            @NotNull net.minecraft.world.level.Level level,
            @NotNull BlockPos pos,
            @NotNull BlockState state
    ) {
        Direction facing =
                state.getValue(FACING);

        Direction newFacing =
                facing.getClockWise();

        BlockState newState =
                state.setValue(
                        FACING,
                        newFacing
                );

        if (!level.isClientSide()) {
            level.setBlock(
                    pos,
                    newState,
                    Block.UPDATE_ALL
            );
        }

        return net.minecraft.world.InteractionResult.SUCCESS;
    }

    /*
     * ================================================================
     * CODEC
     * ================================================================
     */

    @Override
    protected @NotNull MapCodec<? extends CopycatSlopeBlock> codec() {
        return CODEC;
    }
}