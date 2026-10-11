package net.scp_genesis.common.scps.readers.data;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/** Blank is a distinct visual state, never an accreditation level. */
public enum ReaderLevel implements StringRepresentable {
    BLANK(-1),
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6);

    public final int value;

    ReaderLevel(int value) {
        this.value = value;
    }

    public @NotNull String getSerializedName() {
        return value < 0 ? "blank" : Integer.toString(value);
    }

    public static ReaderLevel of(int value) {
        return values()[Math.clamp(value, -1, 6) + 1];
    }
}
