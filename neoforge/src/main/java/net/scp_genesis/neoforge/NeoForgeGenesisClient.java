package net.scp_genesis.neoforge;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.api.distmarker.Dist;

import net.neoforged.neoforge.common.NeoForge;
import net.scp_genesis.neoforge.client.NeoForgeBlockColors;
import net.scp_genesis.neoforge.client.NeoForgeModelLoading;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatOutlineHandler;

@Mod(
        value = "scp_genesis",
        dist = Dist.CLIENT
)
public final class NeoForgeGenesisClient {

    public NeoForgeGenesisClient(IEventBus modEventBus) {
        modEventBus.addListener(NeoForgeBlockColors::register);
        modEventBus.addListener(NeoForgeModelLoading::registerGeometryLoaders);

        NeoForge.EVENT_BUS.register(NeoForgeCopycatOutlineHandler.class);
    }
}