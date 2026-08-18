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
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.copycatblocks.renderer.geometry.CopycatGeometry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private final CopycatGeometry geometry;


    /*
     * ================================================================
     * CONSTRUCTORS
     * ================================================================
     */

    /**
     * Constructor used by simple Copycat Blocks.
     */
    public CopycatBakedModel(
            @NotNull CopycatGeometry geometry
    ) {
        this.geometry = geometry;
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
        return geometry.getQuads(
                state,
                side,
                random,
                modelData,
                renderType
        );
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
        return geometry.getRenderTypes(
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
        return geometry.getModel();
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
        return geometry.getParticleIcon(modelData);
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