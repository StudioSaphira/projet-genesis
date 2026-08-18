package net.scp_genesis.copycatblocks.renderer.util;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
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

public final class CopycatRenderHelper {

    private CopycatRenderHelper() {
    }

    /**
     * Returns the RenderTypes required to render a Copycat model.
     *
     * <p>Simple Copycat Blocks use their MAIN copied state.
     * Multipart Copycat Blocks such as Slabs can combine the
     * RenderTypes of multiple copied states.</p>
     */
    public static @NotNull ChunkRenderTypeSet getRenderTypes(
            @Nullable BakedModel baseModel,
            @Nullable BakedModel doubleModel,
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
                    CopycatBlockStateHelper.getCopiedState(
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

            if (doubleModel == null) {
                return ChunkRenderTypeSet.of(
                        RenderType.cutout()
                );
            }

            return doubleModel.getRenderTypes(
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
         * ------------------------------------------------------------
         * DOUBLE
         * ------------------------------------------------------------
         *
         * A DOUBLE slab can contain two different copied blocks.
         *
         * Example:
         *
         *     BOTTOM = Andesite
         *     TOP    = Grass Block
         *
         * The RenderTypes of both models must therefore be combined.
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
             * The Copycat geometry itself always uses cutout.
             */
            if (!hasBottom || !hasTop) {
                return ChunkRenderTypeSet.of(
                        RenderType.cutout()
                );
            }

            /*
             * BOTTOM + TOP
             *
             * The two copied models can use different RenderTypes.
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
        if (doubleModel != null) {
            return doubleModel.getRenderTypes(
                    state,
                    random,
                    modelData
            );
        }

        return ChunkRenderTypeSet.of(
                RenderType.cutout()
        );
    }
}