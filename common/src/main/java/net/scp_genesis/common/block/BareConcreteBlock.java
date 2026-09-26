package net.scp_genesis.common.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BareConcreteBlock extends Block {
    public BareConcreteBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_CONCRETE));
    }
}
