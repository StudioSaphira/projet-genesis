package net.scp_genesis.fabric.copycatblocks.renderer.util;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.CopycatSlopeGeometry;
import net.scp_genesis.fabric.copycatblocks.provider.FabricCopycatModelProvider;
import net.scp_genesis.fabric.copycatblocks.renderer.FabricCopycatRenderer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public final class FabricCopycatQuadHelper {

    private FabricCopycatQuadHelper() {
    }

    /**
     * Encodes the tint index of a Copycat part.
     */
    public static int encodeCopycatTintIndex(
            int tintIndex,
            @NotNull CopycatPart part
    ) {
        if (tintIndex < 0) {
            return tintIndex;
        }

        return switch (part) {
            case BOTTOM -> tintIndex + 1000;
            case TOP -> tintIndex + 2000;
            default -> tintIndex;
        };
    }

    /**
     * Emits a vanilla BakedQuad through the Fabric Renderer API.
     */
    public static void emitQuad(
            @NotNull QuadEmitter emitter,
            @NotNull BakedQuad quad,
            @NotNull RenderMaterial material
    ) {
        emitter.fromVanilla(
                quad,
                material,
                null
        );

        emitter.emit();
    }

    /**
     * Emits a geometry quad retextured using the sprite of a copied quad.
     *
     * <p>This is the Fabric equivalent of NeoForge's
     * {@code remapQuad()}.</p>
     *
     * <p>The geometry itself always comes from {@code geometryQuad}.
     * Only its UV coordinates are remapped from the geometry sprite
     * to the copied sprite.</p>
     */
    public static void emitRetexturedQuad(
            @NotNull QuadEmitter emitter,
            @NotNull BakedQuad geometryQuad,
            @NotNull BakedQuad copiedQuad,
            @NotNull CopycatPart part
    ) {
        emitter.fromVanilla(
                geometryQuad,
                FabricCopycatRenderer.getCutoutMaterial(),
                geometryQuad.getDirection()
        );

        emitter.spriteBake(
                copiedQuad.getSprite(),
                MutableQuadView.BAKE_LOCK_UV
        );

        emitter.colorIndex(
                encodeCopycatTintIndex(
                        copiedQuad.getTintIndex(),
                        part
                )
        );

        emitter.emit();
    }

    private static List<BakedQuad> getAllGeometryQuads(
            @NotNull BakedModel model,
            @NotNull BlockState state,
            @NotNull RandomSource random
    ) {

        List<BakedQuad> result = new java.util.ArrayList<>(model.getQuads(
                state,
                null,
                random
        ));

        for (Direction direction : Direction.values()) {
            result.addAll(
                    model.getQuads(
                            state,
                            direction,
                            random
                    )
            );
        }

        return result;
    }

    public static void emitBaseModel(
            @NotNull BakedModel model,
            @NotNull BlockState state,
            @NotNull Supplier<RandomSource> randomSupplier,
            @NotNull RenderContext context
    ) {
        RandomSource random = randomSupplier.get();

        QuadEmitter emitter = context.getEmitter();

        RenderMaterial material =
                FabricCopycatRenderer.getCutoutMaterial();

        List<BakedQuad> quads =
                getAllGeometryQuads(
                        model,
                        state,
                        random
                );

        for (BakedQuad quad : quads) {
            emitQuad(
                    emitter,
                    quad,
                    material
            );
        }
    }

    /**
     * Emits a retextured Copycat model through Fabric Renderer.
     */
    public static void retextureModel(
            @NotNull BakedModel geometryModel,
            @SuppressWarnings("unused") @NotNull BlockAndTintGetter blockView,
            @NotNull BlockState copycatState,
            @SuppressWarnings("unused") @NotNull BlockPos pos,
            @NotNull BlockState copiedState,
            @NotNull Supplier<RandomSource> randomSupplier,
            @NotNull RenderContext context,
            @NotNull CopycatPart part
    ) {
        BakedModel copiedModel =
                FabricCopycatModelProvider.getModel(copiedState);

        QuadEmitter emitter =
                context.getEmitter();

        RandomSource random =
                randomSupplier.get();

        List<BakedQuad> geometryQuads =
                getAllGeometryQuads(
                        geometryModel,
                        copycatState,
                        random
                );

        if (geometryQuads.isEmpty()) {
            return;
        }

        for (BakedQuad geometryQuad : geometryQuads) {

            Direction direction =
                    geometryQuad.getDirection();

            List<BakedQuad> copiedQuads =
                    FabricCopycatModelHelper.findMatchingCopiedQuads(
                            copiedModel,
                            copiedState,
                            direction,
                            geometryQuad,
                            random
                    );

            if (copiedQuads.isEmpty()) {
                emitQuad(
                        emitter,
                        geometryQuad,
                        FabricCopycatRenderer.getCutoutMaterial()
                );

                continue;
            }

            for (BakedQuad copiedQuad : copiedQuads) {
                emitRetexturedQuad(
                        emitter,
                        geometryQuad,
                        copiedQuad,
                        part
                );
            }
        }
    }

    /**
     * Emits a Copycat Slope face using the texture of the copied block.
     *
     * <p>The geometry comes from {@link CopycatSlopeGeometry}, while
     * the UV coordinates and sprite come from a quad of the copied
     * block model.</p>
     */
    public static void emitRetexturedSlopeFace(
            @NotNull QuadEmitter emitter,
            @NotNull CopycatSlopeGeometry.Face face,
            @NotNull BakedQuad copiedQuad,
            @NotNull CopycatPart part
    ) {
        CopycatSlopeGeometry.Vertex[] vertices =
                face.vertices();

        emitter.fromVanilla(
                copiedQuad,
                FabricCopycatRenderer.getCutoutMaterial(),
                face.direction()
        );

        /*
         * Replace the copied model geometry with the Slope geometry.
         *
         * fromVanilla() has already copied:
         * - sprite
         * - UV
         * - tint index
         * - other quad data
         *
         * We only replace the vertex positions.
         */

        if (vertices.length == 4) {

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
                    vertices[3]
            );

        } else if (vertices.length == 3) {

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

            /*
             * Fabric Renderer requires four vertices.
             *
             * Duplicate the third vertex to represent
             * the original triangle.
             */
            setVertex(
                    emitter,
                    3,
                    vertices[2]
            );

        } else {
            throw new IllegalStateException(
                    "Invalid Copycat Slope face vertex count: "
                            + vertices.length
            );
        }

        emitter.nominalFace(
                face.direction()
        );

        emitter.colorIndex(
                encodeCopycatTintIndex(
                        copiedQuad.getTintIndex(),
                        part
                )
        );

        emitter.emit();
    }

    private static void setVertex(
            @NotNull QuadEmitter emitter,
            int index,
            @NotNull CopycatSlopeGeometry.Vertex vertex
    ) {
        emitter.pos(
                index,
                vertex.x(),
                vertex.y(),
                vertex.z()
        );
    }

    /**
     * Finds a representative quad from the copied block model
     * for a Slope face.
     */
    public static @Nullable BakedQuad findSlopeCopiedQuad(
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
         * Some models may not expose a quad on the requested side.
         *
         * Fall back to the model's general quads.
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
     * Emits the complete Copycat Slope geometry using the texture
     * of the copied block.
     */
    public static void retextureSlope(
            @NotNull CopycatSlopeGeometry.Face[] faces,
            @NotNull BlockState copiedState,
            @NotNull Supplier<RandomSource> randomSupplier,
            @NotNull RenderContext context,
            @NotNull CopycatPart part
    ) {
        BakedModel copiedModel =
                FabricCopycatModelProvider.getModel(
                        copiedState
                );

        QuadEmitter emitter =
                context.getEmitter();

        RandomSource random =
                randomSupplier.get();

        for (CopycatSlopeGeometry.Face face : faces) {

            BakedQuad copiedQuad =
                    findSlopeCopiedQuad(
                            copiedModel,
                            copiedState,
                            face.direction(),
                            random
                    );

            if (copiedQuad == null) {
                continue;
            }

            emitRetexturedSlopeFace(
                    emitter,
                    face,
                    copiedQuad,
                    part
            );
        }
    }

    /**
     * Emits an empty Copycat Slope using the Copycat default texture.
     */
    public static void emitEmptySlope(
            @NotNull CopycatSlopeGeometry.Face[] faces,
            @NotNull TextureAtlasSprite sprite,
            @NotNull RenderContext context
    ) {
        QuadEmitter emitter =
                context.getEmitter();

        RenderMaterial material =
                FabricCopycatRenderer.getCutoutMaterial();

        for (CopycatSlopeGeometry.Face face : faces) {

            CopycatSlopeGeometry.Vertex[] vertices =
                    face.vertices();

            if (vertices.length == 4) {

                emitSlopeQuad(
                        emitter,
                        vertices,
                        face.direction(),
                        sprite,
                        material
                );

            } else if (vertices.length == 3) {

                emitSlopeTriangle(
                        emitter,
                        vertices,
                        face.direction(),
                        sprite,
                        material
                );

            }
        }
    }

    private static void emitSlopeQuad(
            @NotNull QuadEmitter emitter,
            @NotNull CopycatSlopeGeometry.Vertex[] vertices,
            @NotNull Direction direction,
            @NotNull TextureAtlasSprite sprite,
            @NotNull RenderMaterial material
    ) {
        setVertex(emitter, 0, vertices[0]);
        setVertex(emitter, 1, vertices[1]);
        setVertex(emitter, 2, vertices[2]);
        setVertex(emitter, 3, vertices[3]);

        emitter.uv(0, 0.0F, 0.0F);
        emitter.uv(1, 1.0F, 0.0F);
        emitter.uv(2, 1.0F, 1.0F);
        emitter.uv(3, 0.0F, 1.0F);

        emitter.spriteBake(
                sprite,
                MutableQuadView.BAKE_LOCK_UV
        );

        emitter.material(material);
        emitter.nominalFace(direction);
        emitter.emit();
    }

    private static void emitSlopeTriangle(
            @NotNull QuadEmitter emitter,
            @NotNull CopycatSlopeGeometry.Vertex[] vertices,
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
}