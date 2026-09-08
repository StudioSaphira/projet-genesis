package net.scp_genesis.fabric.copycatblocks.renderer.util.dedicated;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.CopycatFace;
import net.scp_genesis.common.copycatblocks.geometry.CopycatUV;
import net.scp_genesis.common.copycatblocks.geometry.CopycatVector;
import net.scp_genesis.common.copycatblocks.geometry.CopycatGeometryMath;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatSlopeOcclusion;
import net.scp_genesis.common.copycatblocks.provider.CopycatModelProvider;
import net.scp_genesis.fabric.copycatblocks.renderer.FabricCopycatRenderer;
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatQuadHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public final class FabricCopycatSlopeHelper {

    private FabricCopycatSlopeHelper() {}

    /*
     * ================================================================
     * RETEXTURED SLOPE
     * ================================================================
     */

    public static void retexture(
            @NotNull CopycatFace[] faces,
            @NotNull CopycatUV[][] uv,
            @NotNull BlockState copiedState,
            @NotNull Supplier<RandomSource> randomSupplier,
            @NotNull RenderContext context,
            @NotNull CopycatPart part
    ) {
        BakedModel copiedModel =
                CopycatModelProvider.getModel(
                        copiedState
                );

        QuadEmitter emitter =
                context.getEmitter();

        RandomSource random =
                randomSupplier.get();

        for (int i = 0; i < faces.length; i++) {

            CopycatFace face =
                    faces[i];

            CopycatUV[] faceUv =
                    uv[i];

            List<BakedQuad> copiedQuads =
                    findCopiedQuads(
                            copiedModel,
                            copiedState,
                            face.direction(),
                            random
                    );

            if (copiedQuads.isEmpty()) {
                continue;
            }

            for (BakedQuad copiedQuad : copiedQuads) {

                emitRetexturedFace(
                        emitter,
                        face,
                        faceUv,
                        copiedQuad,
                        part
                );
            }
        }
    }

    /*
     * ================================================================
     * SOURCE QUAD
     * ================================================================
     */

    @NotNull
    public static List<BakedQuad> findCopiedQuads(
            @NotNull BakedModel copiedModel,
            @NotNull BlockState copiedState,
            @NotNull Direction direction,
            @NotNull RandomSource random
    ) {
        List<BakedQuad> quads =
                copiedModel.getQuads(
                        copiedState,
                        direction,
                        random
                );

        if (!quads.isEmpty()) {
            return quads;
        }

        return copiedModel.getQuads(
                copiedState,
                null,
                random
        );
    }

    /*
     * ================================================================
     * RETEXTURED FACE
     * ================================================================
     */

    private static void emitRetexturedFace(
            @NotNull QuadEmitter emitter,
            @NotNull CopycatFace face,
            @NotNull CopycatUV[] uv,
            @NotNull BakedQuad copiedQuad,
            @NotNull CopycatPart part
    ) {
        CopycatVector normal =
                CopycatGeometryMath.normalize(
                        CopycatGeometryMath.normal(face)
                );

        Direction cullFace =
                CopycatSlopeOcclusion.supportsFaceCulling(face)
                        ? face.direction()
                        : null;

        /*
         * Copy the vanilla appearance of the source quad.
         *
         * The geometry itself is replaced immediately afterward.
         */
        emitter.fromVanilla(
                copiedQuad,
                FabricCopycatRenderer.getCutoutMaterial(),
                cullFace
        );

        /*
         * ============================================================
         * POSITIONS + NORMALS
         * ============================================================
         */

        int vertexCount =
                face.vertices().length;

        for (int i = 0; i < vertexCount; i++) {

            var vertex =
                    face.vertices()[i];

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
        }

        /*
         * BakedQuad / QuadEmitter always use four vertices.
         */
        if (vertexCount == 3) {

            var vertex =
                    face.vertices()[2];

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
        }

        /*
         * ============================================================
         * UV
         * ============================================================
         *
         * Common UV coordinates are already normalized to 0..1.
         *
         * BAKE_NORMALIZED tells Fabric not to interpret them as
         * conventional 0..16 Minecraft model coordinates.
         */
        for (int i = 0; i < uv.length; i++) {

            emitter.uv(
                    i,
                    uv[i].u(),
                    uv[i].v()
            );
        }

        if (vertexCount == 3) {
            emitter.uv(
                    3,
                    uv[2].u(),
                    uv[2].v()
            );
        }

        emitter.spriteBake(
                copiedQuad.getSprite(),
                MutableQuadView.BAKE_NORMALIZED
        );

        /*
         * ============================================================
         * FACE / CULLING
         * ============================================================
         */

        emitter.nominalFace(
                cullFace
        );

        /*
         * ============================================================
         * TINT
         * ============================================================
         */

        emitter.colorIndex(
                FabricCopycatQuadHelper.encodeCopycatTintIndex(
                        copiedQuad.getTintIndex(),
                        part
                )
        );

        emitter.emit();
    }

    /*
     * ================================================================
     * EMPTY SLOPE
     * ================================================================
     */

    public static void emitEmpty(
            @NotNull CopycatFace[] faces,
            @NotNull CopycatUV[][] uv,
            @NotNull TextureAtlasSprite sprite,
            @NotNull RenderContext context
    ) {
        QuadEmitter emitter =
                context.getEmitter();

        RenderMaterial material =
                FabricCopycatRenderer.getCutoutMaterial();

        for (int i = 0; i < faces.length; i++) {

            CopycatFace face =
                    faces[i];

            CopycatUV[] faceUv =
                    uv[i];

            emitEmptyFace(
                    emitter,
                    face,
                    faceUv,
                    sprite,
                    material
            );
        }
    }

    private static void emitEmptyFace(
            @NotNull QuadEmitter emitter,
            @NotNull CopycatFace face,
            @NotNull CopycatUV[] uv,
            @NotNull TextureAtlasSprite sprite,
            @NotNull RenderMaterial material
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

        for (int i = 0; i < vertexCount; i++) {

            var vertex =
                    face.vertices()[i];

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

        if (vertexCount == 3) {

            var vertex =
                    face.vertices()[2];

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

        emitter.spriteBake(
                sprite,
                MutableQuadView.BAKE_NORMALIZED
        );

        emitter.material(material);

        emitter.nominalFace(
                cullFace
        );

        emitter.emit();
    }
}