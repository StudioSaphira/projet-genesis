package net.scp_genesis.platform;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.scp_genesis.constants.ModConstants;
import net.scp_genesis.registry.ModBlockEntities;
import net.scp_genesis.registry.ModBlocks;
import net.scp_genesis.registry.ModItems;
import net.scp_genesis.registry.ModTabs;

import java.util.function.Supplier;

public final class NeoForgePlatformRegistry
        implements PlatformRegistry {

    private final IEventBus modEventBus;

    private final DeferredRegister.Blocks blocks;
    private final DeferredRegister.Items items;

    private final DeferredRegister<BlockEntityType<?>> blockEntities;

    public NeoForgePlatformRegistry(
            IEventBus modEventBus
    ) {
        this.modEventBus = modEventBus;

        this.blocks =
                DeferredRegister.createBlocks(
                        ModConstants.MOD_ID
                );

        this.items =
                DeferredRegister.createItems(
                        ModConstants.MOD_ID
                );

        this.blockEntities =
                DeferredRegister.create(
                        net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE,
                        ModConstants.MOD_ID
                );
    }

    @Override
    public <T extends Block> PlatformRegistryObject<T> registerBlock(
            String id,
            Supplier<T> supplier
    ) {
        return new NeoForgePlatformRegistryObject<>(
                blocks.register(
                        id,
                        supplier
                )
        );
    }

    @Override
    public <T extends Item> PlatformRegistryObject<T> registerItem(
            String id,
            Supplier<T> supplier
    ) {
        return new NeoForgePlatformRegistryObject<>(
                items.register(
                        id,
                        supplier
                )
        );
    }

    @Override
    public <T extends BlockEntityType<?>> PlatformRegistryObject<T> registerBlockEntity(
            String id,
            Supplier<T> supplier
    ) {
        return new NeoForgePlatformRegistryObject<>(
                blockEntities.register(
                        id,
                        supplier
                )
        );
    }

    @Override
    public void register() {
        ModBlocks.register(this);
        ModItems.register(this);
        ModBlockEntities.register(this);
        ModTabs.register(this);

        blocks.register(modEventBus);
        items.register(modEventBus);
        blockEntities.register(modEventBus);
    }
}