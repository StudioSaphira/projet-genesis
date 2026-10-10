package net.scp_genesis.common.scps.readers;
import net.minecraft.util.StringRepresentable;
/** Visible phases of one access attempt. */
public enum ReaderStatus implements StringRepresentable {
    BASE, OPENING, CLOSING, ERROR;
    public String getSerializedName() { return name().toLowerCase(java.util.Locale.ROOT); }
}
