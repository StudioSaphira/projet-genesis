package net.scp_genesis.fabric.client;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

import net.scp_genesis.common.copycatblocks.client.CopycatBlockColor;
import net.scp_genesis.common.registry.ModBlocks;

public final class FabricBlockColors {

    private FabricBlockColors() {
    }

    public static void register() {

        ColorProviderRegistry.BLOCK.register(
                new CopycatBlockColor(),
                ModBlocks.COPYCAT_CUBE.get(),
                ModBlocks.COPYCAT_STAIRS.get(),
                ModBlocks.COPYCAT_SLAB.get(),
                ModBlocks.COPYCAT_SLOPE.get(),
                        ModBlocks.COPYCAT_HALF_SLOPE.get(),
                        ModBlocks.COPYCAT_HORIZONTAL_HALF_SLOPE.get(),
                        ModBlocks.COPYCAT_VERTICAL_HALF_SLOPE.get()
        );
    }
}