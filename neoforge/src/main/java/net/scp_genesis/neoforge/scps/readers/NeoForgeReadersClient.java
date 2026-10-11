package net.scp_genesis.neoforge.scps.readers;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.scp_genesis.common.registry.ModMenus;
import net.scp_genesis.common.scps.readers.client.ReaderScreen;
import net.scp_genesis.common.scps.readers.network.ReaderClientBridge;

/** Installs the shared screen and NeoForge client transport. */
public final class NeoForgeReadersClient {

    private NeoForgeReadersClient() {}

    public static void register(IEventBus bus) {
        ReaderScreen.sender = PacketDistributor::sendToServer;
        ReaderClientBridge.install(ReaderScreen::receive);
        bus.addListener(
                (RegisterMenuScreensEvent event) ->
                        event.register(ModMenus.READER.get(), ReaderScreen::new));
    }
}
