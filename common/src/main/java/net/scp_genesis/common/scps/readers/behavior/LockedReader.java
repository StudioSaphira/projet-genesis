package net.scp_genesis.common.scps.readers.behavior;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.scp_genesis.common.scps.readers.blockentity.ReaderBlockEntity;

/** Rejects every normal interaction, without changing the locked visual state. */
public final class LockedReader implements ReaderBehavior {

    public void use(ReaderBlockEntity reader, ServerPlayer player, ItemStack held) {}
}
