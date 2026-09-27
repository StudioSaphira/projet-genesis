package net.scp_genesis.common.copycatblocks.geometry;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.*;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.util.abstracts.*;
import net.scp_genesis.common.copycatblocks.util.records.*;
import java.util.*;
import static net.scp_genesis.common.copycatblocks.block.custom.AbstractCopycatHalfBlock.*;
/** Rectangular quarter meshes shared by both loaders, with the joined internal faces omitted. */
public final class CopycatQuarterGeometry {
    private CopycatQuarterGeometry() {}
    /** MAIN is the original single; BOTTOM/TOP are stable primary/secondary material slots in doubles. */
    public static List<CopycatMeshPart> parts(BlockState state) {
        boolean slab=CopycatQuarterState.slab(state);
        float height=slab?0.5F:1, depth=slab?1:0.5F;
        var join=CopycatQuarterState.join(state);
        if(join==CopycatBoxJoin.NONE) return List.of(part(state,CopycatPart.MAIN,0,0,0,.5F,height,depth,null));
        Direction seam=switch(join){case SIDE->Direction.EAST;case DEPTH->Direction.SOUTH;case STACK->Direction.UP;default->throw new IllegalStateException();};
        float x=join==CopycatBoxJoin.SIDE?.5F:0,y=join==CopycatBoxJoin.STACK?.5F:0,z=join==CopycatBoxJoin.DEPTH?.5F:0;
        return List.of(part(state,CopycatPart.BOTTOM,0,0,0,.5F,height,depth,seam),
                part(state,CopycatPart.TOP,x,y,z,x+.5F,y+height,z+depth,seam.getOpposite()));
    }
    private static CopycatMeshPart part(BlockState state,CopycatPart slot,float x,float y,float z,float x2,float y2,float z2,Direction seam) {
        var faces=CopycatGeometryTransforms.orient(CopycatBoxGeometry.faces(x,y,z,x2,y2,z2,seam),state.getValue(FACING),CopycatHalfState.half(state));
        var uv=new CopycatUV[faces.length][];
        for(int i=0;i<faces.length;i++) {
            var face=faces[i];uv[i]=Arrays.stream(face.vertices()).map(v->CopycatUVMapping.project(face.direction(),v)).toArray(CopycatUV[]::new);
        }
        return new CopycatMeshPart(slot,faces,uv);
    }
    /** Axis-aligned bounds of each component provide exact collision, not a stepped approximation. */
    public static VoxelShape shape(BlockState state) {
        VoxelShape result=Shapes.empty();
        for(var part:parts(state)) {
            double minX=1,minY=1,minZ=1,maxX=0,maxY=0,maxZ=0;
            for(var face:part.faces())for(var v:face.vertices()) {
                minX=Math.min(minX,v.x());minY=Math.min(minY,v.y());minZ=Math.min(minZ,v.z());
                maxX=Math.max(maxX,v.x());maxY=Math.max(maxY,v.y());maxZ=Math.max(maxZ,v.z());
            }
            result=Shapes.or(result,Shapes.box(minX,minY,minZ,maxX,maxY,maxZ));
        }
        return result;
    }
}
