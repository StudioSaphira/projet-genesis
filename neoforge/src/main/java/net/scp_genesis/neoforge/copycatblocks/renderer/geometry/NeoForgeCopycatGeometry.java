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
import net.scp_genesis.common.copycatblocks.geometry.CopycatGeometry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * NeoForge-specific rendering contract for a Copycat geometry.
 *
 * <p>A geometry may optionally provide a reference BakedModel.
 * Traditional Copycat geometries such as Cube, Slab and Stairs use
 * this model for common BakedModel properties, while autonomous
 * geometries such as Slope may not require one.</p>
 */
public interface NeoForgeCopycatGeometry extends CopycatGeometry {

    @NotNull
    List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            @NotNull RandomSource random,
            @NotNull ModelData modelData,
            @Nullable RenderType renderType
    );

    /**
     * Returns the optional reference model used by this geometry.
     *
     * <p>Traditional Copycat geometries may return their baked
     * reference model. Autonomous geometries may return {@code null}.</p>
     *
     * @return the reference model, or {@code null} when not applicable
     */
    @Nullable
    default BakedModel getModel() {
        return null;
    }

    @NotNull
    ChunkRenderTypeSet getRenderTypes(
            @Nullable BlockState state,
            @NotNull RandomSource random,
            @NotNull ModelData modelData
    );

    @NotNull
    TextureAtlasSprite getParticleIcon(
            @NotNull ModelData modelData
    );
}