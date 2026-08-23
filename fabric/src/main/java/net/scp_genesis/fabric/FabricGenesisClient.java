package net.scp_genesis.fabric;

import net.fabricmc.api.ClientModInitializer;

import net.scp_genesis.fabric.client.FabricBlockColors;
import net.scp_genesis.fabric.client.FabricModelLoading;

public final class FabricGenesisClient
        implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        FabricBlockColors.register();
        FabricModelLoading.register();
    }
}