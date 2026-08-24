package net.scp_genesis.common.copycatblocks.block.custom;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;

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