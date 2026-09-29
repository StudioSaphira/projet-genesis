package net.scp_genesis.common.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;

import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.util.CopycatConstants;
import net.scp_genesis.common.platform.PlatformRegistry;
import net.scp_genesis.common.platform.PlatformRegistryObject;

public final class ModBlockEntities {
    public static PlatformRegistryObject<BlockEntityType<net.scp_genesis.common.furnitures.storage.PlateBlockEntity>> PLATE;
    public static PlatformRegistryObject<BlockEntityType<net.scp_genesis.common.furnitures.storage.DrawerBlockEntity>> DRAWER;

    private ModBlockEntities() {}

    public static PlatformRegistryObject<BlockEntityType<net.scp_genesis.common.furnitures.storage.LockerBlockEntity>> LOCKER;

    public static PlatformRegistryObject<BlockEntityType<CopycatBlockEntity>>
            COPYCAT_BLOCK_ENTITY;

    public static void register(
            PlatformRegistry registry
    ) {
        PLATE = registry.registerBlockEntity("plate", net.scp_genesis.common.furnitures.storage.PlateBlockEntity::new,
                () -> new net.minecraft.world.level.block.Block[]{ModBlocks.PLATE.get()});
        DRAWER = registry.registerBlockEntity("drawer",
                net.scp_genesis.common.furnitures.storage.DrawerBlockEntity::new,
                () -> new net.minecraft.world.level.block.Block[]{ModBlocks.DRAWER_OAK.get(), ModBlocks.DRAWER_SPRUCE.get(),
                        ModBlocks.DRAWER_DARK_OAK.get(), ModBlocks.DRAWER_BIRCH.get(), ModBlocks.DRAWER_JUNGLE.get(),
                        ModBlocks.DRAWER_ACACIA.get(), ModBlocks.DRAWER_MANGROVE.get(), ModBlocks.DRAWER_CHERRY.get(), ModBlocks.DRAWER_BAMBOO.get()});
        LOCKER = registry.registerBlockEntity("locker",
                net.scp_genesis.common.furnitures.storage.LockerBlockEntity::new,
                () -> new net.minecraft.world.level.block.Block[]{ModBlocks.LOCKER.get(), ModBlocks.LOCKER_SHELF.get()});
        COPYCAT_BLOCK_ENTITY =
                registry.registerCopycatBlockEntity(
                        CopycatConstants.BLOCK_ENTITY_ID
                );
    }
}
