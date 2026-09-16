package net.scp_genesis.common.registry;

import net.minecraft.world.level.block.Block;
import net.scp_genesis.common.furnitures.notmodular.room.bamboo.FurnitureChairBamboo;
import net.scp_genesis.common.furnitures.notmodular.office.FurnitureOfficeChair;
import net.scp_genesis.common.furnitures.notmodular.room.cherry.FurnitureChairCherry;
import net.scp_genesis.common.furnitures.notmodular.room.mangrove.FurnitureChairMangrove;
import net.scp_genesis.common.furnitures.notmodular.room.darkoak.FurnitureChairDarkOak;
import net.scp_genesis.common.furnitures.notmodular.room.acacia.FurnitureChairAcacia;
import net.scp_genesis.common.furnitures.notmodular.room.jungle.FurnitureChairJungle;
import net.scp_genesis.common.furnitures.notmodular.room.birch.FurnitureChairBirch;
import net.scp_genesis.common.furnitures.notmodular.room.spruce.FurnitureChairSpruce;
import net.scp_genesis.common.furnitures.notmodular.room.oak.FurnitureChairOak;
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
    public static PlatformRegistryObject<Block> CHAIR_OAK;
    public static PlatformRegistryObject<Block> DRAWER_OAK;
	public static PlatformRegistryObject<Block> TABLE_SPRUCE;
    public static PlatformRegistryObject<Block> CHAIR_SPRUCE;
    public static PlatformRegistryObject<Block> DRAWER_SPRUCE;
    public static PlatformRegistryObject<Block> TABLE_DARK_OAK;
    public static PlatformRegistryObject<Block> CHAIR_DARK_OAK;
    public static PlatformRegistryObject<Block> DRAWER_DARK_OAK;
    public static PlatformRegistryObject<Block> TABLE_BIRCH;
    public static PlatformRegistryObject<Block> CHAIR_BIRCH;
    public static PlatformRegistryObject<Block> DRAWER_BIRCH;
    public static PlatformRegistryObject<Block> TABLE_JUNGLE;
    public static PlatformRegistryObject<Block> CHAIR_JUNGLE;
    public static PlatformRegistryObject<Block> DRAWER_JUNGLE;
    public static PlatformRegistryObject<Block> TABLE_ACACIA;
    public static PlatformRegistryObject<Block> CHAIR_ACACIA;
    public static PlatformRegistryObject<Block> DRAWER_ACACIA;
    public static PlatformRegistryObject<Block> TABLE_MANGROVE;
    public static PlatformRegistryObject<Block> CHAIR_MANGROVE;
    public static PlatformRegistryObject<Block> DRAWER_MANGROVE;
    public static PlatformRegistryObject<Block> TABLE_CHERRY;
    public static PlatformRegistryObject<Block> CHAIR_CHERRY;
    public static PlatformRegistryObject<Block> DRAWER_CHERRY;
    public static PlatformRegistryObject<Block> TABLE_BAMBOO;
    public static PlatformRegistryObject<Block> CHAIR_BAMBOO;
    public static PlatformRegistryObject<Block> DRAWER_BAMBOO;
    public static PlatformRegistryObject<Block> OFFICE_CHAIR;
    public static PlatformRegistryObject<Block> LOCKER;
    public static PlatformRegistryObject<Block> LOCKER_SHELF;

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
        CHAIR_OAK = registry.registerBlock("chair_oak", FurnitureChairOak::new);
        DRAWER_OAK = registry.registerBlock("drawer_oak", net.scp_genesis.common.furnitures.notmodular.room.oak.FurnitureDrawerOak::new);
		TABLE_SPRUCE = registry.registerBlock("table_spruce", FurnitureTableSpruce::new);
        CHAIR_SPRUCE = registry.registerBlock("chair_spruce", FurnitureChairSpruce::new);
        DRAWER_SPRUCE = registry.registerBlock("drawer_spruce", net.scp_genesis.common.furnitures.notmodular.room.spruce.FurnitureDrawerSpruce::new);
        TABLE_DARK_OAK = registry.registerBlock("table_dark_oak", FurnitureTableDarkOak::new);
        CHAIR_DARK_OAK = registry.registerBlock("chair_dark_oak", FurnitureChairDarkOak::new);
        DRAWER_DARK_OAK = registry.registerBlock("drawer_dark_oak", net.scp_genesis.common.furnitures.notmodular.room.darkoak.FurnitureDrawerDarkOak::new);
        TABLE_BIRCH = registry.registerBlock("table_birch", FurnitureTableBirch::new);
        CHAIR_BIRCH = registry.registerBlock("chair_birch", FurnitureChairBirch::new);
        DRAWER_BIRCH = registry.registerBlock("drawer_birch", net.scp_genesis.common.furnitures.notmodular.room.birch.FurnitureDrawerBirch::new);
        TABLE_JUNGLE = registry.registerBlock("table_jungle", FurnitureTableJungle::new);
        CHAIR_JUNGLE = registry.registerBlock("chair_jungle", FurnitureChairJungle::new);
        DRAWER_JUNGLE = registry.registerBlock("drawer_jungle", net.scp_genesis.common.furnitures.notmodular.room.jungle.FurnitureDrawerJungle::new);
        TABLE_ACACIA = registry.registerBlock("table_acacia", FurnitureTableAcacia::new);
        CHAIR_ACACIA = registry.registerBlock("chair_acacia", FurnitureChairAcacia::new);
        DRAWER_ACACIA = registry.registerBlock("drawer_acacia", net.scp_genesis.common.furnitures.notmodular.room.acacia.FurnitureDrawerAcacia::new);
        TABLE_MANGROVE = registry.registerBlock("table_mangrove", FurnitureTableMangrove::new);
        CHAIR_MANGROVE = registry.registerBlock("chair_mangrove", FurnitureChairMangrove::new);
        DRAWER_MANGROVE = registry.registerBlock("drawer_mangrove", net.scp_genesis.common.furnitures.notmodular.room.mangrove.FurnitureDrawerMangrove::new);
        TABLE_CHERRY = registry.registerBlock("table_cherry", FurnitureTableCherry::new);
        CHAIR_CHERRY = registry.registerBlock("chair_cherry", FurnitureChairCherry::new);
        DRAWER_CHERRY = registry.registerBlock("drawer_cherry", net.scp_genesis.common.furnitures.notmodular.room.cherry.FurnitureDrawerCherry::new);
        TABLE_BAMBOO = registry.registerBlock("table_bamboo", FurnitureTableBamboo::new);
        CHAIR_BAMBOO = registry.registerBlock("chair_bamboo", FurnitureChairBamboo::new);
        DRAWER_BAMBOO = registry.registerBlock("drawer_bamboo", net.scp_genesis.common.furnitures.notmodular.room.bamboo.FurnitureDrawerBamboo::new);
        OFFICE_CHAIR = registry.registerBlock("office_chair", FurnitureOfficeChair::new);
        LOCKER = registry.registerBlock("locker", net.scp_genesis.common.furnitures.notmodular.FurnitureLocker::new);
        LOCKER_SHELF = registry.registerBlock("locker_shelf", net.scp_genesis.common.furnitures.notmodular.FurnitureLockerShelf::new);

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
        ((FireBlock) Blocks.FIRE).setFlammable(CHAIR_OAK.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(DRAWER_OAK.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_SPRUCE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(CHAIR_SPRUCE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(DRAWER_SPRUCE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_DARK_OAK.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(CHAIR_DARK_OAK.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(DRAWER_DARK_OAK.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_BIRCH.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(CHAIR_BIRCH.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(DRAWER_BIRCH.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_JUNGLE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(CHAIR_JUNGLE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(DRAWER_JUNGLE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_ACACIA.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(CHAIR_ACACIA.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(DRAWER_ACACIA.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_MANGROVE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(CHAIR_MANGROVE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(DRAWER_MANGROVE.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_CHERRY.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(CHAIR_CHERRY.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(DRAWER_CHERRY.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(TABLE_BAMBOO.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(CHAIR_BAMBOO.get(), 5, 20);
        ((FireBlock) Blocks.FIRE).setFlammable(DRAWER_BAMBOO.get(), 5, 20);
    }
}
