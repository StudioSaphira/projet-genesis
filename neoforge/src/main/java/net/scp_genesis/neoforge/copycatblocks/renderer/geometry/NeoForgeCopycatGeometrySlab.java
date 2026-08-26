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

    /**
     * Returns the model used for the given Copycat part.
     *
     * <p>The TOP part uses a different baked model when the
     * slab is double.</p>
     */
    private BakedModel getModel(
            CopycatPart part,
            SlabType slabType
    ) {
        return switch (part) {
            case BOTTOM ->
                    bottomModel;

            case TOP ->
                    slabType == SlabType.TOP
                            ? topModel
                            : doubleSecondaryModel;

            default ->
                    throw new IllegalArgumentException(
                            "Unsupported Copycat Slab part: " + part
                    );
        };
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
                CopycatGeometrySlab.getSlabType(state);

        List<BakedQuad> result =
                new ArrayList<>();

        /*
         * ============================================================
         * COPYCAT PARTS
         * ============================================================
         *
         * The common geometry determines which logical parts
         * compose this slab.
         */

        for (CopycatPart part :
                CopycatGeometrySlab.getParts(state)) {

            BakedModel model =
                    getModel(
                            part,
                            slabType
                    );

            BlockState copiedState =
                    NeoForgeCopycatBlockStateHelper.getCopiedState(
                            modelData,
                            part
                    );

            /*
             * --------------------------------------------------------
             * NO COPIED STATE
             * --------------------------------------------------------
             */

            if (copiedState == null || copiedState.isAir()) {

                result.addAll(
                        model.getQuads(
                                state,
                                side,
                                random,
                                modelData,
                                renderType
                        )
                );

                continue;
            }

            /*
             * --------------------------------------------------------
             * RETEXTURED PART
             * --------------------------------------------------------
             */

            result.addAll(
                    NeoForgeCopycatQuadHelper.retextureModel(
                            model,
                            state,
                            copiedState,
                            side,
                            random,
                            renderType,
                            modelData,
                            part
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
        /*
         * ============================================================
         * NO STATE
         * ============================================================
         */

        if (state == null) {
            return ChunkRenderTypeSet.of(
                    RenderType.cutout()
            );
        }

        SlabType slabType =
                CopycatGeometrySlab.getSlabType(state);

        List<CopycatPart> parts =
                CopycatGeometrySlab.getParts(slabType);

        /*
         * ============================================================
         * RENDER TYPES
         * ============================================================
         *
         * A slab may contain one or two different copied blocks.
         * Their render types therefore need to be combined.
         */

        ChunkRenderTypeSet result =
                ChunkRenderTypeSet.of(
                        RenderType.cutout()
                );

        boolean hasCopiedState = false;

        for (CopycatPart part : parts) {

            BlockState copiedState =
                    NeoForgeCopycatBlockStateHelper.getCopiedState(
                            modelData,
                            part
                    );

            if (copiedState == null || copiedState.isAir()) {
                continue;
            }

            hasCopiedState = true;

            BakedModel copiedModel =
                    CopycatModelProvider.getModel(
                            copiedState
                    );

            ChunkRenderTypeSet renderTypes =
                    copiedModel.getRenderTypes(
                            copiedState,
                            random,
                            modelData
                    );

            result =
                    ChunkRenderTypeSet.union(
                            result,
                            renderTypes
                    );
        }

        if (!hasCopiedState) {
            return ChunkRenderTypeSet.of(
                    RenderType.cutout()
            );
        }

        return result;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(
            @NotNull ModelData modelData
    ) {
        /*
         * A slab has no MAIN part.
         *
         * Use the first available copied part as the particle
         * texture, following the common part ordering.
         */

        for (CopycatPart part :
                List.of(
                        CopycatPart.BOTTOM,
                        CopycatPart.TOP
                )) {

            BlockState copiedState =
                    NeoForgeCopycatBlockStateHelper.getCopiedState(
                            modelData,
                            part
                    );

            if (copiedState == null || copiedState.isAir()) {
                continue;
            }

            return CopycatModelProvider
                    .getModel(copiedState)
                    .getParticleIcon(modelData);
        }

        return bottomModel.getParticleIcon(
                modelData
        );
    }
}