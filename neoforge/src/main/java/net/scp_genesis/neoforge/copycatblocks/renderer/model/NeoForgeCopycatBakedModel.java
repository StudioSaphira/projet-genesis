package net.scp_genesis.neoforge.copycatblocks.renderer.model;

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
import net.scp_genesis.neoforge.copycatblocks.renderer.geometry.NeoForgeCopycatGeometry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class NeoForgeCopycatBakedModel implements BakedModel {

    /**
     * <h1>GEOMETRY</h1>
     * ================================================================
     */
    private final NeoForgeCopycatGeometry geometry;

    /**
     * <h1>CONSTRUCTOR</h1>
     * ================================================================
     */
    public NeoForgeCopycatBakedModel(
            @NotNull NeoForgeCopycatGeometry geometry
    ) {
        this.geometry = geometry;
    }

    /**
     * <h1>QUADS</h1>
     * ================================================================
     *
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

    /**
     * <h1>RENDER TYPES</h1>
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

    /**
     * <h1>REFERENCE MODEL</h1>
     * ================================================================
     *
     * <p>Returns the optional reference model of the geometry.</p>
     *
     * <p>Traditional Copycat geometries such as Cube, Slab and
     * Stairs provide one. Autonomous geometries such as Slope may
     * return {@code null}.</p>
     */
    @Nullable
    private BakedModel getReferenceModel() {
        return geometry.getModel();
    }

    /**
     * <h1>COMMON MODEL PROPERTIES</h1>
     * ================================================================
     *
     * <p>When a geometry provides a reference model, its properties
     * are delegated to that model. Otherwise, sensible defaults are
     * used for autonomous geometries.</p>
     */

    @Override
    public boolean useAmbientOcclusion() {
        BakedModel referenceModel =
                getReferenceModel();

        return referenceModel != null
                && referenceModel.useAmbientOcclusion();
    }

    @Override
    public boolean usesBlockLight() {
        BakedModel referenceModel =
                getReferenceModel();

        return referenceModel == null
                || referenceModel.usesBlockLight();
    }

    @Override
    public boolean isGui3d() {
        BakedModel referenceModel =
                getReferenceModel();

        return referenceModel == null
                || referenceModel.isGui3d();
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    /**
     * <h1>PARTICLE</h1>
     * ================================================================
     *
     * @deprecated Use the ModelData-aware overload.
     */
    @Deprecated
    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        BakedModel referenceModel =
                getReferenceModel();

        if (referenceModel == null) {
            throw new IllegalStateException(
                    "Copycat geometry does not provide a reference model"
            );
        }

        return referenceModel.getParticleIcon();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(
            @NotNull ModelData modelData
    ) {
        return geometry.getParticleIcon(
                modelData
        );
    }

    /**
     * <h1>TRANSFORMS</h1>
     * ================================================================
     *
     * <p>Item transforms only apply to geometries which provide a
     * reference baked model.</p>
     *
     * @deprecated Use the modern transform API.
     */
    @Override
    @SuppressWarnings("deprecation")
    @Deprecated
    public @NotNull ItemTransforms getTransforms() {
        return geometry.getTransforms();
    }

    /**
     * <h1>OVERRIDES</h1>
     * ================================================================
     */
    @Override
    public @NotNull ItemOverrides getOverrides() {
        BakedModel referenceModel =
                getReferenceModel();

        if (referenceModel == null) {
            return ItemOverrides.EMPTY;
        }

        return referenceModel.getOverrides();
    }
}