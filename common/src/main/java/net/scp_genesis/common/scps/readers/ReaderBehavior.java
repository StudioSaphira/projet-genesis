package net.scp_genesis.common.scps.readers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
/** Server-side access strategy; each reader type has a dedicated implementation. */
public interface ReaderBehavior { void use(ReaderBlockEntity reader, ServerPlayer player, ItemStack held); }
