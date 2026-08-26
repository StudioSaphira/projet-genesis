package net.scp_genesis.neoforge.copycatblocks.renderer.geometry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.stairs.CopycatGeometryStairs;
import net.scp_genesis.common.copycatblocks.geometry.stairs.CopycatStairsModelKey;
import net.scp_genesis.common.copycatblocks.provider.CopycatModelProvider;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatBlockStateHelper;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatQuadHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public final class NeoForgeCopycatGeometryStairs
        implements NeoForgeCopycatGeometry {

    private static final CopycatStairsModelKey DEFAULT_MODEL_KEY =
            new CopycatStairsModelKey(
                    Direction.EAST,
                    Half.BOTTOM,
                    StairsShape.STRAIGHT
            );

    private final Map<CopycatStairsModelKey, BakedModel> models;

    public NeoForgeCopycatGeometryStairs(
            @NotNull Map<CopycatStairsModelKey, BakedModel> models
    ) {
        this.models = models;
    }

    private BakedModel getModel(
            @Nullable BlockState state
    ) {
        if (state == null) {
            return getModel();
        }

        CopycatStairsModelKey key =
                CopycatGeometryStairs.getModelKey(state);

        BakedModel model = models.get(key);

        if (model == null) {
            throw new IllegalStateException(
                    "Missing Copycat Stairs model for: " + key
            );
        }

        return model;
    }

    @Override
    public @NotNull BakedModel getModel() {
        BakedModel model =
                models.get(DEFAULT_MODEL_KEY);

        if (model == null) {
            throw new IllegalStateException(
                    "Missing default Copycat Stairs model"
            );
        }

        return model;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            @NotNull RandomSource random,
            @NotNull ModelData modelData,
            @Nullable RenderType renderType
    ) {
        BakedModel geometryModel =
                getModel(state);

        BlockState copiedState =
                NeoForgeCopycatBlockStateHelper.getCopiedState(
                        modelData,
                        CopycatPart.MAIN
                );

        if (copiedState == null || copiedState.isAir()) {
            return geometryModel.getQuads(
                    state,
                    side,
                    random,
                    modelData,
                    renderType
            );
        }

        return NeoForgeCopycatQuadHelper.retextureModel(
                geometryModel,
                state,
                copiedState,
                side,
                random,
                renderType,
                modelData,
                CopycatPart.MAIN
        );
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(
            @Nullable BlockState state,
            @NotNull RandomSource random,
            @NotNull ModelData modelData
    ) {
        BlockState copiedState =
                NeoForgeCopycatBlockStateHelper.getCopiedState(
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

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(
            @NotNull ModelData modelData
    ) {
        BlockState copiedState =
                NeoForgeCopycatBlockStateHelper.getCopiedState(
                        modelData,
                        CopycatPart.MAIN
                );

        if (copiedState == null || copiedState.isAir()) {
            return getModel(null)
                    .getParticleIcon(modelData);
        }

        return CopycatModelProvider
                .getModel(copiedState)
                .getParticleIcon(modelData);
    }
}