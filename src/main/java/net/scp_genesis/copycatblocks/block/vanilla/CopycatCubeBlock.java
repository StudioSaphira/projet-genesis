package net.scp_genesis.copycatblocks.block.vanilla;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.scp_genesis.copycatblocks.block.AbstractCopycatBlock;
import org.jetbrains.annotations.NotNull;

/**
 * Basic Copycat Cube Block.
 *
 * <p>This is the simplest Copycat Block implementation.
 * It supports camouflage but has no additional geometry
 * or wrench behavior.</p>
 */
public class CopycatCubeBlock extends AbstractCopycatBlock {

    public static final MapCodec<CopycatCubeBlock> CODEC = Block.simpleCodec(CopycatCubeBlock::new);

    public CopycatCubeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends CopycatCubeBlock> codec() {
        return CODEC;
    }
}