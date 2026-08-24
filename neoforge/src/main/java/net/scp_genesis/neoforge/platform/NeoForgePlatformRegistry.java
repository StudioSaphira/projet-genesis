package net.scp_genesis.neoforge.platform;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

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

public final class NeoForgePlatformRegistry
        implements PlatformRegistry {

    private final IEventBus modEventBus;

    private final DeferredRegister.Blocks blocks;
    private final DeferredRegister.Items items;
    private final DeferredRegister<BlockEntityType<?>> blockEntities;
    private final DeferredRegister<CreativeModeTab> creativeModeTabs;

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

        this.creativeModeTabs =
                DeferredRegister.create(
                        Registries.CREATIVE_MODE_TAB,
                        ModConstants.MOD_ID
                );
    }

    @Override
    public <T extends Block> PlatformRegistryObject<T> registerBlock(
            String id,
            Supplier<T> supplier
    ) {
        var holder = blocks.register(id, supplier);

        return new NeoForgePlatformRegistryObject<>(
                holder,
                holder::getId
        );
    }

    @Override
    public <T extends Item> PlatformRegistryObject<T> registerItem(
            String id,
            Supplier<T> supplier
    ) {
        var holder = items.register(id, supplier);

        return new NeoForgePlatformRegistryObject<>(
                holder,
                holder::getId
        );
    }

    @Override
    public PlatformRegistryObject<Item> registerBlockItem(
            String id,
            PlatformRegistryObject<? extends Block> block
    ) {
        var holder = items.register(
                id,
                () -> new net.minecraft.world.item.BlockItem(
                        block.get(),
                        new Item.Properties()
                )
        );

        return new NeoForgePlatformRegistryObject<>(
                holder,
                holder::getId
        );
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public PlatformRegistryObject<BlockEntityType<CopycatBlockEntity>>
    registerCopycatBlockEntity(String id) {

        var holder = blockEntities.register(
                id,
                () -> BlockEntityType.Builder.of(
                        CopycatBlockEntity::new,
                        ModBlocks.COPYCAT_CUBE.get(),
                        ModBlocks.COPYCAT_SLAB.get(),
                        ModBlocks.COPYCAT_STAIRS.get(),
                        ModBlocks.COPYCAT_SLOPE.get()
                ).build(null)
        );

        return new NeoForgePlatformRegistryObject<>(
                holder,
                holder::getId
        );
    }

    @Override
    public <T extends BlockEntityType<?>> PlatformRegistryObject<T> registerBlockEntity(
            String id,
            Supplier<T> supplier
    ) {
        var holder = blockEntities.register(id, supplier);

        return new NeoForgePlatformRegistryObject<>(
                holder,
                holder::getId
        );
    }

    @Override
    public PlatformCreativeModeTabBuilder createCreativeModeTabBuilder() {
        return new NeoForgeCreativeModeTabBuilder();
    }

    @Override
    public PlatformRegistryObject<CreativeModeTab> registerCreativeModeTab(
            String id,
            Supplier<PlatformCreativeModeTabBuilder> builder
    ) {
        var holder = creativeModeTabs.register(
                id,
                () -> builder.get().build()
        );

        return new NeoForgePlatformRegistryObject<>(
                holder,
                holder::getId
        );
    }

    @Override
    public PlatformRegistryObject<CreativeModeTab> registerCreativeModeTab(
            String id,
            ResourceLocation before,
            Supplier<PlatformCreativeModeTabBuilder> builder
    ) {
        var holder = creativeModeTabs.register(
                id,
                () -> {
                    PlatformCreativeModeTabBuilder tabBuilder = builder.get();

                    tabBuilder.withTabsBefore(before);

                    return tabBuilder.build();
                }
        );

        return new NeoForgePlatformRegistryObject<>(
                holder,
                holder::getId
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
        creativeModeTabs.register(modEventBus);
    }
}