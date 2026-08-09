package net.scp_genesis.copycatblocks.block.vanilla;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.scp_genesis.copycatblocks.block.AbstractCopycatBlock;

/**
 * Basic Copycat Cube Block.
 *
 * <p>This is the simplest Copycat Block implementation.
 * It supports camouflage but has no additional geometry
 * or wrench behavior.</p>
 */
public class CopycatCubeBlock extends AbstractCopycatBlock {

    protected CopycatCubeBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public CopycatCubeBlock() {
        this(BlockBehaviour.Properties.of()
                .strength(2.0F, 6.0F)
                .sound(SoundType.STONE)
                .requiresCorrectToolForDrops());
    }

}