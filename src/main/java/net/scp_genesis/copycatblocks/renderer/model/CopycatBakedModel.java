package net.scp_genesis.copycatblocks.renderer.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.copycatblocks.data.CopycatPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.scp_genesis.copycatblocks.provider.CopycatModelProvider;

import java.util.EnumMap;
import java.util.List;

public final class CopycatBakedModel implements BakedModel {

    private final BakedModel baseModel;
    private final BakedModel bottomModel;
    private final BakedModel topModel;
    private final BakedModel doubleModel;

    private BakedModel getBottomModel() {
        return java.util.Objects.requireNonNull(bottomModel);
    }

    private BakedModel getTopModel() {
        return java.util.Objects.requireNonNull(topModel);
    }

    private BakedModel getDoubleModel() {
        return java.util.Objects.requireNonNull(doubleModel);
    }

    /**
     * Constructor for COPYCAT_CUBE.
     *
     * @param baseModel base baked model used by the Copycat Cube.
     */
    public CopycatBakedModel(@NotNull BakedModel baseModel) {
        this.baseModel = baseModel;
        this.bottomModel = null;
        this.topModel = null;
        this.doubleModel = null;
    }

    /**
     * Constructor for COPYCAT_SLAB.
     *
     * @param bottomModel baked model used for the bottom half.
     * @param topModel baked model used for the top half.
     * @param doubleModel baked model used for the double slab.
     */
    public CopycatBakedModel(
            @NotNull BakedModel bottomModel,
            @NotNull BakedModel topModel,
            @NotNull BakedModel doubleModel
    ) {
        this.baseModel = null;
        this.bottomModel = bottomModel;
        this.topModel = topModel;
        this.doubleModel = doubleModel;
    }

    /**
     * @deprecated Forge: Use {@link #getQuads(BlockState, Direction, RandomSource, ModelData, RenderType)}
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

    private static BlockState getCopiedState(
            ModelData modelData,
            CopycatPart part
    ) {
        EnumMap<CopycatPart, BlockState> copiedStates =
                modelData.get(CopycatModelProperties.COPIED_STATES);

        if (copiedStates == null) {
            return null;
        }

        return copiedStates.get(part);
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
         * ------------------------------------------------------------
         * Simple Copycat Block
         * ------------------------------------------------------------
         */
        if (baseModel != null) {

            BlockState copiedState =
                    getCopiedState(
                            modelData,
                            CopycatPart.MAIN
                    );

            if (copiedState == null || copiedState.isAir()) {
                return baseModel.getQuads(
                        state,
                        side,
                        random,
                        modelData,
                        renderType
                );
            }

            return baseModel.getQuads(
                    copiedState,
                    side,
                    random,
                    modelData,
                    renderType
            );
        }

        /*
         * ------------------------------------------------------------
         * Multi-part Copycat Block
         * ------------------------------------------------------------
         */
        if (state == null) {
            return List.of();
        }

        List<BakedQuad> quads = new java.util.ArrayList<>();

        net.minecraft.world.level.block.state.properties.SlabType slabType =
                state.getValue(
                        net.minecraft.world.level.block.state.properties.BlockStateProperties.SLAB_TYPE
                );

        /*
         * Bottom
         */
        if (slabType ==
                net.minecraft.world.level.block.state.properties.SlabType.BOTTOM
                || slabType ==
                net.minecraft.world.level.block.state.properties.SlabType.DOUBLE) {

            BlockState copiedState =
                    getCopiedState(
                            modelData,
                            CopycatPart.BOTTOM
                    );

            if (copiedState != null && !copiedState.isAir()) {
                quads.addAll(
                        getBottomModel().getQuads(
                                copiedState,
                                side,
                                random,
                                modelData,
                                renderType
                        )
                );
            }
        }

        /*
         * Top
         */
        if (slabType ==
                net.minecraft.world.level.block.state.properties.SlabType.TOP
                || slabType ==
                net.minecraft.world.level.block.state.properties.SlabType.DOUBLE) {

            BlockState copiedState =
                    getCopiedState(
                            modelData,
                            CopycatPart.TOP
                    );

            if (copiedState != null && !copiedState.isAir()) {
                quads.addAll(
                        getTopModel().getQuads(
                                copiedState,
                                side,
                                random,
                                modelData,
                                renderType
                        )
                );
            }
        }

        /*
         * Empty Copycat Slab:
         * use the normal Copycat texture.
         */
        if (quads.isEmpty()) {

            return switch (slabType) {
                case TOP -> getTopModel().getQuads(
                        state,
                        side,
                        random,
                        modelData,
                        renderType
                );

                case DOUBLE -> getDoubleModel().getQuads(
                        state,
                        side,
                        random,
                        modelData,
                        renderType
                );

                default -> getBottomModel().getQuads(
                        state,
                        side,
                        random,
                        modelData,
                        renderType
                );
            };
        }

        return quads;
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(
            @Nullable BlockState state,
            @NotNull RandomSource random,
            @NotNull ModelData modelData
    ) {
        BlockState copiedState =
                getCopiedState(
                        modelData,
                        CopycatPart.MAIN
                );

        if (copiedState == null || copiedState.isAir()) {
            return getReferenceModel().getRenderTypes(
                    state,
                    random,
                    modelData
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

    private BakedModel getReferenceModel() {
        if (baseModel != null) {
            return baseModel;
        }

        return doubleModel;
    }

    @Override
    public boolean useAmbientOcclusion() {return getReferenceModel().useAmbientOcclusion();}

    @Override
    public boolean usesBlockLight() {return getReferenceModel().usesBlockLight();}

    @Override
    public boolean isGui3d() {return getReferenceModel().isGui3d();}

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    /**
     * @deprecated Forge: Use {@link #getParticleIcon(ModelData)}
     */
    @Deprecated
    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {return getReferenceModel().getParticleIcon();}

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData modelData) {
        BlockState copiedState = getCopiedState(modelData, CopycatPart.MAIN);

        if (copiedState == null || copiedState.isAir()) {return getReferenceModel().getParticleIcon(modelData);}

        return CopycatModelProvider.getModel(copiedState).getParticleIcon(modelData);
    }

    /**
     * @deprecated Forge: Use {@link #applyTransform(ItemDisplayContext, PoseStack, boolean)} instead
     */
    @Deprecated
    @Override
    public @NotNull ItemTransforms getTransforms() {return getReferenceModel().getTransforms();}

    @Override
    public @NotNull ItemOverrides getOverrides() {return getReferenceModel().getOverrides();}
}
