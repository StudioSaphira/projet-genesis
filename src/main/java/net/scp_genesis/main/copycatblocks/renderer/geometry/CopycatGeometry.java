package net.scp_genesis.main.copycatblocks.renderer.geometry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Defines the geometry of a Copycat Block.
 *
 * <p>A Copycat geometry is responsible for generating the physical
 * geometry of a Copycat Block independently of its appearance.</p>
 */
public interface CopycatGeometry {

    /**
     * Generates the quads used to render this geometry.
     *
     * @param state the Copycat BlockState
     * @param side the requested face
     * @param random the random source
     * @param modelData the model data
     * @param renderType the requested render type
     *
     * @return the generated quads
     */
    @NotNull
    List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            @NotNull RandomSource random,
            @NotNull ModelData modelData,
            @Nullable RenderType renderType
    );

    @NotNull
    BakedModel getModel();

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