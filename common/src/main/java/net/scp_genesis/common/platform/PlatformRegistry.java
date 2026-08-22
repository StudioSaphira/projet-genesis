package net.scp_genesis.common.platform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public interface PlatformRegistry {

    <T extends Block> PlatformRegistryObject<T> registerBlock(
            String id,
            Supplier<T> supplier
    );

    <T extends Item> PlatformRegistryObject<T> registerItem(
            String id,
            Supplier<T> supplier
    );

    PlatformRegistryObject<Item> registerBlockItem(
            String id,
            PlatformRegistryObject<? extends Block> block
    );

    <T extends BlockEntityType<?>> PlatformRegistryObject<T> registerBlockEntity(
            String id,
            Supplier<T> supplier
    );

    PlatformRegistryObject<CreativeModeTab> registerCreativeModeTab(
            String id,
            Supplier<CreativeModeTab.Builder> builder
    );

    PlatformRegistryObject<CreativeModeTab> registerCreativeModeTab(
            String id,
            ResourceLocation before,
            Supplier<CreativeModeTab.Builder> builder
    );

    void register();
}