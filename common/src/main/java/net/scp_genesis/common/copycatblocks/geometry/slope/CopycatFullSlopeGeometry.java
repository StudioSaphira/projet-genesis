package net.scp_genesis.common.copycatblocks.geometry.slope;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.block.custom.basic.CopycatVerticalSlopeBlock;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.*;
import net.scp_genesis.common.copycatblocks.util.records.*;
import java.util.*;
import static net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock.*;

/** Exact complementary triangular prisms for horizontal and vertical full slopes. */
public final class CopycatFullSlopeGeometry {
    private CopycatFullSlopeGeometry() {}
    public static boolean vertical(BlockState state) { return state.getBlock() instanceof CopycatVerticalSlopeBlock; }
    /** The complementary prism preserves the diagonal plane while occupying its other side. */
    public static BlockState complement(BlockState state) {
        state = state.setValue(FACING, state.getValue(FACING).getOpposite());
        return vertical(state) ? state : state.setValue(HALF, state.getValue(HALF) == Half.BOTTOM ? Half.TOP : Half.BOTTOM);
    }
    /** Reflects the canonical horizontal prism across X=Y to stand its diagonal vertically. */
    public static CopycatFace[] singleFaces(BlockState state) {
        if (!vertical(state)) return CopycatGeometrySlope.getFaces(state.getValue(FACING), state.getValue(HALF));
        var faces = CopycatGeometrySlope.getDefaultFaces();
        for (int i=0;i<faces.length;i++) {
            var source=faces[i].vertices(); var vertices=new CopycatVertex[source.length];
            for(int j=0;j<source.length;j++) vertices[source.length-1-j]=new CopycatVertex(source[j].y(),source[j].x(),source[j].z());
            Direction direction=switch(faces[i].direction()) {
                case UP -> Direction.EAST; case DOWN -> Direction.WEST;
                case EAST -> Direction.UP; case WEST -> Direction.DOWN; default -> faces[i].direction();
            };
            faces[i]=new CopycatFace(direction,vertices);
        }
        return CopycatGeometryTransforms.orient(faces,state.getValue(FACING),Half.BOTTOM);
    }
    /** Omits the internal diagonal of doubles; MAIN becomes BOTTOM and the new side uses TOP/ALT. */
    public static List<CopycatMeshPart> parts(BlockState state) {
        if (!state.getValue(DOUBLE)) return List.of(part(state,CopycatPart.MAIN,false));
        return List.of(part(state,CopycatPart.BOTTOM,true),part(complement(state),CopycatPart.TOP,true));
    }
    private static CopycatMeshPart part(BlockState state, CopycatPart slot, boolean joined) {
        var faces=Arrays.stream(singleFaces(state)).filter(f -> !joined || CopycatFaceBounds.boundaryDirection(f)!=null).toArray(CopycatFace[]::new);
        var uv=new CopycatUV[faces.length][];
        for(int i=0;i<faces.length;i++) uv[i]=CopycatSlopeUV.calculate(faces[i]);
        return new CopycatMeshPart(slot,faces,uv);
    }
    /** Shared outline surface, including both outer halves in double form. */
    public static CopycatFace[] faces(BlockState state) {
        return parts(state).stream().flatMap(p -> Arrays.stream(p.faces())).toArray(CopycatFace[]::new);
    }
}
