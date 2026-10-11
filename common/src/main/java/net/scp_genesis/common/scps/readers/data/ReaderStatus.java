package net.scp_genesis.common.scps.readers.data;

import net.minecraft.util.StringRepresentable;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/** Visible phases of one access attempt. */
public enum ReaderStatus implements StringRepresentable {
    BASE,
    OPENING,
    CLOSING,
    ERROR;

    @NotNull
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
