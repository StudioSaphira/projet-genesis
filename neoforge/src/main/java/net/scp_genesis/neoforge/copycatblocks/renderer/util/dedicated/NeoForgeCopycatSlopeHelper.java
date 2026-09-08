package net.scp_genesis.neoforge.copycatblocks.renderer.util.dedicated;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.CopycatFace;
import net.scp_genesis.common.copycatblocks.geometry.CopycatGeometryMath;
import net.scp_genesis.common.copycatblocks.geometry.CopycatUV;
import net.scp_genesis.common.copycatblocks.geometry.CopycatVector;
import net.scp_genesis.common.copycatblocks.provider.CopycatModelProvider;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatBlockStateHelper;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatModelHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * NeoForge-specific helper for rendering Copycat Slopes.
 *
 * <p>This class bridges the platform-independent Common Slope geometry
 * and the NeoForge BakedQuad rendering system.</p>
 *
 * <p>The geometry itself is never generated here. It is supplied by
 * {@code CopycatGeometrySlope} through {@link CopycatFace} and
 * {@link CopycatUV}.</p>
 *
 * <p>The rendering pipeline is:</p>
 *
 * <pre>
 * CopycatFace
 *      +
 * CopycatUV[]
 *      +
 * copied BlockState
 *      ↓
 * copied BakedModel
 *      ↓
 * TextureAtlasSprite / tint / lighting
 *      ↓
 * BakedQuad
 * </pre>
 */
public final class NeoForgeCopycatSlopeHelper {

    private static final int VERTEX_SIZE = 8;

    private static final int POSITION_OFFSET = 0;
    private static final int COLOR_OFFSET = 3;
    private static final int UV_OFFSET = 4;
    private static final int LIGHT_OFFSET = 6;
    private static final int NORMAL_OFFSET = 7;

    private NeoForgeCopycatSlopeHelper() {}

    /*
     * ================================================================
     * MODEL DATA
     * ================================================================
     */

    /**
     * Returns the copied BlockState associated with the MAIN
     * Copycat part.
     */
    @Nullable
    public static BlockState getCopiedState(
            @NotNull ModelData modelData
    ) {
        return NeoForgeCopycatBlockStateHelper.getCopiedState(
                modelData,
                CopycatPart.MAIN
        );
    }

    /*
     * ================================================================
     * QUAD GENERATION
     * ================================================================
     */

    /**
     * Converts a Common Slope face into a NeoForge BakedQuad.
     *
     * <p>The Common geometry supplies the vertices and UV coordinates.
     * The copied block model supplies the texture and rendering
     * information.</p>
     *
     * @param face Common Slope face
     * @param uv normalized UV coordinates matching the face vertices
     * @param copiedState state of the copied block
     * @param part Copycat part being rendered
     * @param random Minecraft random source
     * @param renderType requested render type
     * @return generated BakedQuads
     */
    public static @NotNull List<BakedQuad> buildQuads(
            @NotNull CopycatFace face,
            @NotNull CopycatUV[] uv,
            @NotNull BlockState copiedState,
            @NotNull CopycatPart part,
            @NotNull RandomSource random,
            @Nullable RenderType renderType
    ) {
        int vertexCount = face.vertices().length;

        if (vertexCount < 3) {return Collections.emptyList();}

        if (uv.length != vertexCount) {
            throw new IllegalArgumentException(
                    "Copycat Slope face and UV vertex counts do not match"
            );
        }

        /*
         * ============================================================
         * COPIED MODEL
         * ============================================================
         */

        BakedModel copiedModel = CopycatModelProvider.getModel(copiedState);

        /*
         * ============================================================
         * COPIED QUADS
         * ============================================================
         *
         * Prefer the directional quads corresponding to the Slope
         * face.
         */
        List<BakedQuad> copiedQuads =
                NeoForgeCopycatModelHelper.getCopiedQuads(
                        copiedModel,
                        copiedState,
                        face.direction(),
                        random,
                        renderType
                );

        if (copiedQuads.isEmpty()) {return Collections.emptyList();}

        /*
         * ============================================================
         * BUILD ALL QUAD
         * ============================================================
         */

        List<BakedQuad> result =
                new java.util.ArrayList<>();

        for (BakedQuad copiedQuad : copiedQuads) {

            result.add(
                    buildQuad(
                            face,
                            uv,
                            copiedQuad,
                            copiedQuad.getSprite(),
                            part
                    )
            );
        }

        return result;
    }

