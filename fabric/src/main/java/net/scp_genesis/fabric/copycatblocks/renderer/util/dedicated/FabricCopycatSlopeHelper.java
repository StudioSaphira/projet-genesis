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
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatGeometrySlope;
import net.scp_genesis.common.copycatblocks.provider.CopycatModelProvider;
import net.scp_genesis.fabric.copycatblocks.renderer.FabricCopycatRenderer;
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatQuadHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public final class FabricCopycatSlopeHelper {

    private FabricCopycatSlopeHelper() {}

    /**
     * Emits the complete Copycat Slope using the texture
     * of the copied block.
     */
    public static void retexture(
            @NotNull CopycatGeometrySlope.Face[] faces,
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

        for (CopycatGeometrySlope.Face face : faces) {

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
     *
     * <p>The copied quad provides the texture, UV information and
     * tint index. The actual vertex positions come from the canonical
     * Copycat Slope geometry.</p>
     */
    public static void emitRetexturedFace(
            @NotNull QuadEmitter emitter,
            @NotNull CopycatGeometrySlope.Face face,
            @NotNull BakedQuad copiedQuad,
            @NotNull CopycatPart part
    ) {
        CopycatGeometrySlope.Vertex[] vertices =
                face.vertices();

        emitter.fromVanilla(
                copiedQuad,
                FabricCopycatRenderer.getCutoutMaterial(),
                face.direction()
        );

        /*
         * Replace the copied model geometry with the Slope geometry.
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
         * A Slope triangle therefore becomes a degenerate quad.
         */
        if (vertices.length == 3) {
            setVertex(
                    emitter,
                    3,
                    vertices[2]
            );
        }

        /*
         * Rebuild the UVs according to the Slope face.
         */
        setSlopeFaceUv(
                emitter,
                face
        );

        emitter.nominalFace(
                face.direction()
        );

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
     *
     * <p>The projection depends on the orientation of the face.
     * This prevents the side triangles from being stretched like
     * rectangular faces.</p>
     */
    private static void setSlopeFaceUv(
            @NotNull QuadEmitter emitter,
            @NotNull CopycatGeometrySlope.Face face
    ) {
        CopycatGeometrySlope.Vertex[] vertices =
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
         * Duplicate the third UV for triangles.
         */
        if (vertices.length == 3) {

            setSlopeVertexUv(
                    emitter,
                    3,
                    vertices[2],
                    face.direction()
            );
        }

        /*
         * The sprite has already been supplied by fromVanilla().
         *
         * We only need to lock the UV coordinates.
         */
        emitter.spriteBake(
                null,
                MutableQuadView.BAKE_LOCK_UV
        );
    }

    private static void setSlopeVertexUv(
            @NotNull QuadEmitter emitter,
            int index,
            @NotNull CopycatGeometrySlope.Vertex vertex,
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
     * Emits an empty Slope using the default Copycat texture.
     */
    public static void emitEmpty(
            @NotNull CopycatGeometrySlope.Face[] faces,
            @NotNull TextureAtlasSprite sprite,
            @NotNull RenderContext context
    ) {
        QuadEmitter emitter = context.getEmitter();

        RenderMaterial material = FabricCopycatRenderer.getCutoutMaterial();

        for (CopycatGeometrySlope.Face face : faces) {

            CopycatGeometrySlope.Vertex[] vertices =
                    face.vertices();

            if (vertices.length == 4) {

                emitQuad(
                        emitter,
                        vertices,
                        face.direction(),
                        sprite,
                        material
                );

            } else if (vertices.length == 3) {

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
            @NotNull CopycatGeometrySlope.Vertex[] vertices,
            @NotNull Direction direction,
            @NotNull TextureAtlasSprite sprite,
            @NotNull RenderMaterial material
    ) {
        setVertex(emitter, 0, vertices[0]);
        setVertex(emitter, 1, vertices[1]);
        setVertex(emitter, 2, vertices[2]);
        setVertex(emitter, 3, vertices[3]);

        setFullFaceUv(emitter);

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
            @NotNull CopycatGeometrySlope.Vertex[] vertices,
            @NotNull Direction direction,
            @NotNull TextureAtlasSprite sprite,
            @NotNull RenderMaterial material
    ) {
        setVertex(emitter, 0, vertices[0]);
        setVertex(emitter, 1, vertices[1]);
        setVertex(emitter, 2, vertices[2]);
        setVertex(emitter, 3, vertices[2]);

        emitter.uv(0, 0.0F, 0.0F);
        emitter.uv(1, 1.0F, 0.0F);
        emitter.uv(2, 1.0F, 1.0F);
        emitter.uv(3, 1.0F, 1.0F);

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
        emitter.uv(0, 0.0F, 0.0F);
        emitter.uv(1, 1.0F, 0.0F);
        emitter.uv(2, 1.0F, 1.0F);
        emitter.uv(3, 0.0F, 1.0F);
    }

    private static void setVertex(
            @NotNull QuadEmitter emitter,
            int index,
            @NotNull CopycatGeometrySlope.Vertex vertex
    ) {
        emitter.pos(
                index,
                vertex.x(),
                vertex.y(),
                vertex.z()
        );
    }
}