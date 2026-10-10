package net.scp_genesis.common.scps.readers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
/** Immutable saved configuration; coordinates belong to the reader's dimension. */
public record ReaderConfiguration(String identifier, ReaderType type, int level, String code,
                                  BlockPos first, BlockPos second) {
    public static final ReaderConfiguration DEFAULT = new ReaderConfiguration("", ReaderType.KEYCARD, 0, "", null, null);
    public ReaderConfiguration {
        identifier = identifier.trim();
        level = type == ReaderType.KEYCARD ? Math.clamp(level, 0, 6) : -1;
        first = first == null ? null : first.immutable();
        second = second == null ? null : second.immutable();
    }
    public BlockPos door(int slot) { return slot == 0 ? first : second; }
    public ReaderConfiguration withDoor(int slot, BlockPos pos) {
        return new ReaderConfiguration(identifier, type, level, code, slot == 0 ? pos : first, slot == 1 ? pos : second);
    }
    public boolean links(BlockPos pos) { return pos.equals(first) || pos.equals(second); }
    public CompoundTag save() {
        var tag = new CompoundTag();
        tag.putString("identifier", identifier); tag.putString("type", type.getSerializedName());
        tag.putInt("level", level); tag.putString("code", code);
        if (first != null) tag.putLong("door0", first.asLong());
        if (second != null) tag.putLong("door1", second.asLong());
        return tag;
    }
    public static ReaderConfiguration load(CompoundTag tag) {
        return new ReaderConfiguration(tag.getString("identifier"), ReaderType.parse(tag.getString("type")),
                tag.getInt("level"), tag.getString("code"),
                tag.contains("door0") ? BlockPos.of(tag.getLong("door0")) : null,
                tag.contains("door1") ? BlockPos.of(tag.getLong("door1")) : null);
    }
}
