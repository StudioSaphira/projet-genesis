package net.scp_genesis.fabric.copycatblocks.renderer.geometry;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

/**
 * Defines the geometry of a Copycat Block.
 *
 * <p>A Copycat geometry is responsible for generating the physical
 * geometry of a Copycat Block independently of its appearance.</p>
 */
public interface FabricCopycatGeometry {

    /**
     * Emits the quads used to render this geometry.
     */
    void emitBlockQuads(
            BlockAndTintGetter blockView,
            BlockState state,
            BlockPos pos,
            Supplier<RandomSource> randomSupplier,
            RenderContext context
    );

    /**
     * Returns the original baked model used by this geometry.
     */
    BakedModel getModel();

    /**
     * Returns the particle texture used by this geometry.
     */
    TextureAtlasSprite getParticleIcon();
}