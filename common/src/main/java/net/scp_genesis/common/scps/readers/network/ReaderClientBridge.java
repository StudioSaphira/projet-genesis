package net.scp_genesis.common.scps.readers.network;

import java.util.function.Consumer;

/**
 * Client receiver installed only by client entry points, avoiding client classes on dedicated
 * servers.
 */
public final class ReaderClientBridge {

    private static Consumer<ReaderPacket> receiver = packet -> {};

    private ReaderClientBridge() {}

    public static void install(Consumer<ReaderPacket> callback) {
        receiver = callback;
    }

    public static void receive(ReaderPacket packet) {
        receiver.accept(packet);
    }
}
