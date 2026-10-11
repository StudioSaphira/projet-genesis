package net.scp_genesis.common.scps.readers.network;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.scp_genesis.common.platform.PlatformServices;
import net.scp_genesis.common.scps.readers.data.ReaderConfiguration;
import net.scp_genesis.common.scps.readers.data.ReaderType;
import net.scp_genesis.common.scps.readers.item.ReaderConfiguratorItem;
import net.scp_genesis.common.scps.readers.menu.ReaderMenu;
import net.scp_genesis.common.scps.readers.storage.ReaderDirectory;
import net.scp_genesis.common.scps.readers.util.ReaderDoorController;

/** Validates every network action against the current server menu, held tool and reader type. */
public final class ReaderServer {

    private ReaderServer() {}

    @SuppressWarnings("IfCanBeSwitch")
    public static void handle(ServerPlayer player, ReaderPacket packet) {
        var data = packet.data();

        if (data == null
                || !(player.containerMenu instanceof ReaderMenu menu)
                || menu.reader == null
                || data.getInt("menu") != menu.containerId
                || !menu.stillValid(player)) {
            return;
        }

        String action = data.getString("action");
        var reader = menu.reader;

        if (action.equals("sync")) {
            reply(player, menu, "");

            return;
        }

        if (action.equals("code")) {
            if (menu.configuring
                    || reader.configuration().type() != ReaderType.CODE
                    || !reader.ready()) {
                return;
            }

            String code = data.getString("code");

            if (!code.isEmpty()
                    && code.length() <= 12
                    && code.equals(reader.configuration().code())) {
                reader.open();
                player.closeContainer();
            } else {
                reader.error(100);
                reply(player, menu, "gui.scp_genesis.reader.bad_code");
            }

            return;
        }

        if (!menu.configuring) {
            return;
        }

        var directory = ReaderDirectory.get(player.serverLevel());

        if (action.equals("probe")) {
            String id = data.getString("identifier");

            if (id.length() > 32) {
                return;
            }

            var tag = new CompoundTag();
            tag.putInt("menu", menu.containerId);
            tag.putBoolean("probe", true);
            tag.putString("identifier", id);
            tag.putBoolean("duplicate", directory.duplicate(id, reader.getBlockPos()) != null);
            send(player, tag);

            return;
        }

        if (action.equals("copy")) {
            var source = directory.duplicate(data.getString("identifier"), reader.getBlockPos());

            if (source != null) {
                reader.configure(source);
            }

            reply(
                    player,
                    menu,
                    source == null
                            ? "gui.scp_genesis.reader.no_duplicate"
                            : "gui.scp_genesis.reader.copied");

            return;
        }

        if (action.equals("open") || action.equals("close")) {
            if (reader.configuration().type() == ReaderType.LOCKED) {
                reply(player, menu, "gui.scp_genesis.reader.locked");

                return;
            }

            if (action.equals("open")) {
                reader.open();
            } else {
                reader.close();
            }

            reply(player, menu, "");

            return;
        }

        if (!action.equals("save") && !action.equals("link")) {
            return;
        }

        var next = ReaderConfiguration.load(data.getCompound("configuration"));

        if (next.identifier().length() > 32
                || !next.identifier().matches("[A-Za-z0-9_. -]*")
                || next.code().length() > 12
                || !next.code().matches("[0-9]*")
                || next.type() == ReaderType.ID) {
            reply(player, menu, "gui.scp_genesis.reader.invalid");

            return;
        }

        if (directory.duplicate(next.identifier(), reader.getBlockPos()) != null
                && !next.identifier().equals(reader.configuration().identifier())) {
            reply(player, menu, "gui.scp_genesis.reader.duplicate");

            return;
        }

        // A locked reader can be unlocked with the configurator, but no links or controls edited
        // while staying locked.
        if (reader.configuration().type() == ReaderType.LOCKED
                && next.type() == ReaderType.LOCKED) {
            reply(player, menu, "gui.scp_genesis.reader.locked");

            return;
        }

        for (int slot = 0; slot < 2; slot++) {
            BlockPos pos = next.door(slot);

            if (pos == null || pos.equals(reader.configuration().door(slot))) {
                continue;
            }

            BlockPos base = ReaderDoorController.resolve(player.serverLevel(), pos);

            if (base == null) {
                reply(player, menu, "gui.scp_genesis.reader.invalid_door");

                return;
            }

            next = next.withDoor(slot, base);
        }

        if (next.type() == ReaderType.CODE && next.code().isEmpty()) {
            reply(player, menu, "gui.scp_genesis.reader.code_required");

            return;
        }

        reader.configure(next);

        if (action.equals("link") && next.type() != ReaderType.LOCKED) {
            int slot = data.getInt("slot");

            if (slot >= 0 && slot < 2) {
                ReaderConfiguratorItem.startLink(player, reader, slot);
            }
        } else {
            reply(player, menu, "gui.scp_genesis.reader.saved");
        }
    }

    private static void send(ServerPlayer player, CompoundTag data) {
        PlatformServices.get().sendReader(player, new ReaderPacket(data));
    }

    private static void reply(ServerPlayer player, ReaderMenu menu, String message) {
        var tag = new CompoundTag();
        tag.putInt("menu", menu.containerId);
        tag.putBoolean("configuring", menu.configuring);
        tag.putString("message", message);
        tag.putString("status", menu.reader.status().getSerializedName());

        if (menu.configuring) {
            tag.put("configuration", menu.reader.configuration().save());
        }

        send(player, tag);
    }
}
