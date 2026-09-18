package net.scp_genesis.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BareConcreteABlock extends Block {
    public BareConcreteABlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE));
    }
}
