package net.scp_genesis.common.copycatblocks.geometry.panel;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.geometry.*;
import net.scp_genesis.common.copycatblocks.util.records.*;
import java.util.List;
import static net.scp_genesis.common.copycatblocks.block.custom.basic.CopycatPanelBlock.*;

/** Panel meshes with two separately tinted components and no shared internal surface. */
public final class CopycatPanelGeometry {
    private CopycatPanelGeometry() {}
    /** BOTTOM/TOP store canonical left/right materials; singles always use MAIN. */
    public static List<CopycatMeshPart> parts(BlockState state) {
        Direction facing = state.getValue(FACING);
        if (!state.getValue(DOUBLE)) {
            return List.of(part(CopycatPart.MAIN, state.getValue(SIDE), facing, false));
        }
        return List.of(part(CopycatPart.BOTTOM, CopycatHalfSide.LEFT, facing, true),
                part(CopycatPart.TOP, CopycatHalfSide.RIGHT, facing, true));
    }
    private static CopycatMeshPart part(CopycatPart part, CopycatHalfSide side, Direction facing, boolean joined) {
        float x = side == CopycatHalfSide.LEFT ? 0 : 0.5F;
        Direction omitted = joined ? (side == CopycatHalfSide.LEFT ? Direction.EAST : Direction.WEST) : null;
        var faces = CopycatGeometryTransforms.orient(CopycatBoxGeometry.faces(x,0,0,x+0.5F,1,1,omitted), facing, Half.BOTTOM);
        CopycatUV[][] uv = new CopycatUV[faces.length][];
        for (int i=0; i<faces.length; i++) {
            uv[i] = new CopycatUV[faces[i].vertices().length];
            for (int j=0; j<uv[i].length; j++) {
                uv[i][j] = CopycatUVMapping.project(faces[i].direction(), faces[i].vertices()[j]);
            }
        }
        return new CopycatMeshPart(part, faces, uv);
    }
}
