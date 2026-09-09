package net.scp_genesis.fabric.copycatblocks.renderer.model.dedicated;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Half;

import net.scp_genesis.common.copycatblocks.geometry.CopycatFace;
import net.scp_genesis.common.copycatblocks.geometry.CopycatGeometryMath;
import net.scp_genesis.common.copycatblocks.geometry.CopycatUV;
import net.scp_genesis.common.copycatblocks.geometry.CopycatVector;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatGeometrySlope;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatSlopeOcclusion;

import net.scp_genesis.fabric.copycatblocks.renderer.FabricCopycatRenderer;

import org.jetbrains.annotations.NotNull;

public final class FabricCopycatSlopeItemModel {

    private FabricCopycatSlopeItemModel() {
    }

    public static void emit(
            @NotNull TextureAtlasSprite sprite,
            @NotNull RenderContext context
    ) {
        QuadEmitter emitter = context.getEmitter();

        /*
         * ============================================================
         * DEFAULT SLOPE STATE
         * ============================================================
         *
         * The item represents the default Slope orientation.
         *
         * Block default:
         *   FACING = NORTH
         *   HALF   = BOTTOM
         */
        CopycatFace[] faces =
                CopycatGeometrySlope.getFaces(
                        Direction.NORTH,
                        Half.BOTTOM
                );

        CopycatUV[][] uv =
                CopycatGeometrySlope.getUV(
                        Direction.NORTH,
                        Half.BOTTOM
                );

        for (int i = 0; i < faces.length; i++) {

            emitFace(
                    emitter,
                    faces[i],
                    uv[i],
                    sprite
            );
        }
    }

    private static void emitFace(
            @NotNull QuadEmitter emitter,
            @NotNull CopycatFace face,
            @NotNull CopycatUV[] uv,
            @NotNull TextureAtlasSprite sprite
    ) {
        CopycatVector normal =
                CopycatGeometryMath.normalize(
                        CopycatGeometryMath.normal(face)
                );

        int vertexCount =
                face.vertices().length;

        Direction cullFace =
                CopycatSlopeOcclusion.supportsFaceCulling(face)
                        ? face.direction()
                        : null;

        /*
         * ============================================================
         * POSITIONS + NORMALS + UV
         * ============================================================
         */

        for (int i = 0; i < vertexCount; i++) {

            var vertex = face.vertices()[i];

            emitter.pos(
                    i,
                    vertex.x(),
                    vertex.y(),
                    vertex.z()
            );

            emitter.normal(
                    i,
                    normal.x(),
                    normal.y(),
                    normal.z()
            );

            emitter.uv(
                    i,
                    uv[i].u(),
                    uv[i].v()
            );
        }

        /*
         * QuadEmitter always expects four vertices.
         *
         * The WEST/EAST triangular faces therefore duplicate
         * their third vertex.
         */
        if (vertexCount == 3) {

            var vertex = face.vertices()[2];

            emitter.pos(
                    3,
                    vertex.x(),
                    vertex.y(),
                    vertex.z()
            );

            emitter.normal(
                    3,
                    normal.x(),
                    normal.y(),
                    normal.z()
            );

            emitter.uv(
                    3,
                    uv[2].u(),
                    uv[2].v()
            );
        }

        /*
         * ============================================================
         * TEXTURE
         * ============================================================
         */

        emitter.color(
                0xFFFFFFFF,
                0xFFFFFFFF,
                0xFFFFFFFF,
                0xFFFFFFFF
        );

        emitter.spriteBake(
                sprite,
                MutableQuadView.BAKE_NORMALIZED
        );

        /*
         * ============================================================
         * MATERIAL / CULLING
         * ============================================================
         */

        emitter.material(
                FabricCopycatRenderer.getCutoutMaterial()
        );

        emitter.nominalFace(cullFace);

        emitter.colorIndex(-1);

        emitter.emit();
    }
}