package net.scp_genesis.common.copycatblocks.geometry.stairs.half;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.geometry.*;
import net.scp_genesis.common.copycatblocks.util.records.*;
import java.util.List;
import static net.scp_genesis.common.copycatblocks.block.custom.half.CopycatHalfStairsBlock.*;

/** Half-stair meshes with two separately tinted components and no shared internal surface. */
public final class CopycatHalfStairsGeometry {
    private CopycatHalfStairsGeometry() {}
    /** BOTTOM/TOP store canonical left/right materials; singles always use MAIN. */
    public static List<CopycatMeshPart> parts(BlockState state) {
        Direction facing = state.getValue(FACING);
        if (!state.getValue(DOUBLE)) {
            return List.of(part(CopycatPart.MAIN, state.getValue(SIDE), facing, state.getValue(HALF), false));
        }
        return List.of(part(CopycatPart.BOTTOM, CopycatHalfSide.LEFT, facing, state.getValue(HALF), true),
                part(CopycatPart.TOP, CopycatHalfSide.RIGHT, facing, state.getValue(HALF), true));
    }
    private static CopycatMeshPart part(CopycatPart part, CopycatHalfSide side, Direction facing, Half half, boolean joined) {
        float x = side == CopycatHalfSide.LEFT ? 0 : 0.5F;
        Direction omitted = joined ? (side == CopycatHalfSide.LEFT ? Direction.EAST : Direction.WEST) : null;
        var faces = CopycatGeometryTransforms.orient(canonical(x, omitted), facing, half);
        CopycatUV[][] uv = new CopycatUV[faces.length][];
        for (int i=0; i<faces.length; i++) {
            uv[i] = new CopycatUV[faces[i].vertices().length];
            for (int j=0; j<uv[i].length; j++) {
                uv[i][j] = CopycatUVMapping.project(faces[i].direction(), faces[i].vertices()[j]);
            }
        }
        return new CopycatMeshPart(part, faces, uv);
    }
    /** Builds the exposed surface of three half-height cells, excluding every shared face. */
    private static CopycatFace[] canonical(float x, Direction seam) {
        var result = new java.util.ArrayList<CopycatFace>();
        addCell(result, x, 0, 0, seam, Direction.UP, Direction.SOUTH);
        addCell(result, x, 0, 0.5F, seam, Direction.NORTH);
        addCell(result, x, 0.5F, 0, seam, Direction.DOWN);
        return result.toArray(CopycatFace[]::new);
    }
    private static void addCell(java.util.List<CopycatFace> result, float x, float y, float z,
                                Direction seam, Direction... hidden) {
        for (var face : CopycatBoxGeometry.faces(x,y,z,x+0.5F,y+0.5F,z+0.5F,seam)) {
            if (java.util.Arrays.stream(hidden).noneMatch(direction -> direction == face.direction())) result.add(face);
        }
    }
}
