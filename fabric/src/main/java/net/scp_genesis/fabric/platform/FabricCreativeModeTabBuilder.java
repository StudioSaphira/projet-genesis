package net.scp_genesis.fabric.platform;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import net.scp_genesis.common.platform.PlatformCreativeModeTabBuilder;

import java.util.function.Supplier;

public final class FabricCreativeModeTabBuilder
        implements PlatformCreativeModeTabBuilder {

    private final CreativeModeTab.Builder builder;

    public FabricCreativeModeTabBuilder() {
        this.builder =
                CreativeModeTab.builder(
                        CreativeModeTab.Row.TOP,
                        0
                );
    }

    @Override
    public PlatformCreativeModeTabBuilder title(
            Component title
    ) {
        builder.title(title);
        return this;
    }

    @Override
    public PlatformCreativeModeTabBuilder icon(
            Supplier<ItemStack> icon
    ) {
        builder.icon(icon);
        return this;
    }

    @Override
    public PlatformCreativeModeTabBuilder displayItems(
            CreativeModeTab.DisplayItemsGenerator generator
    ) {
        builder.displayItems(generator);
        return this;
    }

    @Override
    public PlatformCreativeModeTabBuilder withSearchBar() {
        /*
         * Fabric 1.21.1 use the vanilla builder.
         * The creation of the builder with a position
         * then allow to use the vanilla configuration.
         */
        return this;
    }

    @Override
    public CreativeModeTab build() {
        return builder.build();
    }
}