    /**
     * Builds a Slope quad using the default Copycat texture.
     *
     * <p>This method is used when no copied BlockState is available,
     * for example during the initial rendering of the Copycat Slope.</p>
     *
     * <p>Unlike {@link #buildQuads}, this method does not require a
     * copied BakedModel. The Common geometry and the default texture
     * are sufficient to construct the quad.</p>
     */
    public static @NotNull List<BakedQuad> buildDefaultQuads(
            @NotNull CopycatFace face,
            @NotNull CopycatUV[] uv,
            @NotNull TextureAtlasSprite sprite
    ) {
        int vertexCount =
                face.vertices().length;

        if (vertexCount < 3) {
            return Collections.emptyList();
        }

        if (uv.length != vertexCount) {
            throw new IllegalArgumentException(
                    "Copycat Slope face and UV vertex counts do not match"
            );
        }

        int[] vertices =
                new int[
                        4 * VERTEX_SIZE
                        ];

        /*
         * ============================================================
         * NORMAL
         * ============================================================
         */

        CopycatVector normal =
                CopycatGeometryMath.normalize(
                        CopycatGeometryMath.normal(
                                face
                        )
                );

        int packedNormal =
                packNormal(normal);

        /*
         * ============================================================
         * VERTICES
         * ============================================================
         */

        for (int i = 0; i < 4; i++) {

            int sourceIndex =
                    Math.min(
                            i,
                            vertexCount - 1
                    );

            int offset =
                    i * VERTEX_SIZE;

            var vertex =
                    face.vertices()[sourceIndex];

            CopycatUV vertexUV =
                    uv[sourceIndex];

            /*
             * --------------------------------------------------------
             * POSITION
             * --------------------------------------------------------
             */

            vertices[offset + POSITION_OFFSET] =
                    Float.floatToRawIntBits(
                            vertex.x()
                    );

            vertices[offset + POSITION_OFFSET + 1] =
                    Float.floatToRawIntBits(
                            vertex.y()
                    );

            vertices[offset + POSITION_OFFSET + 2] =
                    Float.floatToRawIntBits(
                            vertex.z()
                    );

            /*
             * --------------------------------------------------------
             * COLOR
             * --------------------------------------------------------
             */

            vertices[offset + COLOR_OFFSET] =
                    -1;

            /*
             * --------------------------------------------------------
             * UV
             * --------------------------------------------------------
             */

            vertices[offset + UV_OFFSET] =
                    Float.floatToRawIntBits(
                            sprite.getU(
                                    vertexUV.u()
                            )
                    );

            vertices[offset + UV_OFFSET + 1] =
                    Float.floatToRawIntBits(
                            sprite.getV(
                                    vertexUV.v()
                            )
                    );

            /*
             * --------------------------------------------------------
             * LIGHT
             * --------------------------------------------------------
             */

            vertices[offset + LIGHT_OFFSET] =
                    0;

            /*
             * --------------------------------------------------------
             * NORMAL
             * --------------------------------------------------------
             */

            vertices[offset + NORMAL_OFFSET] =
                    packedNormal;
        }

        return List.of(
                new BakedQuad(
                        vertices,
                        -1,
                        face.direction(),
                        sprite,
                        true,
                        true
                )
        );
    }

    /*
     * ================================================================
     * QUAD CONSTRUCTION
     * ================================================================
     */

