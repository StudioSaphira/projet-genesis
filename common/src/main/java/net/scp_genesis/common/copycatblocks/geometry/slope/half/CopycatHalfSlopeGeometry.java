package net.scp_genesis.common.copycatblocks.geometry.slope.half;

import net.scp_genesis.common.copycatblocks.util.abstracts.CopycatHalfState;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.geometry.CopycatGeometryTransforms;
import net.scp_genesis.common.copycatblocks.geometry.CopycatUVMapping;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatGeometrySlope;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatSlopeUV;
import net.scp_genesis.common.copycatblocks.util.records.*;
import java.util.ArrayList;
import java.util.List;
import static net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock.FACING;

/** Builds the exact half-width prisms and removes their shared internal faces before rendering. */
public final class CopycatHalfSlopeGeometry {
    private CopycatHalfSlopeGeometry() {}

    /** Returns fresh components; BOTTOM/TOP are stable material slots, not world-space heights. */
    public static List<CopycatMeshPart> parts(BlockState state) {
        var form = CopycatHalfState.form(state);
        var side = CopycatHalfState.side(state);
        var half = CopycatHalfState.half(state);
        var facing = state.getValue(FACING);
        if (form == CopycatHalfForm.SINGLE) {
            return List.of(mesh(CopycatPart.MAIN, side, half, facing, false, null));
        }
        if (form == CopycatHalfForm.HORIZONTAL) {
            return List.of(mesh(CopycatPart.BOTTOM, CopycatHalfSide.LEFT, half, facing, false, Direction.EAST),
                    mesh(CopycatPart.TOP, CopycatHalfSide.RIGHT, half, facing, false, Direction.WEST));
        }
        // The second prism is the complement Y+Z>=1, not a simple vertical reflection.
        return List.of(mesh(CopycatPart.BOTTOM, side, Half.BOTTOM, facing, false, Direction.SOUTH),
                mesh(CopycatPart.TOP, side, Half.BOTTOM, facing, true, Direction.SOUTH));
    }

    private static CopycatMeshPart mesh(CopycatPart part, CopycatHalfSide side, Half half,
                                        Direction facing, boolean complement, Direction omitted) {
        List<CopycatFace> faces = new ArrayList<>();
        for (CopycatFace face : CopycatGeometrySlope.getDefaultFaces()) {
            if (face.direction() == omitted) continue;
            CopycatVertex[] vertices = new CopycatVertex[face.vertices().length];
            for (int i = 0; i < vertices.length; i++) {
                var v = face.vertices()[i];
                vertices[i] = new CopycatVertex(v.x() * 0.5F + (side == CopycatHalfSide.RIGHT ? 0.5F : 0),
                        complement ? 1 - v.y() : v.y(), complement ? 1 - v.z() : v.z());
            }
            Direction direction = face.direction();
            if (complement && direction.getAxis() != Direction.Axis.X) direction = direction.getOpposite();
            faces.add(new CopycatFace(direction, vertices));
        }
        var oriented = CopycatGeometryTransforms.orient(faces.toArray(CopycatFace[]::new), facing, half);
        CopycatUV[][] uv = new CopycatUV[oriented.length][];
        for (int i = 0; i < oriented.length; i++) {
            uv[i] = CopycatSlopeUV.calculate(oriented[i]);
            for (int j = 0; j < uv[i].length; j++) uv[i][j] = CopycatUVMapping.clamp(uv[i][j]);
        }
        return new CopycatMeshPart(part, oriented, uv);
    }

    /** Returns all exterior polygons for selection outlines. */
    public static CopycatFace[] faces(BlockState state) {
        return parts(state).stream().flatMap(p -> java.util.Arrays.stream(p.faces())).toArray(CopycatFace[]::new);
    }
}
