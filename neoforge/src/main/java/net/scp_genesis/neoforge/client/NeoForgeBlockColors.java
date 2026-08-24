package net.scp_genesis.neoforge.client;

import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import net.scp_genesis.common.registry.ModBlocks;
import net.scp_genesis.common.copycatblocks.client.CopycatBlockColor;

public final class NeoForgeBlockColors {

    private NeoForgeBlockColors() {}

    public static void register(RegisterColorHandlersEvent.Block event) {

        event.register(
                new CopycatBlockColor(),
                ModBlocks.COPYCAT_CUBE.get(),
                ModBlocks.COPYCAT_STAIRS.get(),
                ModBlocks.COPYCAT_SLAB.get(),
                ModBlocks.COPYCAT_SLOPE.get()
        );
    }
}