    /**
     * Builds a BakedQuad directly from Common Slope geometry.
     */
    private static @NotNull BakedQuad buildQuad(
            @NotNull CopycatFace face,
            @NotNull CopycatUV[] uv,
            @NotNull BakedQuad sourceQuad,
            @NotNull TextureAtlasSprite sprite,
            @NotNull CopycatPart part
    ) {
        int vertexCount = face.vertices().length;

        int[] vertices =
                new int[
                        4 * VERTEX_SIZE
                        ];

        /*
         * ============================================================
         * NORMAL
         * ============================================================
         */

        CopycatVector normal =
                CopycatGeometryMath.normalize(
                        CopycatGeometryMath.normal(
                                face
                        )
                );

        int packedNormal = packNormal(normal);

        /*
         * ============================================================
         * VERTICES
         * ============================================================
         *
         * BakedQuad always contains four vertices.
         *
         * A triangular Common face therefore duplicates its final
         * vertex.
         */

        for (int i = 0; i < 4; i++) {

            int sourceIndex =
                    Math.min(
                            i,
                            vertexCount - 1
                    );

            int offset = i * VERTEX_SIZE;

            var vertex = face.vertices()[sourceIndex];

            CopycatUV vertexUV = uv[sourceIndex];

            /*
             * --------------------------------------------------------
             * POSITION
             * --------------------------------------------------------
             */

            vertices[offset + POSITION_OFFSET] =
                    Float.floatToRawIntBits(
                            vertex.x()
                    );

            vertices[offset + POSITION_OFFSET + 1] =
                    Float.floatToRawIntBits(
                            vertex.y()
                    );

            vertices[offset + POSITION_OFFSET + 2] =
                    Float.floatToRawIntBits(
                            vertex.z()
                    );

            /*
             * --------------------------------------------------------
             * COLOR
             * --------------------------------------------------------
             */

            vertices[offset + COLOR_OFFSET] =
                    getVertexColor(
                            sourceQuad,
                            i
                    );

            /*
             * --------------------------------------------------------
             * UV
             * --------------------------------------------------------
             */

            vertices[offset + UV_OFFSET] = Float.floatToRawIntBits(sprite.getU(vertexUV.u()));

            vertices[offset + UV_OFFSET + 1] = Float.floatToRawIntBits(sprite.getV(vertexUV.v()));

            /*
             * --------------------------------------------------------
             * LIGHT
             * --------------------------------------------------------
             */

            vertices[offset + LIGHT_OFFSET] =
                    getVertexLight(
                            sourceQuad,
                            i
                    );

            /*
             * --------------------------------------------------------
             * NORMAL
             * --------------------------------------------------------
             */

            vertices[offset + NORMAL_OFFSET] = packedNormal;
        }

        /*
         * ============================================================
         * TINT
         * ============================================================
         */

        int tintIndex = sourceQuad.getTintIndex();

        if (tintIndex >= 0) {
            tintIndex =
                    encodeCopycatTintIndex(
                            tintIndex,
                            part
                    );
        }

        return new BakedQuad(
                vertices,
                tintIndex,
                face.direction(),
                sprite,
                sourceQuad.isShade(),
                sourceQuad.hasAmbientOcclusion()
        );
    }

    /*
     * ================================================================
     * VERTEX DATA
     * ================================================================
     */

    private static int getVertexColor(@NotNull BakedQuad quad, int vertex) {
        int[] data = quad.getVertices();

        return data[
                vertex * VERTEX_SIZE
                        + COLOR_OFFSET
                ];
    }

    private static int getVertexLight(@NotNull BakedQuad quad, int vertex) {
        int[] data = quad.getVertices();

        return data[
                vertex * VERTEX_SIZE
                        + LIGHT_OFFSET
                ];
    }

    /*
     * ================================================================
     * NORMALS
     * ================================================================
     */

    private static int packNormal(@NotNull CopycatVector normal) {
        int x =
                packNormalComponent(
                        normal.x()
                );

        int y =
                packNormalComponent(
                        normal.y()
                );

        int z =
                packNormalComponent(
                        normal.z()
                );

        return x
                | (y << 8)
                | (z << 16);
    }

    private static int packNormalComponent(float value) {
        int packed =
                Math.round(
                        Math.clamp(
                                value,
                                -1.0F,
                                1.0F
                        ) * 127.0F
                );

        return packed & 0xFF;
    }

    /*
     * ================================================================
     * TINT
     * ================================================================
     */

    private static int encodeCopycatTintIndex(int tintIndex, @NotNull CopycatPart part) {
        if (tintIndex < 0) {return tintIndex;}

        return switch (part) {
            case BOTTOM -> tintIndex + 1000;

            case TOP -> tintIndex + 2000;

            default -> tintIndex;
        };
    }

    /*
     * ================================================================
     * PARTICLE
     * ================================================================
     */

    @SuppressWarnings("deprecation")
    private static @NotNull TextureAtlasSprite getMissingParticleSprite() {
        return Minecraft
                .getInstance()
                .getBlockRenderer()
                .getBlockModelShaper()
                .getModelManager()
                .getMissingModel()
                .getParticleIcon();
    }

    /**
     * Returns the particle sprite of the copied block.
     */
    @SuppressWarnings({"deprecation", "unused"})
    public static @NotNull TextureAtlasSprite getParticleSprite(@NotNull ModelData modelData) {
        BlockState copiedState = getCopiedState(modelData);

        if (copiedState == null || copiedState.isAir()) {return getMissingParticleSprite();}

        return Minecraft
                .getInstance()
                .getBlockRenderer()
                .getBlockModelShaper()
                .getParticleIcon(copiedState);
    }
}