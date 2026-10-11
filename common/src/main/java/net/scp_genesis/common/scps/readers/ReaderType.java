package net.scp_genesis.common.scps.readers;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/** Configurable access method; ID is reserved and cannot be selected yet. */
public enum ReaderType implements StringRepresentable {

    ID, CODE, BUTTON, KEYCARD, LOCKED;

    public @NotNull String getSerializedName() { return name().toLowerCase(java.util.Locale.ROOT); }

    public static ReaderType parse(String value) {
        for (var type : values()) if (type.getSerializedName().equals(value)) return type;

        return LOCKED;
    }
}
