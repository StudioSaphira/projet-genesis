package net.scp_genesis.common.scps.readers;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/** Visible phases of one access attempt. */
public enum ReaderStatus implements StringRepresentable {

    BASE, OPENING, CLOSING, ERROR;

    public @NotNull String getSerializedName() { return name().toLowerCase(java.util.Locale.ROOT); }
}
