package net.scp_genesis.client;

import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import net.scp_genesis.copycatblocks.client.CopycatBlockColor;
import net.scp_genesis.registry.ModBlocks;

public final class ModBlockColors {

    private ModBlockColors() {}

    public static void register(RegisterColorHandlersEvent.Block event) {

        event.register(
                new CopycatBlockColor(),
                ModBlocks.COPYCAT_CUBE.get(),
                ModBlocks.COPYCAT_STAIRS.get(),
                ModBlocks.COPYCAT_SLAB.get()
        );
    }
}