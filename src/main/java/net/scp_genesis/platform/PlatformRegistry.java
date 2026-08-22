package net.scp_genesis.platform;

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

    <T extends Item> PlatformRegistryObject<T> registerBlockItem(
            String id,
            PlatformRegistryObject<? extends Block> block
    );

    <T extends BlockEntityType<?>> PlatformRegistryObject<T> registerBlockEntity(
            String id,
            Supplier<T> supplier
    );

    void register();
}