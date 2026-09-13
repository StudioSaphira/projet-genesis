package net.scp_genesis.neoforge.copycatblocks.renderer.geometry;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatGeometrySlope;
import net.scp_genesis.neoforge.copycatblocks.renderer.model.NeoForgeCopycatBakedModel;
import net.scp_genesis.neoforge.furnitures.NeoForgeFurnituresLockerModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.TriState;

public final class SlopeLightingTest {
    public static void main(String[] args) {
        for (Direction facing : Direction.Plane.HORIZONTAL) {
            for (Half half : Half.values()) {
                var faces = CopycatGeometrySlope.getFaces(facing, half);
                int unculled = 0, culled = 0;
                for (int i = 0; i < faces.length; i++) {
                    Direction bucket = NeoForgeCopycatGeometrySlope.boundaryDirection(faces[i]);
                    if (bucket == null) unculled++; else culled++;
                    // Face 2 is the sloping surface; even a solid neighbor must not hide it.
                    if ((i == 2) != (bucket == null)) throw new AssertionError("Incorrect diagonal culling: " + facing + half);
                }
                if (unculled != 1 || culled != 4) throw new AssertionError("Each of the five faces needs exactly one bucket");
            }
        }
        var slope = new NeoForgeCopycatBakedModel(new NeoForgeCopycatGeometrySlope(null, null));
        var locker = new NeoForgeFurnituresLockerModel(null);
        if (slope.useAmbientOcclusion() || locker.useAmbientOcclusion()
                || slope.useAmbientOcclusion(null, ModelData.EMPTY, null) != TriState.FALSE
                || locker.useAmbientOcclusion(null, ModelData.EMPTY, null) != TriState.FALSE) {
            throw new AssertionError("Irregular meshes must bypass cube AO through both rendering entry points");
        }
        System.out.println("PASS: eight slope orientations, diagonal never culled, five unique faces, legacy and NeoForge AO entry points.");
    }
}
