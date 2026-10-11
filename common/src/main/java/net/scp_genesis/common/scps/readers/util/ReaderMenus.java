package net.scp_genesis.common.scps.readers.util;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.scp_genesis.common.scps.readers.blockentity.ReaderBlockEntity;
import net.scp_genesis.common.scps.readers.item.ReaderConfiguratorItem;
import net.scp_genesis.common.scps.readers.menu.ReaderMenu;

/** Opens one vanilla menu type for either configuration or code entry. */
public final class ReaderMenus {

    private ReaderMenus() {}

    public static void open(ServerPlayer player, ReaderBlockEntity reader, boolean configuring) {
        if (player.isSpectator()
                || (configuring
                        && (!player.mayBuild() || !ReaderConfiguratorItem.isHeld(player)))) {
            return;
        }

        player.openMenu(
                new SimpleMenuProvider(
                        (id, inventory, p) -> new ReaderMenu(id, inventory, reader, configuring),
                        Component.translatable(
                                configuring
                                        ? "gui.scp_genesis.reader.configure"
                                        : "gui.scp_genesis.reader.keypad")));
    }
}
