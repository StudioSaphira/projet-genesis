package net.scp_genesis.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.scp_genesis.block.*;
import net.scp_genesis.copycatblocks.block.vanilla.*;
import net.scp_genesis.platform.PlatformRegistry;
import net.scp_genesis.platform.PlatformRegistryObject;

public final class ModBlocks {

	private ModBlocks() {}

	public static PlatformRegistryObject<Block> CLEAN_WHITE_WALL;
	public static PlatformRegistryObject<Block> BROKEN_WHITE_WALL_ONE;
	public static PlatformRegistryObject<Block> BROKEN_WHITE_WALL_TWO;
	public static PlatformRegistryObject<Block> DIRTY_BROKEN_WHITE_WALL_ONE;
	public static PlatformRegistryObject<Block> DIRTY_BROKEN_WHITE_WALL_TWO;
	public static PlatformRegistryObject<Block> WALL_BASE;
	public static PlatformRegistryObject<Block> GREEN_TILED_FLOOR;

	public static PlatformRegistryObject<Block> COPYCAT_CUBE;
	public static PlatformRegistryObject<Block> COPYCAT_STAIRS;
	public static PlatformRegistryObject<Block> COPYCAT_SLAB;

	public static void register(
			PlatformRegistry registry
	) {
		CLEAN_WHITE_WALL =
				registry.registerBlock(
						"clean_white_wall",
						CleanWhiteWallBlock::new
				);

		BROKEN_WHITE_WALL_ONE =
				registry.registerBlock(
						"broken_white_wall_one",
						BrokenWhiteWallOneBlock::new
				);

		BROKEN_WHITE_WALL_TWO =
				registry.registerBlock(
						"broken_white_wall_two",
						BrokenWhiteWallTwoBlock::new
				);

		DIRTY_BROKEN_WHITE_WALL_ONE =
				registry.registerBlock(
						"dirty_broken_white_wall_one",
						DirtyBrokenWhiteWallOneBlock::new
				);

		DIRTY_BROKEN_WHITE_WALL_TWO =
				registry.registerBlock(
						"dirty_broken_white_wall_two",
						DirtyBrokenWhiteWallTwoBlock::new
				);

		WALL_BASE =
				registry.registerBlock(
						"wall_base",
						WallBaseBlock::new
				);

		GREEN_TILED_FLOOR =
				registry.registerBlock(
						"green_tiled_floor",
						GreenTiledFloorBlock::new
				);

		COPYCAT_CUBE =
				registry.registerBlock(
						"copycat_cube",
						() -> new CopycatCubeBlock(
								BlockBehaviour.Properties.of()
										.strength(2.0F, 6.0F)
										.sound(SoundType.METAL)
										.requiresCorrectToolForDrops()
										.noOcclusion()
						)
				);

		COPYCAT_STAIRS =
				registry.registerBlock(
						"copycat_stairs",
						() -> new CopycatStairsBlock(
								BlockBehaviour.Properties.of()
										.strength(2.0F, 6.0F)
										.sound(SoundType.METAL)
										.requiresCorrectToolForDrops()
										.noOcclusion()
						)
				);

		COPYCAT_SLAB =
				registry.registerBlock(
						"copycat_slab",
						() -> new CopycatSlabBlock(
								BlockBehaviour.Properties.of()
										.strength(2.0F, 6.0F)
										.sound(SoundType.METAL)
										.requiresCorrectToolForDrops()
										.noOcclusion()
						)
				);
	}
}