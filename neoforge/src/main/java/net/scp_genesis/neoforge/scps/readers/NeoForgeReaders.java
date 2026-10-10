package net.scp_genesis.neoforge.scps.readers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.level.ServerPlayer;
import net.scp_genesis.common.scps.readers.*;
/** NeoForge transport registration, with all gameplay work dispatched to the main thread. */
public final class NeoForgeReaders {
    private NeoForgeReaders() {}
    public static void register(IEventBus bus) {
        bus.addListener((RegisterPayloadHandlersEvent event) -> event.registrar("1").playBidirectional(
                ReaderPacket.TYPE,ReaderPacket.CODEC,(packet,context) -> context.enqueueWork(() -> {
                    if(context.flow()==PacketFlow.SERVERBOUND && context.player() instanceof ServerPlayer player)
                        ReaderServer.handle(player,packet);
                    else if(context.flow()==PacketFlow.CLIENTBOUND) ReaderClientBridge.receive(packet);
                })));
    }
}
