package net.scp_genesis.common.scps.readers.data;

import net.minecraft.util.StringRepresentable;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/** Configurable access method; ID is reserved and cannot be selected yet. */
public enum ReaderType implements StringRepresentable {
    ID,
    CODE,
    BUTTON,
    KEYCARD,
    LOCKED;

    @NotNull
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static ReaderType parse(String value) {
        for (var type : values()) {
            if (type.getSerializedName().equals(value)) {
                return type;
            }
        }

        return LOCKED;
    }
}
