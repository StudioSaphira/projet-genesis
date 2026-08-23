package net.scp_genesis.fabric.platform;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.scp_genesis.common.constants.ModConstants;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.platform.PlatformCreativeModeTabBuilder;
import net.scp_genesis.common.platform.PlatformRegistry;
import net.scp_genesis.common.platform.PlatformRegistryObject;
import net.scp_genesis.common.registry.ModBlockEntities;
import net.scp_genesis.common.registry.ModBlocks;
import net.scp_genesis.common.registry.ModItems;
import net.scp_genesis.common.registry.ModTabs;

import java.util.function.Supplier;

public final class FabricPlatformRegistry
        implements PlatformRegistry {

    private ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(
                ModConstants.MOD_ID,
                path
        );
    }

    // ------------------------------------------------------------------------
    // Blocks
    // ------------------------------------------------------------------------

    @Override
    public <T extends Block> PlatformRegistryObject<T> registerBlock(
            String id,
            Supplier<T> supplier
    ) {
        ResourceLocation location = id(id);

        T block = Registry.register(
                BuiltInRegistries.BLOCK,
                location,
                supplier.get()
        );

        return new FabricPlatformRegistryObject<>(
                block,
                location
        );
    }

    // ------------------------------------------------------------------------
    // Items
    // ------------------------------------------------------------------------

    @Override
    public <T extends Item> PlatformRegistryObject<T> registerItem(
            String id,
            Supplier<T> supplier
    ) {
        ResourceLocation location = id(id);

        T item = Registry.register(
                BuiltInRegistries.ITEM,
                location,
                supplier.get()
        );

        return new FabricPlatformRegistryObject<>(
                item,
                location
        );
    }

    @Override
    public PlatformRegistryObject<Item> registerBlockItem(
            String id,
            PlatformRegistryObject<? extends Block> block
    ) {
        ResourceLocation location = id(id);

        Item item = Registry.register(
                BuiltInRegistries.ITEM,
                location,
                new BlockItem(
                        block.get(),
                        new Item.Properties()
                )
        );

        return new FabricPlatformRegistryObject<>(
                item,
                location
        );
    }

    // ------------------------------------------------------------------------
    // Block Entities
    // ------------------------------------------------------------------------

    @Override
    public PlatformRegistryObject<BlockEntityType<CopycatBlockEntity>>
    registerCopycatBlockEntity(String id) {

        ResourceLocation location = id(id);

        BlockEntityType<CopycatBlockEntity> type =
                BlockEntityType.Builder.of(
                        CopycatBlockEntity::new,
                        ModBlocks.COPYCAT_CUBE.get(),
                        ModBlocks.COPYCAT_SLAB.get(),
                        ModBlocks.COPYCAT_STAIRS.get()
                ).build(null);

        Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                location,
                type
        );

        return new FabricPlatformRegistryObject<>(
                type,
                location
        );
    }

    @Override
    public <T extends BlockEntityType<?>> PlatformRegistryObject<T> registerBlockEntity(
            String id,
            Supplier<T> supplier
    ) {
        ResourceLocation location = id(id);

        T blockEntityType = Registry.register(
                BuiltInRegistries.BLOCK_ENTITY_TYPE,
                location,
                supplier.get()
        );

        return new FabricPlatformRegistryObject<>(
                blockEntityType,
                location
        );
    }

    // ------------------------------------------------------------------------
    // Creative Mode Tabs
    // ------------------------------------------------------------------------

    @Override
    public PlatformCreativeModeTabBuilder createCreativeModeTabBuilder() {
        return new FabricCreativeModeTabBuilder();
    }

    @Override
    public PlatformRegistryObject<CreativeModeTab> registerCreativeModeTab(
            String id,
            Supplier<PlatformCreativeModeTabBuilder> builder
    ) {
        ResourceLocation location = id(id);

        CreativeModeTab tab = Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                location,
                builder.get().build()
        );

        return new FabricPlatformRegistryObject<>(
                tab,
                location
        );
    }

    @Override
    public PlatformRegistryObject<CreativeModeTab> registerCreativeModeTab(
            String id,
            ResourceLocation before,
            Supplier<PlatformCreativeModeTabBuilder> builder
    ) {
        ResourceLocation location = id(id);

        CreativeModeTab tab = Registry.register(
                BuiltInRegistries.CREATIVE_MODE_TAB,
                location,
                builder.get().build()
        );

        return new FabricPlatformRegistryObject<>(
                tab,
                location
        );
    }

    // ------------------------------------------------------------------------
    // Registration
    // ------------------------------------------------------------------------

    @Override
    public void register() {
        ModBlocks.register(this);
        ModItems.register(this);
        ModBlockEntities.register(this);
        ModTabs.register(this);
    }
}