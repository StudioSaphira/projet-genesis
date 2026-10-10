package net.scp_genesis.common.scps.readers;
import net.minecraft.util.StringRepresentable;
/** Blank is a distinct visual state, never an accreditation level. */
public enum ReaderLevel implements StringRepresentable {
    BLANK(-1), ZERO(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6);
    public final int value;
    ReaderLevel(int value) { this.value = value; }
    public String getSerializedName() { return value < 0 ? "blank" : Integer.toString(value); }
    public static ReaderLevel of(int value) { return values()[Math.max(-1, Math.min(6, value)) + 1]; }
}
