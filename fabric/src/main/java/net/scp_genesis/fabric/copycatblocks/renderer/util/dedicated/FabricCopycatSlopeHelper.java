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
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatSlopeFace;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatSlopeOcclusion;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatSlopeVertex;
import net.scp_genesis.common.copycatblocks.provider.CopycatModelProvider;
import net.scp_genesis.fabric.copycatblocks.renderer.FabricCopycatRenderer;
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatQuadHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public final class FabricCopycatSlopeHelper {

    private FabricCopycatSlopeHelper() {
    }

    /**
     * Emits a complete Copycat Slope using the appearance
     * of the copied block.
     */
    public static void retexture(
            @NotNull CopycatSlopeFace[] faces,
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

        for (CopycatSlopeFace face : faces) {

            BakedQuad copiedQuad =
                    findCopiedQuad(
                            copiedModel,
                            copiedState,
                            face.direction(),
                            random
                    );

            if (copiedQuad == null) {
                continue;
            }

            emitRetexturedFace(
                    emitter,
                    face,
                    copiedQuad,
                    part
            );
        }
    }

    /**
     * Finds a representative quad from the copied block model
     * for the requested Slope face.
     */
    public static @Nullable BakedQuad findCopiedQuad(
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
            return quads.getFirst();
        }

        /*
         * Some models do not expose a quad on every direction.
         *
         * Fall back to general quads.
         */
        List<BakedQuad> generalQuads =
                copiedModel.getQuads(
                        copiedState,
                        null,
                        random
                );

        if (!generalQuads.isEmpty()) {
            return generalQuads.getFirst();
        }

        return null;
    }

    /**
     * Emits one Slope face using the appearance of a copied quad.
     */
    public static void emitRetexturedFace(
            @NotNull QuadEmitter emitter,
            @NotNull CopycatSlopeFace face,
            @NotNull BakedQuad copiedQuad,
            @NotNull CopycatPart part
    ) {
        CopycatSlopeVertex[] vertices =
                face.vertices();

        emitter.fromVanilla(
                copiedQuad,
                FabricCopycatRenderer.getCutoutMaterial(),
                face.direction()
        );

        /*
         * Replace the copied model geometry with the actual
         * Copycat Slope geometry.
         */
        for (int i = 0; i < vertices.length; i++) {
            setVertex(
                    emitter,
                    i,
                    vertices[i]
            );
        }

        /*
         * Fabric requires four vertices.
         *
         * A triangular Slope face is represented by a degenerate quad.
         */
        if (CopycatSlopeOcclusion.isTriangle(face)) {
            setVertex(
                    emitter,
                    3,
                    vertices[2]
            );
        }

        /*
         * Rebuild UV coordinates according to the Slope geometry.
         */
        setSlopeFaceUv(
                emitter,
                face
        );

        /*
         * Only complete faces can participate in normal
         * block-face culling.
         *
         * Triangular faces must remain non-cullable.
         */
        if (CopycatSlopeOcclusion.supportsFaceCulling(face)) {
            emitter.nominalFace(
                    face.direction()
            );
        } else {
            emitter.nominalFace(
                    null
            );
        }

        emitter.colorIndex(
                FabricCopycatQuadHelper.encodeCopycatTintIndex(
                        copiedQuad.getTintIndex(),
                        part
                )
        );

        emitter.emit();
    }

    /**
     * Generates UV coordinates from the actual Slope geometry.
     */
    private static void setSlopeFaceUv(
            @NotNull QuadEmitter emitter,
            @NotNull CopycatSlopeFace face
    ) {
        CopycatSlopeVertex[] vertices =
                face.vertices();

        for (int i = 0; i < vertices.length; i++) {
            setSlopeVertexUv(
                    emitter,
                    i,
                    vertices[i],
                    face.direction()
            );
        }

        /*
         * Duplicate the third UV for triangular faces.
         */
        if (CopycatSlopeOcclusion.isTriangle(face)) {
            setSlopeVertexUv(
                    emitter,
                    3,
                    vertices[2],
                    face.direction()
            );
        }

        emitter.spriteBake(
                null,
                MutableQuadView.BAKE_LOCK_UV
        );
    }

    private static void setSlopeVertexUv(
            @NotNull QuadEmitter emitter,
            int index,
            @NotNull CopycatSlopeVertex vertex,
            @NotNull Direction direction
    ) {
        float u;
        float v;

        switch (direction) {

            case NORTH, SOUTH -> {
                u = vertex.x();
                v = vertex.y();
            }

            case EAST, WEST -> {
                u = vertex.z();
                v = vertex.y();
            }

            case DOWN -> {
                u = vertex.x();
                v = vertex.z();
            }

            case UP -> {
                u = vertex.x();
                v = (vertex.y() + vertex.z()) * 0.5F;
            }

            default -> {
                u = 0.0F;
                v = 0.0F;
            }
        }

        emitter.uv(
                index,
                u,
                v
        );
    }

    /**
     * Emits an empty Copycat Slope using its default texture.
     */
    public static void emitEmpty(
            @NotNull CopycatSlopeFace[] faces,
            @NotNull TextureAtlasSprite sprite,
            @NotNull RenderContext context
    ) {
        QuadEmitter emitter =
                context.getEmitter();

        RenderMaterial material =
                FabricCopycatRenderer.getCutoutMaterial();

        for (CopycatSlopeFace face : faces) {

            CopycatSlopeVertex[] vertices =
                    face.vertices();

            if (CopycatSlopeOcclusion.isQuad(face)) {

                emitQuad(
                        emitter,
                        vertices,
                        face.direction(),
                        sprite,
                        material
                );

            } else if (CopycatSlopeOcclusion.isTriangle(face)) {

                emitTriangle(
                        emitter,
                        vertices,
                        face.direction(),
                        sprite,
                        material
                );
            }
        }
    }

    private static void emitQuad(
            @NotNull QuadEmitter emitter,
            @NotNull CopycatSlopeVertex[] vertices,
            @NotNull Direction direction,
            @NotNull TextureAtlasSprite sprite,
            @NotNull RenderMaterial material
    ) {
        for (int i = 0; i < 4; i++) {
            setVertex(
                    emitter,
                    i,
                    vertices[i]
            );
        }

        setFullFaceUv(
                emitter
        );

        emitter.spriteBake(
                sprite,
                MutableQuadView.BAKE_LOCK_UV
        );

        emitter.material(material);
        emitter.nominalFace(direction);
        emitter.emit();
    }

    private static void emitTriangle(
            @NotNull QuadEmitter emitter,
            @NotNull CopycatSlopeVertex[] vertices,
            @NotNull Direction direction,
            @NotNull TextureAtlasSprite sprite,
            @NotNull RenderMaterial material
    ) {
        setVertex(
                emitter,
                0,
                vertices[0]
        );

        setVertex(
                emitter,
                1,
                vertices[1]
        );

        setVertex(
                emitter,
                2,
                vertices[2]
        );

        setVertex(
                emitter,
                3,
                vertices[2]
        );

        emitter.uv(
                0,
                0.0F,
                0.0F
        );

        emitter.uv(
                1,
                1.0F,
                0.0F
        );

        emitter.uv(
                2,
                1.0F,
                1.0F
        );

        emitter.uv(
                3,
                1.0F,
                1.0F
        );

        emitter.spriteBake(
                sprite,
                MutableQuadView.BAKE_LOCK_UV
        );

        emitter.material(material);
        emitter.nominalFace(direction);
        emitter.emit();
    }

    private static void setFullFaceUv(
            @NotNull QuadEmitter emitter
    ) {
        emitter.uv(
                0,
                0.0F,
                0.0F
        );

        emitter.uv(
                1,
                1.0F,
                0.0F
        );

        emitter.uv(
                2,
                1.0F,
                1.0F
        );

        emitter.uv(
                3,
                0.0F,
                1.0F
        );
    }

    private static void setVertex(
            @NotNull QuadEmitter emitter,
            int index,
            @NotNull CopycatSlopeVertex vertex
    ) {
        emitter.pos(
                index,
                vertex.x(),
                vertex.y(),
                vertex.z()
        );
    }
}