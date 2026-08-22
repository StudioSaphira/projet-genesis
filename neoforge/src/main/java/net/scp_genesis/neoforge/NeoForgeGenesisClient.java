package net.scp_genesis.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.api.distmarker.Dist;

import net.scp_genesis.neoforge.client.ModBlockColors;
import net.scp_genesis.neoforge.client.ModModelLoading;

@Mod(
        value = "scp_genesis",
        dist = Dist.CLIENT
)
public final class NeoForgeGenesisClient {

    public NeoForgeGenesisClient(
            IEventBus modEventBus
    ) {
        modEventBus.addListener(
                ModBlockColors::register
        );

        modEventBus.addListener(
                ModModelLoading::registerGeometryLoaders
        );
    }
}