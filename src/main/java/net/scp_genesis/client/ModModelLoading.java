package net.scp_genesis.client;

import net.neoforged.neoforge.client.event.ModelEvent;

import net.scp_genesis.copycatblocks.renderer.model.CopycatModelLoader;

public final class ModModelLoading {

    private ModModelLoading() {}

    public static void registerGeometryLoaders(
            ModelEvent.RegisterGeometryLoaders event
    ) {
        event.register(
                CopycatModelLoader.ID,
                CopycatModelLoader.INSTANCE
        );
    }
}