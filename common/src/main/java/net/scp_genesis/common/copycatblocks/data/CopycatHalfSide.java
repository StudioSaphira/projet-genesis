package net.scp_genesis.common.copycatblocks.data;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/** Lateral half in the canonical NORTH frame: LEFT occupies X=0..0.5. */
public enum CopycatHalfSide implements StringRepresentable {
    LEFT, RIGHT;
    @Override public @NotNull String getSerializedName() { return name().toLowerCase(java.util.Locale.ROOT); }
    /** Returns the other lateral half. */
    public CopycatHalfSide opposite() { return this == LEFT ? RIGHT : LEFT; }
}
