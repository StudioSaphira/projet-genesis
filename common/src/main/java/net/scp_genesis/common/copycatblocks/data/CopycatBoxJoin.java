package net.scp_genesis.common.copycatblocks.data;
import net.minecraft.util.StringRepresentable;
/** Arrangement of two rectangular quarter-block components within the same block space. */
public enum CopycatBoxJoin implements StringRepresentable {
    NONE("none"), SIDE("horizontal"), DEPTH("depth"), STACK("vertical");
    private final String name;
    CopycatBoxJoin(String name) { this.name=name; }
    @Override public String getSerializedName() { return name; }
}
