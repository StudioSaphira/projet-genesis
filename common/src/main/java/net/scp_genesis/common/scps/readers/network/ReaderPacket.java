package net.scp_genesis.common.scps.readers.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

/** Bounded menu commands and private snapshots, transported by the active platform. */
public record ReaderPacket(CompoundTag data) implements CustomPacketPayload {

    public static final Type<ReaderPacket> TYPE =
            new Type<>(ResourceLocation.parse("scp_genesis:reader"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ReaderPacket> CODEC =
            StreamCodec.of(
                    (buffer, packet) -> buffer.writeNbt(packet.data()),
                    buffer -> new ReaderPacket(buffer.readNbt()));

    @Override
    @NotNull
    public Type<ReaderPacket> type() {
        return TYPE;
    }
}
