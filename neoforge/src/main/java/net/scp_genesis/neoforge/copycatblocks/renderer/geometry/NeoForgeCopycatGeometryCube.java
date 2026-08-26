package net.scp_genesis.neoforge.copycatblocks.renderer.geometry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.neoforge.copycatblocks.provider.NeoForgeCopycatModelProvider;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatBlockStateHelper;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatQuadHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Geometry implementation for standard Copycat Cubes.
 *
 * <p>The Cube geometry represents a standard full-block shape.
 * Its appearance is handled separately by the Copycat rendering system.</p>
 */
public final class NeoForgeCopycatGeometryCube implements NeoForgeCopycatGeometry {

    private final BakedModel baseModel;

    public NeoForgeCopycatGeometryCube(
            @NotNull BakedModel baseModel
    ) {this.baseModel = baseModel;}

    @Override
    public @NotNull BakedModel getModel() {
        return baseModel;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            @NotNull RandomSource random,
            @NotNull ModelData modelData,
            @Nullable RenderType renderType
    ) {
        BlockState copiedState =
                NeoForgeCopycatBlockStateHelper.getCopiedState(
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

        return NeoForgeCopycatQuadHelper.retextureModel(
                baseModel,
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

        return NeoForgeCopycatModelProvider
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
            return baseModel.getParticleIcon(modelData);
        }

        return NeoForgeCopycatModelProvider
                .getModel(copiedState)
                .getParticleIcon(modelData);
    }
}