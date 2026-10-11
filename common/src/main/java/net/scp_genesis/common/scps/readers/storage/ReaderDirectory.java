package net.scp_genesis.common.scps.readers.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.scp_genesis.common.scps.readers.data.ReaderConfiguration;
import net.scp_genesis.common.scps.readers.data.ReaderDirectoryEntry;
import net.scp_genesis.common.scps.readers.data.ReaderType;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Dimension-local index: duplicate IDs and door commands survive chunk and world reloads. */
public final class ReaderDirectory extends SavedData {

    private final Map<BlockPos, ReaderDirectoryEntry> entries = new HashMap<>();

    private final Map<BlockPos, Set<BlockPos>> byDoor = new HashMap<>();

    public static ReaderDirectory get(ServerLevel level) {
        return level.getDataStorage()
                .computeIfAbsent(
                        new Factory<>(ReaderDirectory::new, ReaderDirectory::load, null),
                        "scp_readers");
    }

    private static ReaderDirectory load(CompoundTag tag, HolderLookup.Provider registries) {
        var directory = new ReaderDirectory();

        for (var value : tag.getList("readers", Tag.TAG_COMPOUND)) {
            var entry = (CompoundTag) value;
            directory.update(
                    BlockPos.of(entry.getLong("pos")),
                    ReaderConfiguration.load(entry.getCompound("config")),
                    entry.getLong("until"));
        }

        directory.setDirty(false);

        return directory;
    }

    @Override
    @NotNull
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        var list = new ListTag();
        entries.forEach(
                (pos, value) -> {
                    var entry = new CompoundTag();
                    entry.putLong("pos", pos.asLong());
                    entry.put("config", value.configuration().save());
                    entry.putLong("until", value.openUntil());
                    list.add(entry);
                });
        tag.put("readers", list);

        return tag;
    }

    public void update(BlockPos pos, ReaderConfiguration config, long until) {
        var value = new ReaderDirectoryEntry(config, until);
        var old = entries.get(pos);

        if (value.equals(old)) {
            return;
        }

        if (old != null) {
            unindex(pos, old.configuration());
        }

        entries.put(pos.immutable(), value);

        for (int slot = 0; slot < 2; slot++) {
            var door = config.door(slot);

            if (door != null) {
                byDoor.computeIfAbsent(door, key -> new HashSet<>()).add(pos.immutable());
            }
        }

        setDirty();
    }

    private void unindex(BlockPos pos, ReaderConfiguration config) {
        for (int slot = 0; slot < 2; slot++) {
            var door = config.door(slot);
            var readers = byDoor.get(door);

            if (readers != null) {
                readers.remove(pos);

                if (readers.isEmpty()) {
                    byDoor.remove(door);
                }
            }
        }
    }

    public void remove(BlockPos pos) {
        var old = entries.remove(pos);

        if (old != null) {
            unindex(pos, old.configuration());
            setDirty();
        }
    }

    public ReaderConfiguration duplicate(String identifier, BlockPos except) {
        if (identifier.isBlank()) {
            return null;
        }

        return entries.entrySet().stream()
                .filter(
                        e ->
                                !e.getKey().equals(except)
                                        && e.getValue()
                                                .configuration()
                                                .identifier()
                                                .equals(identifier))
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getValue().configuration())
                .findFirst()
                .orElse(null);
    }

    public boolean controls(BlockPos door) {
        return byDoor.containsKey(door);
    }

    public boolean wantsOpen(BlockPos door, long time) {
        return byDoor.getOrDefault(door, Set.of()).stream()
                .map(entries::get)
                .anyMatch(
                        e -> e.configuration().type() != ReaderType.LOCKED && e.openUntil() > time);
    }
}
