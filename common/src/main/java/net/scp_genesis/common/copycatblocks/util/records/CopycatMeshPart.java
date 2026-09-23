package net.scp_genesis.common.copycatblocks.util.records;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
/** A custom mesh component with its own copied material and normalized UVs. */
public record CopycatMeshPart(CopycatPart part, CopycatFace[] faces, CopycatUV[][] uv) {}
