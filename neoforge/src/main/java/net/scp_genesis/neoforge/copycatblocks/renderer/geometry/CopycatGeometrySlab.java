package net.scp_genesis.neoforge.copycatblocks.renderer.geometry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.neoforge.copycatblocks.provider.CopycatModelProvider;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.CopycatBlockStateHelper;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.CopycatQuadHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class CopycatGeometrySlab
        implements CopycatGeometry {

    private final BakedModel bottomModel;
    private final BakedModel topModel;
    private final BakedModel doubleSecondaryModel;
    private final BakedModel doubleModel;

    public CopycatGeometrySlab(
            @NotNull BakedModel bottomModel,
            @NotNull BakedModel topModel,
            @NotNull BakedModel doubleSecondaryModel,
            @NotNull BakedModel doubleModel
    ) {
        this.bottomModel = bottomModel;
        this.topModel = topModel;
        this.doubleSecondaryModel = doubleSecondaryModel;
        this.doubleModel = doubleModel;
    }

    @Override
    public @NotNull BakedModel getModel() {
        return doubleModel;
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
         * NO STATE
         * ============================================================
         */

        if (state == null) {
            return bottomModel.getQuads(
                    null,
                    side,
                    random,
                    modelData,
                    renderType
            );
        }

        SlabType slabType =
                state.getValue(
                        BlockStateProperties.SLAB_TYPE
                );

        /*
         * ============================================================
         * BOTTOM
         * ============================================================
         */

        if (slabType == SlabType.BOTTOM) {

            BlockState copiedState =
                    CopycatBlockStateHelper.getCopiedState(
                            modelData,
                            CopycatPart.BOTTOM
                    );

            if (copiedState == null || copiedState.isAir()) {
                return bottomModel.getQuads(
                        state,
                        side,
                        random,
                        modelData,
                        renderType
                );
            }

            return CopycatQuadHelper.retextureModel(
                    bottomModel,
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
         * ============================================================
         * TOP
         * ============================================================
         */

        if (slabType == SlabType.TOP) {

            BlockState copiedState =
                    CopycatBlockStateHelper.getCopiedState(
                            modelData,
                            CopycatPart.TOP
                    );

            if (copiedState == null || copiedState.isAir()) {
                return topModel.getQuads(
                        state,
                        side,
                        random,
                        modelData,
                        renderType
                );
            }

            return CopycatQuadHelper.retextureModel(
                    topModel,
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
         * ============================================================
         * DOUBLE
         * ============================================================
         *
         * A DOUBLE slab is composed of two logical Copycat parts:
         *
         *     BOTTOM -> bottomModel
         *     TOP    -> doubleSecondaryModel
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

            List<BakedQuad> result =
                    new ArrayList<>();

            /*
             * --------------------------------------------------------
             * BOTTOM HALF
             * --------------------------------------------------------
             */

            if (hasBottom) {

                result.addAll(
                        CopycatQuadHelper.retextureModel(
                                bottomModel,
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

                result.addAll(
                        bottomModel.getQuads(
                                state,
                                side,
                                random,
                                modelData,
                                renderType
                        )
                );
            }

            /*
             * --------------------------------------------------------
             * TOP HALF
             * --------------------------------------------------------
             */

            if (hasTop) {

                result.addAll(
                        CopycatQuadHelper.retextureModel(
                                doubleSecondaryModel,
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

                result.addAll(
                        doubleSecondaryModel.getQuads(
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

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(
            @Nullable BlockState state,
            @NotNull RandomSource random,
            @NotNull ModelData modelData
    ) {
        if (state == null) {
            return ChunkRenderTypeSet.of(
                    RenderType.cutout()
            );
        }

        SlabType slabType =
                state.getValue(
                        BlockStateProperties.SLAB_TYPE
                );

        /*
         * ============================================================
         * BOTTOM
         * ============================================================
         */

        if (slabType == SlabType.BOTTOM) {

            BlockState copiedState =
                    CopycatBlockStateHelper.getCopiedState(
                            modelData,
                            CopycatPart.BOTTOM
                    );

            if (copiedState == null || copiedState.isAir()) {
                return ChunkRenderTypeSet.of(
                        RenderType.cutout()
                );
            }

            BakedModel copiedModel =
                    CopycatModelProvider.getModel(
                            copiedState
                    );

            return copiedModel.getRenderTypes(
                    copiedState,
                    random,
                    modelData
            );
        }

        /*
         * ============================================================
         * TOP
         * ============================================================
         */

        if (slabType == SlabType.TOP) {

            BlockState copiedState =
                    CopycatBlockStateHelper.getCopiedState(
                            modelData,
                            CopycatPart.TOP
                    );

            if (copiedState == null || copiedState.isAir()) {
                return ChunkRenderTypeSet.of(
                        RenderType.cutout()
                );
            }

            BakedModel copiedModel =
                    CopycatModelProvider.getModel(
                            copiedState
                    );

            return copiedModel.getRenderTypes(
                    copiedState,
                    random,
                    modelData
            );
        }

        /*
         * ============================================================
         * DOUBLE
         * ============================================================
         *
         * A DOUBLE slab can contain two different copied blocks.
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
             * No copied block, or only one copied block.
             *
             * The Copycat geometry itself uses cutout.
             */
            if (!hasBottom || !hasTop) {
                return ChunkRenderTypeSet.of(
                        RenderType.cutout()
                );
            }

            /*
             * BOTTOM + TOP
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

        return ChunkRenderTypeSet.of(
                RenderType.cutout()
        );
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(
            @NotNull ModelData modelData
    ) {
        /*
         * A Slab has no single MAIN part.
         *
         * The particle texture therefore depends on the
         * copied state associated with the slab part.
         */

        BlockState bottomState =
                CopycatBlockStateHelper.getCopiedState(
                        modelData,
                        CopycatPart.BOTTOM
                );

        if (bottomState != null && !bottomState.isAir()) {
            return CopycatModelProvider
                    .getModel(bottomState)
                    .getParticleIcon(modelData);
        }

        BlockState topState =
                CopycatBlockStateHelper.getCopiedState(
                        modelData,
                        CopycatPart.TOP
                );

        if (topState != null && !topState.isAir()) {
            return CopycatModelProvider
                    .getModel(topState)
                    .getParticleIcon(modelData);
        }

        return bottomModel.getParticleIcon(modelData);
    }
}