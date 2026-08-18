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
import net.scp_genesis.copycatblocks.renderer.util.CopycatBlockStateHelper;
import net.scp_genesis.copycatblocks.renderer.util.CopycatQuadHelper;
import net.scp_genesis.copycatblocks.renderer.util.CopycatRenderHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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
    private final BakedModel doubleSecondaryModel;
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
        this.doubleSecondaryModel = null;
        this.doubleModel = null;
    }

    /**
     * Constructor used by Copycat Slabs.
     */
    public CopycatBakedModel(
            @NotNull BakedModel bottomModel,
            @NotNull BakedModel topModel,
            @NotNull BakedModel doubleSecondaryModel,
            @NotNull BakedModel doubleModel
    ) {
        this.baseModel = null;

        this.bottomModel = bottomModel;
        this.topModel = topModel;
        this.doubleSecondaryModel = doubleSecondaryModel;
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
    private BakedModel getDoubleSecondaryModel() {
        return java.util.Objects.requireNonNull(doubleSecondaryModel);
    }
    private BakedModel getDoubleModel() {
        return java.util.Objects.requireNonNull(doubleModel);
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
                    CopycatBlockStateHelper.getCopiedState(
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

            return CopycatQuadHelper.retextureModel(
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
            return getBottomModel().getQuads(
                    null,
                    side,
                    random,
                    modelData,
                    renderType
            );
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
                    CopycatBlockStateHelper.getCopiedState(
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

            return CopycatQuadHelper.retextureModel(
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
                    CopycatBlockStateHelper.getCopiedState(
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

            return CopycatQuadHelper.retextureModel(
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
                    CopycatBlockStateHelper.getCopiedState(
                            modelData,
                            CopycatPart.BOTTOM
                    );

            BlockState topState =
                    CopycatBlockStateHelper.getCopiedState(
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

            List<BakedQuad> result =
                    new ArrayList<>();

            /*
             * ============================================================
             * BOTTOM HALF
             * ============================================================
             */

            if (hasBottom) {

                result.addAll(
                        CopycatQuadHelper.retextureModel(
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
                        CopycatQuadHelper.retextureModel(
                                getDoubleSecondaryModel(),
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
                        getDoubleSecondaryModel().getQuads(
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
        return CopycatRenderHelper.getRenderTypes(
                baseModel,
                doubleModel,
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
                CopycatBlockStateHelper.getCopiedState(
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