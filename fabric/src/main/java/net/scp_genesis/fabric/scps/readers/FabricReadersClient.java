package net.scp_genesis.fabric.scps.readers;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screens.MenuScreens;
import net.scp_genesis.common.registry.ModMenus;
import net.scp_genesis.common.scps.readers.client.ReaderScreen;
import net.scp_genesis.common.scps.readers.network.ReaderClientBridge;
import net.scp_genesis.common.scps.readers.network.ReaderPacket;

/** Installs the shared screen and Fabric client transport. */
public final class FabricReadersClient {

    private FabricReadersClient() {}

    public static void register() {
        ReaderScreen.sender = ClientPlayNetworking::send;
        ReaderClientBridge.install(ReaderScreen::receive);
        ClientPlayNetworking.registerGlobalReceiver(
                ReaderPacket.TYPE,
                (packet, context) ->
                        context.client().execute(() -> ReaderClientBridge.receive(packet)));
        MenuScreens.register(ModMenus.READER.get(), ReaderScreen::new);
    }
}
