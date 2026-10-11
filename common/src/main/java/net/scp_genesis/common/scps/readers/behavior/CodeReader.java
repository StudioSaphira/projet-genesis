package net.scp_genesis.common.scps.readers.behavior;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.scp_genesis.common.scps.readers.blockentity.ReaderBlockEntity;
import net.scp_genesis.common.scps.readers.util.ReaderMenus;

/** Opens the numeric keypad without disclosing the saved code. */
public final class CodeReader implements ReaderBehavior {

    public void use(ReaderBlockEntity reader, ServerPlayer player, ItemStack held) {
        if (reader.ready()) {
            ReaderMenus.open(player, reader, false);
        }
    }
}
