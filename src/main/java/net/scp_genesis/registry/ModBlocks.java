package net.scp_genesis.registry;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

import net.scp_genesis.constants.ModConstants;
import net.scp_genesis.block.*;
import net.scp_genesis.copycatblocks.block.vanilla.*;

public final class ModBlocks {

	private ModBlocks() {
	}

	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(ModConstants.MOD_ID);
	public static final DeferredBlock<Block>
			CLEAN_WHITE_WALL = REGISTRY.register("clean_white_wall", CleanWhiteWallBlock::new),
			BROKEN_WHITE_WALL_ONE = REGISTRY.register("broken_white_wall_one", BrokenWhiteWallOneBlock::new),
			BROKEN_WHITE_WALL_TWO = REGISTRY.register("broken_white_wall_two", BrokenWhiteWallTwoBlock::new),
			DIRTY_BROKEN_WHITE_WALL_ONE = REGISTRY.register("dirty_broken_white_wall_one", DirtyBrokenWhiteWallOneBlock::new),
			DIRTY_BROKEN_WHITE_WALL_TWO = REGISTRY.register("dirty_broken_white_wall_two", DirtyBrokenWhiteWallTwoBlock::new),
			WALL_BASE = REGISTRY.register("wall_base", WallBaseBlock::new),
			GREEN_TILED_FLOOR = REGISTRY.register("green_tiled_floor", GreenTiledFloorBlock::new),
			COPYCAT_CUBE = REGISTRY.register("copycat_cube",
					() -> new CopycatCubeBlock(
							BlockBehaviour.Properties.of()
									.strength(2.0F, 6.0F)
									.sound(SoundType.STONE)
									.requiresCorrectToolForDrops()
					));
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
