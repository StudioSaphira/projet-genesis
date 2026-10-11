package net.scp_genesis.common.scps.readers.behavior;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.scp_genesis.common.scps.readers.blockentity.ReaderBlockEntity;

/** Reserved identification reader, currently unavailable. */
public final class IdReader implements ReaderBehavior {

    public void use(ReaderBlockEntity reader, ServerPlayer player, ItemStack held) {
        if (reader.ready()) {
            reader.error(50);
        }
    }
}
