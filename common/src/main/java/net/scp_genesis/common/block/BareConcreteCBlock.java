package net.scp_genesis.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BareConcreteCBlock extends Block {
    public BareConcreteCBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE));
    }
}
