package net.scp_genesis.neoforge.copycatblocks.renderer.geometry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.slab.CopycatGeometrySlab;
import net.scp_genesis.common.copycatblocks.provider.CopycatModelProvider;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatBlockStateHelper;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatQuadHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class NeoForgeCopycatGeometrySlab
        implements NeoForgeCopycatGeometry {

    private final BakedModel bottomModel;
    private final BakedModel topModel;
    private final BakedModel doubleSecondaryModel;
    private final BakedModel doubleModel;

    public NeoForgeCopycatGeometrySlab(
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
                CopycatGeometrySlab.getSlabType(state);

        /*
         * ============================================================
         * SINGLE SLAB
         * ============================================================
         */

        if (!CopycatGeometrySlab.isDouble(slabType)) {

            CopycatPart part =
                    CopycatGeometrySlab.getPart(slabType);

            BakedModel model =
                    slabType == SlabType.BOTTOM
                            ? bottomModel
                            : topModel;

            BlockState copiedState =
                    NeoForgeCopycatBlockStateHelper.getCopiedState(
                            modelData,
                            part
                    );

            if (copiedState == null || copiedState.isAir()) {
                return model.getQuads(
                        state,
                        side,
                        random,
                        modelData,
                        renderType
                );
            }

            return NeoForgeCopycatQuadHelper.retextureModel(
                    model,
                    state,
                    copiedState,
                    side,
                    random,
                    renderType,
                    modelData,
                    part
            );
        }

        /*
         * ============================================================
         * DOUBLE
         * ============================================================
         */

        BlockState bottomState =
                NeoForgeCopycatBlockStateHelper.getCopiedState(
                        modelData,
                        CopycatPart.BOTTOM
                );

        BlockState topState =
                NeoForgeCopycatBlockStateHelper.getCopiedState(
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
         * BOTTOM
         */

        if (hasBottom) {
            result.addAll(
                    NeoForgeCopycatQuadHelper.retextureModel(
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
         * TOP
         */

        if (hasTop) {
            result.addAll(
                    NeoForgeCopycatQuadHelper.retextureModel(
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
                CopycatGeometrySlab.getSlabType(state);

        /*
         * ============================================================
         * SINGLE SLAB
         * ============================================================
         */

        if (!CopycatGeometrySlab.isDouble(slabType)) {

            CopycatPart part =
                    CopycatGeometrySlab.getPart(slabType);

            BlockState copiedState =
                    NeoForgeCopycatBlockStateHelper.getCopiedState(
                            modelData,
                            part
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
         */

        BlockState bottomState =
                NeoForgeCopycatBlockStateHelper.getCopiedState(
                        modelData,
                        CopycatPart.BOTTOM
                );

        BlockState topState =
                NeoForgeCopycatBlockStateHelper.getCopiedState(
                        modelData,
                        CopycatPart.TOP
                );

        boolean hasBottom =
                bottomState != null
                        && !bottomState.isAir();

        boolean hasTop =
                topState != null
                        && !topState.isAir();

        if (!hasBottom || !hasTop) {
            return ChunkRenderTypeSet.of(
                    RenderType.cutout()
            );
        }

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

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(
            @NotNull ModelData modelData
    ) {
        BlockState bottomState =
                NeoForgeCopycatBlockStateHelper.getCopiedState(
                        modelData,
                        CopycatPart.BOTTOM
                );

        if (bottomState != null && !bottomState.isAir()) {
            return CopycatModelProvider
                    .getModel(bottomState)
                    .getParticleIcon(modelData);
        }

        BlockState topState =
                NeoForgeCopycatBlockStateHelper.getCopiedState(
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