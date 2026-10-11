package net.scp_genesis.common.scps.readers.behavior;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.scp_genesis.common.scps.readers.blockentity.ReaderBlockEntity;

/** A button accepts any normal main-hand interaction. */
public final class ButtonReader implements ReaderBehavior {

    public void use(ReaderBlockEntity reader, ServerPlayer player, ItemStack held) {
        if (reader.ready()) {
            reader.open();
        }
    }
}
