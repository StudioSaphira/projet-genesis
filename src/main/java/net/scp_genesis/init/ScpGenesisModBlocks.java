package net.scp_genesis.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

import net.minecraft.world.level.block.Block;

import net.scp_genesis.ScpGenesisMod;
import net.scp_genesis.block.*;

public class ScpGenesisModBlocks {
	public static final DeferredRegister.Blocks REGISTRY = DeferredRegister.createBlocks(ScpGenesisMod.MODID);
	public static final DeferredBlock<Block> CLEAN_WHITE_WALL;
	public static final DeferredBlock<Block> BROKEN_WHITE_WALL_ONE;
	public static final DeferredBlock<Block> BROKEN_WHITE_WALL_TWO;
	public static final DeferredBlock<Block> DIRTY_BROKEN_WHITE_WALL_ONE;
	public static final DeferredBlock<Block> DIRTY_BROKEN_WHITE_WALL_TWO;
	public static final DeferredBlock<Block> WALL_BASE;
	public static final DeferredBlock<Block> GREEN_TILED_FLOOR;
	public static final DeferredBlock<Block> COPYCAT_CUBE;
	static {
		CLEAN_WHITE_WALL = REGISTRY.register("clean_white_wall", CleanWhiteWallBlock::new);
		BROKEN_WHITE_WALL_ONE = REGISTRY.register("broken_white_wall_one", BrokenWhiteWallOneBlock::new);
		BROKEN_WHITE_WALL_TWO = REGISTRY.register("broken_white_wall_two", BrokenWhiteWallTwoBlock::new);
		DIRTY_BROKEN_WHITE_WALL_ONE = REGISTRY.register("dirty_broken_white_wall_one", DirtyBrokenWhiteWallOneBlock::new);
		DIRTY_BROKEN_WHITE_WALL_TWO = REGISTRY.register("dirty_broken_white_wall_two", DirtyBrokenWhiteWallTwoBlock::new);
		WALL_BASE = REGISTRY.register("wall_base", WallBaseBlock::new);
		GREEN_TILED_FLOOR = REGISTRY.register("green_tiled_floor", GreenTiledFloorBlock::new);
		COPYCAT_CUBE = REGISTRY.register("copycat_cube", CopycatCubeBlock::new);
	}
	// Start of user code block custom blocks
	// End of user code block custom blocks
}