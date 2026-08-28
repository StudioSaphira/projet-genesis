package net.scp_genesis.neoforge.client;

import net.neoforged.neoforge.client.event.ModelEvent;
import net.scp_genesis.neoforge.copycatblocks.renderer.model.NeoForgeCopycatModelLoader;

public final class NeoForgeModelLoading {

    private NeoForgeModelLoading() {}

    public static void registerGeometryLoaders(
            ModelEvent.RegisterGeometryLoaders event
    ) {

        event.register(
                NeoForgeCopycatModelLoader.ID,
                NeoForgeCopycatModelLoader.INSTANCE
        );

        event.register(
                NeoForgeCopycatModelLoader.ID_SLOPE,
                NeoForgeCopycatModelLoader.SLOPE_INSTANCE
        );
    }
}