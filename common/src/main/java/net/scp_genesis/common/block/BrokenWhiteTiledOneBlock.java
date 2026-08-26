package net.scp_genesis.common.block;

import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.Block;

public class BrokenWhiteTiledOneBlock extends Block {
	public BrokenWhiteTiledOneBlock() {
		super(BlockBehaviour.Properties.of().strength(1f, 10f).instrument(NoteBlockInstrument.BASEDRUM));
	}
}