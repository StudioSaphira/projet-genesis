package net.scp_genesis.common.scps.readers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
/** Rejects every normal interaction, without changing the locked visual state. */
public final class LockedReader implements ReaderBehavior {
    public void use(ReaderBlockEntity reader, ServerPlayer player, ItemStack held) {  }
}
