package net.scp_genesis.fabric.scps.readers;
import net.fabricmc.fabric.api.networking.v1.*;
import net.scp_genesis.common.scps.readers.*;
/** Fabric transport registration for the common reader protocol. */
public final class FabricReaders {
    private FabricReaders() {}
    public static void register() {
        PayloadTypeRegistry.playC2S().register(ReaderPacket.TYPE,ReaderPacket.CODEC);
        PayloadTypeRegistry.playS2C().register(ReaderPacket.TYPE,ReaderPacket.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(ReaderPacket.TYPE,(packet,context) ->
                context.server().execute(() -> ReaderServer.handle(context.player(),packet)));
    }
}
