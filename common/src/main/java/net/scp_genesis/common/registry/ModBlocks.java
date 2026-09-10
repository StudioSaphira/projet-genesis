package net.scp_genesis.common.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.scp_genesis.common.furnitures.notmodular.room.oak.FurnitureTableOak;
import net.scp_genesis.common.furnitures.notmodular.room.spruce.FurnitureTableSpruce;
import net.scp_genesis.common.furnitures.notmodular.room.birch.FurnitureTableBirch;
import net.scp_genesis.common.furnitures.notmodular.room.jungle.FurnitureTableJungle;
import net.scp_genesis.common.furnitures.notmodular.room.acacia.FurnitureTableAcacia;
import net.scp_genesis.common.furnitures.notmodular.room.mangrove.FurnitureTableMangrove;
import net.scp_genesis.common.furnitures.notmodular.room.cherry.FurnitureTableCherry;
import net.scp_genesis.common.furnitures.notmodular.room.bamboo.FurnitureTableBamboo;
import net.scp_genesis.common.furnitures.notmodular.room.darkoak.FurnitureTableDarkOak;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.scp_genesis.common.block.*;
import net.scp_genesis.common.copycatblocks.block.custom.CopycatSlopeBlock;
import net.scp_genesis.common.copycatblocks.block.vanilla.CopycatCubeBlock;
import net.scp_genesis.common.copycatblocks.block.vanilla.CopycatSlabBlock;
import net.scp_genesis.common.copycatblocks.block.vanilla.CopycatStairsBlock;
import net.scp_genesis.common.platform.PlatformRegistry;
import net.scp_genesis.common.platform.PlatformRegistryObject;

public final class ModBlocks {

	private ModBlocks() {}

	public static PlatformRegistryObject<Block> CLEAN_WHITE_WALL;
	public static PlatformRegistryObject<Block> DIRTY_WHITE_WALL;
	public static PlatformRegistryObject<Block> BROKEN_WHITE_WALL_ONE;
	public static PlatformRegistryObject<Block> BROKEN_WHITE_WALL_TWO;
	public static PlatformRegistryObject<Block> DIRTY_BROKEN_WHITE_WALL_ONE;
	public static PlatformRegistryObject<Block> DIRTY_BROKEN_WHITE_WALL_TWO;
	public static PlatformRegistryObject<Block> WALL_BASE;
	public static PlatformRegistryObject<Block> GREEN_TILED_FLOOR;

    public static PlatformRegistryObject<Block> TABLE_OAK;
	public static PlatformRegistryObject<Block> TABLE_SPRUCE;
    public static PlatformRegistryObject<Block> TABLE_DARK_OAK;
    public static PlatformRegistryObject<Block> TABLE_BIRCH;
    public static PlatformRegistryObject<Block> TABLE_JUNGLE;
    public static PlatformRegistryObject<Block> TABLE_ACACIA;
    public static PlatformRegistryObject<Block> TABLE_MANGROVE;
    public static PlatformRegistryObject<Block> TABLE_CHERRY;
    public static PlatformRegistryObject<Block> TABLE_BAMBOO;

	public static PlatformRegistryObject<Block> COPYCAT_CUBE;
	public static PlatformRegistryObject<Block> COPYCAT_STAIRS;
	public static PlatformRegistryObject<Block> COPYCAT_SLAB;
	public static PlatformRegistryObject<Block> COPYCAT_SLOPE;

	public static void register(
			PlatformRegistry registry
	) {
		CLEAN_WHITE_WALL = registry.registerBlock("clean_white_wall", CleanWhiteTiledBlock::new);
		DIRTY_WHITE_WALL = registry.registerBlock("dirty_white_wall", DirtyWhiteTiledBlock::new);
		BROKEN_WHITE_WALL_ONE = registry.registerBlock("broken_white_wall_one", BrokenWhiteTiledOneBlock::new);
		BROKEN_WHITE_WALL_TWO = registry.registerBlock("broken_white_wall_two", BrokenWhiteTiledTwoBlock::new);
		DIRTY_BROKEN_WHITE_WALL_ONE = registry.registerBlock("dirty_broken_white_wall_one", DirtyBrokenWhiteTiledOneBlock::new);
		DIRTY_BROKEN_WHITE_WALL_TWO = registry.registerBlock("dirty_broken_white_wall_two", DirtyBrokenWhiteTiledTwoBlock::new);
		WALL_BASE = registry.registerBlock("wall_base", WallBaseBlock::new);
		GREEN_TILED_FLOOR = registry.registerBlock("green_tiled_floor", GreenTiledFloorBlock::new);

        TABLE_OAK = registry.registerBlock("table_oak", FurnitureTableOak::new);
		TABLE_SPRUCE = registry.registerBlock("table_spruce", FurnitureTableSpruce::new);
        TABLE_DARK_OAK = registry.registerBlock("table_dark_oak", FurnitureTableDarkOak::new);
        TABLE_BIRCH = registry.registerBlock("table_birch", FurnitureTableBirch::new);
        TABLE_JUNGLE = registry.registerBlock("table_jungle", FurnitureTableJungle::new);
        TABLE_ACACIA = registry.registerBlock("table_acacia", FurnitureTableAcacia::new);
        TABLE_MANGROVE = registry.registerBlock("table_mangrove", FurnitureTableMangrove::new);
        TABLE_CHERRY = registry.registerBlock("table_cherry", FurnitureTableCherry::new);
        TABLE_BAMBOO = registry.registerBlock("table_bamboo", FurnitureTableBamboo::new);

		COPYCAT_CUBE = registry.registerBlock("copycat_cube", () -> new CopycatCubeBlock(
				BlockBehaviour.Properties.of()
						.strength(2.0F, 6.0F)
						.sound(SoundType.METAL)
						.requiresCorrectToolForDrops()
						.noOcclusion()));

		COPYCAT_STAIRS = registry.registerBlock("copycat_stairs", () -> new CopycatStairsBlock(
				BlockBehaviour.Properties.of()
						.strength(2.0F, 6.0F)
						.sound(SoundType.METAL)
						.requiresCorrectToolForDrops()
						.noOcclusion()));

		COPYCAT_SLAB = registry.registerBlock("copycat_slab", () -> new CopycatSlabBlock(
				BlockBehaviour.Properties.of()
						.strength(2.0F, 6.0F)
						.sound(SoundType.METAL)
						.requiresCorrectToolForDrops()
						.noOcclusion()));

		COPYCAT_SLOPE = registry.registerBlock("copycat_slope", () -> new CopycatSlopeBlock(
				BlockBehaviour.Properties.of()
						.strength(2.0F, 6.0F)
						.sound(SoundType.METAL)
						.requiresCorrectToolForDrops()));
	}

    /** Call after block registration, on the main setup thread. */
    public static void registerFlammables() {
        // Vanilla oak planks: encouragement 5, flammability 20.
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_OAK.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_SPRUCE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_DARK_OAK.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_BIRCH.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_JUNGLE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_ACACIA.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_MANGROVE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_CHERRY.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_BAMBOO.get(), 5, 20);
    }
}
