package net.scp_genesis.copycatblocks.renderer.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.copycatblocks.data.CopycatPart;
import net.scp_genesis.copycatblocks.provider.CopycatModelProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public final class CopycatBakedModel implements BakedModel {

    /*
     * ================================================================
     * MODELS
     * ================================================================
     */

    /**
     * Base geometry used by a simple Copycat Block such as the Cube.
     */
    private final BakedModel baseModel;
    private final BakedModel bottomModel;
    private final BakedModel topModel;
    private final BakedModel doubleTopModel;
    private final BakedModel doubleModel;


    /*
     * ================================================================
     * CONSTRUCTORS
     * ================================================================
     */

    /**
     * Constructor used by simple Copycat Blocks.
     */
    public CopycatBakedModel(
            @NotNull BakedModel baseModel
    ) {
        this.baseModel = baseModel;

        this.bottomModel = null;
        this.topModel = null;
        this.doubleTopModel = null;
        this.doubleModel = null;
    }

    /**
     * Constructor used by Copycat Slabs.
     */
    public CopycatBakedModel(
            @NotNull BakedModel bottomModel,
            @NotNull BakedModel topModel,
            @NotNull BakedModel doubleTopModel,
            @NotNull BakedModel doubleModel
    ) {
        this.baseModel = null;

        this.bottomModel = bottomModel;
        this.topModel = topModel;
        this.doubleTopModel = doubleTopModel;
        this.doubleModel = doubleModel;
    }


    /*
     * ================================================================
     * MODEL ACCESS
     * ================================================================
     */

    private BakedModel getBaseModel() {
        return java.util.Objects.requireNonNull(baseModel);
    }
    private BakedModel getBottomModel() {
        return java.util.Objects.requireNonNull(bottomModel);
    }
    private BakedModel getTopModel() {
        return java.util.Objects.requireNonNull(topModel);
    }
    private BakedModel getDoubleTopModel() {
        return java.util.Objects.requireNonNull(doubleTopModel);
    }
    private BakedModel getDoubleModel() {
        return java.util.Objects.requireNonNull(doubleModel);
    }


    /*
     * ================================================================
     * COPYCAT DATA
     * ================================================================
     */

    /**
     * Returns the copied BlockState associated with a Copycat part.
     */
    private static @Nullable BlockState getCopiedState(
            @NotNull ModelData modelData,
            @NotNull CopycatPart part
    ) {
        EnumMap<CopycatPart, BlockState> copiedStates =
                modelData.get(
                        CopycatModelProperties.COPIED_STATES
                );

        if (copiedStates == null) {
            return null;
        }

        return copiedStates.get(part);
    }

    /*
     * ================================================================
     * TEXTURE SELECTION
     * ================================================================
     */

    /**
     * Finds the most appropriate quad from the copied model for the
     * specified Copycat quad.
     *
     * <p>We first try to find a quad with the same tint index.
     * This is important for blocks such as Grass Block, which use
     * tinted and non-tinted quads.</p>
     */
    private static List<BakedQuad> findMatchingCopiedQuads(
            @NotNull BakedModel copiedModel,
            @NotNull BlockState copiedState,
            @Nullable Direction geometryDirection,
            @NotNull BakedQuad geometryQuad,
            @NotNull RandomSource random,
            @Nullable RenderType renderType
    ) {
        Direction direction = geometryDirection;

        if (direction == null) {
            direction = geometryQuad.getDirection();
        }

        List<BakedQuad> copiedQuads =
                copiedModel.getQuads(
                        copiedState,
                        direction,
                        random,
                        ModelData.EMPTY,
                        renderType
                );

        /*
         * Some models contain general quads instead of
         * directional quads.
         */
        if (copiedQuads.isEmpty()) {
            copiedQuads =
                    copiedModel.getQuads(
                            copiedState,
                            null,
                            random,
                            ModelData.EMPTY,
                            renderType
                    );
        }

        if (copiedQuads.isEmpty()) {
            return List.of();
        }

        /*
         * Keep all quads.
         *
         * This is important for models such as Grass Block:
         *
         *     grass_block_side
         *     grass_block_side_overlay
         *
         * Both must be rendered by the Copycat.
         */
        return copiedQuads;
    }

    /*
     * ================================================================
     * TINT MAPPING
     * ================================================================
     */

    private static int encodeCopycatTintIndex(
            int tintIndex,
            @NotNull CopycatPart part
    ) {
        /*
         * A Copycat quad with no tint must remain untinted.
         */
        if (tintIndex < 0) {
            return tintIndex;
        }

        return switch (part) {
            case BOTTOM -> tintIndex + 1000;
            case TOP -> tintIndex + 2000;
            default -> tintIndex;
        };
    }

    /*
     * ================================================================
     * UV REMAPPING
     * ================================================================
     */

    /**
     * Remaps the UV coordinates of a Copycat quad from its original
     * sprite to the sprite of the copied block.
     *
     * <p>The geometry remains unchanged. Only its texture mapping
     * is replaced.</p>
     */
    private static BakedQuad remapQuad(
            @NotNull BakedQuad sourceQuad,
            @NotNull TextureAtlasSprite sourceSprite,
            @NotNull BakedQuad copiedQuad,
            @Nullable CopycatPart part
    ) {
        TextureAtlasSprite targetSprite =
                copiedQuad.getSprite();

        int[] vertices =
                sourceQuad.getVertices().clone();

        final int vertexSize = 8;

        /*
         * ------------------------------------------------------------
         * SPRITE UV RANGES
         * ------------------------------------------------------------
         *
         * IMPORTANT:
         *
         * We use the complete UV range of the source sprite,
         * NOT the UV range of the geometry quad.
         *
         * This preserves partial UV mappings such as:
         *
         * slab_top side -> V = 0..8
         *
         * instead of stretching them to:
         *
         * V = 0..16
         */
        float sourceSpriteMinU =
                sourceSprite.getU0();

        float sourceSpriteMaxU =
                sourceSprite.getU1();

        float sourceSpriteMinV =
                sourceSprite.getV0();

        float sourceSpriteMaxV =
                sourceSprite.getV1();

        float targetSpriteMinU =
                targetSprite.getU0();

        float targetSpriteMaxU =
                targetSprite.getU1();

        float targetSpriteMinV =
                targetSprite.getV0();

        float targetSpriteMaxV =
                targetSprite.getV1();

        float sourceSpriteUSize =
                sourceSpriteMaxU - sourceSpriteMinU;

        float sourceSpriteVSize =
                sourceSpriteMaxV - sourceSpriteMinV;

        float targetSpriteUSize =
                targetSpriteMaxU - targetSpriteMinU;

        float targetSpriteVSize =
                targetSpriteMaxV - targetSpriteMinV;

        /*
         * Invalid sprite dimensions.
         */
        if (sourceSpriteUSize == 0.0F
                || sourceSpriteVSize == 0.0F
                || targetSpriteUSize == 0.0F
                || targetSpriteVSize == 0.0F) {

            return sourceQuad;
        }

        /*
         * ------------------------------------------------------------
         * UV REMAPPING
         * ------------------------------------------------------------
         *
         * We preserve the UV position relative to the COMPLETE
         * source sprite.
         *
         * Example:
         *
         * slab_top side:
         *
         *     source V = 0..8
         *
         * becomes:
         *
         *     target V = 0..8
         *
         * instead of 0..16.
         */
        for (int vertex = 0; vertex < 4; vertex++) {

            int offset =
                    vertex * vertexSize;

            float u =
                    Float.intBitsToFloat(
                            vertices[offset + 4]
                    );

            float v =
                    Float.intBitsToFloat(
                            vertices[offset + 5]
                    );

            float normalizedU =
                    (u - sourceSpriteMinU)
                            / sourceSpriteUSize;

            float normalizedV =
                    (v - sourceSpriteMinV)
                            / sourceSpriteVSize;

            float newU =
                    targetSpriteMinU
                            + normalizedU
                            * targetSpriteUSize;

            float newV =
                    targetSpriteMinV
                            + normalizedV
                            * targetSpriteVSize;

            vertices[offset + 4] =
                    Float.floatToRawIntBits(newU);

            vertices[offset + 5] =
                    Float.floatToRawIntBits(newV);
        }

        /*
         * ------------------------------------------------------------
         * TINT
         * ------------------------------------------------------------
         *
         * The copied quad determines the tint behaviour.
         */
        int tintIndex =
                copiedQuad.getTintIndex();

        if (tintIndex >= 0) {

            if (part == CopycatPart.BOTTOM) {

                tintIndex =
                        encodeCopycatTintIndex(
                                tintIndex,
                                CopycatPart.BOTTOM
                        );

            } else if (part == CopycatPart.TOP) {

                tintIndex =
                        encodeCopycatTintIndex(
                                tintIndex,
                                CopycatPart.TOP
                        );
            }
        }

        /*
         * ------------------------------------------------------------
         * RESULT
         * ------------------------------------------------------------
         */

        return new BakedQuad(
                vertices,
                tintIndex,
                sourceQuad.getDirection(),
                targetSprite,
                sourceQuad.isShade(),
                sourceQuad.hasAmbientOcclusion()
        );
    }


    /**
     * Retextures a geometry model using the corresponding copied model.
     */
    private static List<BakedQuad> retextureModel(
            @NotNull BakedModel geometryModel,
            @NotNull BlockState copycatState,
            @NotNull BlockState copiedState,
            @Nullable Direction side,
            @NotNull RandomSource random,
            @Nullable RenderType renderType,
            @NotNull ModelData modelData,
            @NotNull CopycatPart part
    ) {
        BakedModel copiedModel =
                CopycatModelProvider.getModel(copiedState);

        /*
         * The geometry must always be generated from the
         * Copycat BlockState.
         *
         * The copied BlockState is used only to determine
         * textures and tint information.
         */
        List<BakedQuad> geometryQuads =
                geometryModel.getQuads(
                        copycatState,
                        side,
                        random,
                        modelData,
                        renderType
                );

        if (geometryQuads.isEmpty()) {
            return List.of();
        }

        List<BakedQuad> result =
                new ArrayList<>();

        for (BakedQuad geometryQuad : geometryQuads) {

            Direction direction =
                    side != null
                            ? side
                            : geometryQuad.getDirection();

            /*
             * Retrieve EVERY copied quad corresponding
             * to this face.
             *
             * This is what allows models such as Grass Block
             * to provide both:
             *
             *   grass_block_side
             *   grass_block_side_overlay
             */
            List<BakedQuad> copiedQuads =
                    findMatchingCopiedQuads(
                            copiedModel,
                            copiedState,
                            direction,
                            geometryQuad,
                            random,
                            renderType
                    );

            if (copiedQuads.isEmpty()) {
                result.add(geometryQuad);
                continue;
            }

            for (BakedQuad copiedQuad : copiedQuads) {

                result.add(
                        remapQuad(
                                geometryQuad,
                                geometryQuad.getSprite(),
                                copiedQuad,
                                part
                        )
                );
            }
        }

        return result;
    }


    /*
     * ================================================================
     * QUADS
     * ================================================================
     */

    /**
     * @deprecated Use the ModelData-aware overload.
     */
    @Deprecated
    @Override
    public @NotNull List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            @NotNull RandomSource random
    ) {
        return getQuads(
                state,
                side,
                random,
                ModelData.EMPTY,
                null
        );
    }


    @Override
    public @NotNull List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            @NotNull RandomSource random,
            @NotNull ModelData modelData,
            @Nullable RenderType renderType
    ) {
        /*
         * ============================================================
         * COPYCAT CUBE
         * ============================================================
         */
        if (baseModel != null) {

            BlockState copiedState =
                    getCopiedState(
                            modelData,
                            CopycatPart.MAIN
                    );

            if (copiedState == null || copiedState.isAir()) {
                return getBaseModel().getQuads(
                        state,
                        side,
                        random,
                        modelData,
                        renderType
                );
            }

            return retextureModel(
                    getBaseModel(),
                    state,
                    copiedState,
                    side,
                    random,
                    renderType,
                    modelData,
                    CopycatPart.MAIN
            );
        }

        /*
         * ============================================================
         * COPYCAT SLAB
         * ============================================================
         */
        if (state == null) {
            return List.of();
        }

        SlabType slabType =
                state.getValue(BlockStateProperties.SLAB_TYPE);

        /*
         * ------------------------------------------------------------
         * BOTTOM
         * ------------------------------------------------------------
         */
        if (slabType == SlabType.BOTTOM) {

            BlockState copiedState =
                    getCopiedState(
                            modelData,
                            CopycatPart.BOTTOM
                    );

            if (copiedState == null || copiedState.isAir()) {
                return getBottomModel().getQuads(
                        state,
                        side,
                        random,
                        modelData,
                        renderType
                );
            }

            return retextureModel(
                    getBottomModel(),
                    state,
                    copiedState,
                    side,
                    random,
                    renderType,
                    modelData,
                    CopycatPart.BOTTOM
            );
        }

        /*
         * ------------------------------------------------------------
         * TOP
         * ------------------------------------------------------------
         */
        if (slabType == SlabType.TOP) {

            BlockState copiedState =
                    getCopiedState(
                            modelData,
                            CopycatPart.TOP
                    );

            if (copiedState == null || copiedState.isAir()) {
                return getTopModel().getQuads(
                        state,
                        side,
                        random,
                        modelData,
                        renderType
                );
            }

            return retextureModel(
                    getTopModel(),
                    state,
                    copiedState,
                    side,
                    random,
                    renderType,
                    modelData,
                    CopycatPart.TOP
            );
        }

        /*
         * ------------------------------------------------------------
         * DOUBLE
         * ------------------------------------------------------------
         *
         * A DOUBLE slab is still composed of two logical Copycat parts.
         *
         * Bottom half -> CopycatPart.BOTTOM
         * Top half    -> CopycatPart.TOP
         */
        if (slabType == SlabType.DOUBLE) {

            BlockState bottomState =
                    getCopiedState(
                            modelData,
                            CopycatPart.BOTTOM
                    );

            BlockState topState =
                    getCopiedState(
                            modelData,
                            CopycatPart.TOP
                    );

            boolean hasBottom =
                    bottomState != null
                            && !bottomState.isAir();

            boolean hasTop =
                    topState != null
                            && !topState.isAir();

            /*
             * No copied state at all.
             *
             * Use the normal full-block geometry.
             */
            if (!hasBottom && !hasTop) {
                return getDoubleModel().getQuads(
                        state,
                        side,
                        random,
                        modelData,
                        renderType
                );
            }

            List<BakedQuad> result =
                    new ArrayList<>();

            /*
             * ============================================================
             * BOTTOM HALF
             * ============================================================
             */

            if (hasBottom) {

                result.addAll(
                        retextureModel(
                                getBottomModel(),
                                state,
                                bottomState,
                                side,
                                random,
                                renderType,
                                modelData,
                                CopycatPart.BOTTOM
                        )
                );

            } else {

                /*
                 * No copied BOTTOM:
                 * render the normal Copycat bottom texture.
                 */
                result.addAll(
                        getBottomModel().getQuads(
                                state,
                                side,
                                random,
                                modelData,
                                renderType
                        )
                );
            }

            /*
             * ============================================================
             * TOP HALF
             * ============================================================
             */

            if (hasTop) {

                result.addAll(
                        retextureModel(
                                getDoubleTopModel(),
                                state,
                                topState,
                                side,
                                random,
                                renderType,
                                modelData,
                                CopycatPart.TOP
                        )
                );

            } else {

                /*
                 * No copied TOP:
                 * render the normal Copycat top texture.
                 */
                result.addAll(
                        getDoubleTopModel().getQuads(
                                state,
                                side,
                                random,
                                modelData,
                                renderType
                        )
                );
            }

            return result;
        }

        return List.of();
    }


    /*
     * ================================================================
     * RENDER TYPES
     * ================================================================
     */

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(
            @Nullable BlockState state,
            @NotNull RandomSource random,
            @NotNull ModelData modelData
    ) {

        /*
         * ============================================================
         * COPYCAT CUBE
         * ============================================================
         */

        if (baseModel != null) {

            BlockState copiedState =
                    getCopiedState(
                            modelData,
                            CopycatPart.MAIN
                    );

            if (copiedState == null || copiedState.isAir()) {
                return ChunkRenderTypeSet.of(
                        RenderType.cutout()
                );
            }

            return CopycatModelProvider
                    .getModel(copiedState)
                    .getRenderTypes(
                            copiedState,
                            random,
                            modelData
                    );
        }


        /*
         * ============================================================
         * COPYCAT SLAB
         * ============================================================
         */

        if (state == null) {
            return getDoubleModel().getRenderTypes(
                    null,
                    random,
                    modelData
            );
        }

        SlabType slabType =
                state.getValue(
                        BlockStateProperties.SLAB_TYPE
                );


        /*
         * ------------------------------------------------------------
         * BOTTOM
         * ------------------------------------------------------------
         */

        if (slabType == SlabType.BOTTOM) {

            BlockState copiedState =
                    getCopiedState(
                            modelData,
                            CopycatPart.BOTTOM
                    );

            if (copiedState == null || copiedState.isAir()) {
                return ChunkRenderTypeSet.of(
                        RenderType.cutout()
                );
            }

            BakedModel copiedModel =
                    CopycatModelProvider.getModel(copiedState);

            return copiedModel.getRenderTypes(
                    copiedState,
                    random,
                    modelData
            );
        }


        /*
         * ------------------------------------------------------------
         * TOP
         * ------------------------------------------------------------
         */

        if (slabType == SlabType.TOP) {

            BlockState copiedState =
                    getCopiedState(
                            modelData,
                            CopycatPart.TOP
                    );

            if (copiedState == null || copiedState.isAir()) {
                return ChunkRenderTypeSet.of(
                        RenderType.cutout()
                );
            }

            BakedModel copiedModel =
                    CopycatModelProvider.getModel(copiedState);

            return copiedModel.getRenderTypes(
                    copiedState,
                    random,
                    modelData
            );
        }


        /*
         * ------------------------------------------------------------
         * DOUBLE
         * ------------------------------------------------------------
         *
         * A DOUBLE can contain two different blocks.
         *
         * Example :
         *
         *     BOTTOM = Andesite
         *     TOP    = Grass Block
         *
         * It is therefore necessary to combine the RenderTypes of the two models.
         */

        if (slabType == SlabType.DOUBLE) {

            BlockState bottomState =
                    getCopiedState(
                            modelData,
                            CopycatPart.BOTTOM
                    );

            BlockState topState =
                    getCopiedState(
                            modelData,
                            CopycatPart.TOP
                    );

            boolean hasBottom =
                    bottomState != null
                            && !bottomState.isAir();

            boolean hasTop =
                    topState != null
                            && !topState.isAir();


            /*
             * No blocks copied.
             */
            if (!hasBottom && !hasTop) {
                return ChunkRenderTypeSet.of(
                        RenderType.cutout()
                );
            }


            /*
             * BOTTOM Only.
             */
            if (hasBottom && !hasTop) {
                return ChunkRenderTypeSet.of(
                        RenderType.cutout()
                );
            }


            /*
             * TOP Only.
             */
            if (!hasBottom) {
                return ChunkRenderTypeSet.of(
                        RenderType.cutout()
                );
            }


            /*
             * BOTTOM + TOP.
             *
             * The two models can have different RenderTypes.
             */
            BakedModel bottomCopiedModel =
                    CopycatModelProvider.getModel(
                            bottomState
                    );

            BakedModel topCopiedModel =
                    CopycatModelProvider.getModel(
                            topState
                    );

            ChunkRenderTypeSet bottomRenderTypes =
                    bottomCopiedModel.getRenderTypes(
                            bottomState,
                            random,
                            modelData
                    );

            ChunkRenderTypeSet topRenderTypes =
                    topCopiedModel.getRenderTypes(
                            topState,
                            random,
                            modelData
                    );

            return ChunkRenderTypeSet.union(
                    bottomRenderTypes,
                    topRenderTypes
            );
        }


        /*
         * Fallback.
         */
        return getDoubleModel().getRenderTypes(
                state,
                random,
                modelData
        );
    }


    /*
     * ================================================================
     * COMMON MODEL PROPERTIES
     * ================================================================
     */

    private BakedModel getReferenceModel() {

        if (baseModel != null) {
            return getBaseModel();
        }

        return getDoubleModel();
    }


    @Override
    public boolean useAmbientOcclusion() {
        return getReferenceModel()
                .useAmbientOcclusion();
    }


    @Override
    public boolean usesBlockLight() {
        return getReferenceModel()
                .usesBlockLight();
    }


    @Override
    public boolean isGui3d() {
        return getReferenceModel()
                .isGui3d();
    }


    @Override
    public boolean isCustomRenderer() {
        return false;
    }


    /*
     * ================================================================
     * PARTICLE
     * ================================================================
     */

    /**
     * @deprecated Use the ModelData-aware overload.
     */
    @Deprecated
    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return getReferenceModel()
                .getParticleIcon();
    }


    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(
            @NotNull ModelData modelData
    ) {

        BlockState copiedState =
                getCopiedState(
                        modelData,
                        CopycatPart.MAIN
                );

        if (copiedState == null
                || copiedState.isAir()) {

            return getReferenceModel()
                    .getParticleIcon(
                            modelData
                    );
        }

        return CopycatModelProvider
                .getModel(copiedState)
                .getParticleIcon(
                        modelData
                );
    }


    /*
     * ================================================================
     * TRANSFORMS
     * ================================================================
     */

    /**
     * @deprecated Use the modern transform API.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public @NotNull ItemTransforms getTransforms() {
        return getReferenceModel()
                .getTransforms();
    }


    @Override
    public @NotNull ItemOverrides getOverrides() {
        return getReferenceModel().getOverrides();
    }
}