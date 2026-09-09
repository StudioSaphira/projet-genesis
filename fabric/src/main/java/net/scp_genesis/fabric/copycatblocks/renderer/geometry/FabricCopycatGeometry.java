package net.scp_genesis.fabric.copycatblocks.renderer.geometry;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.geometry.CopycatGeometry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Fabric-specific rendering contract for a Copycat geometry.
 */
public interface FabricCopycatGeometry extends CopycatGeometry {

    void emitBlockQuads(
            BlockAndTintGetter blockView,
            BlockState state,
            BlockPos pos,
            Supplier<RandomSource> randomSupplier,
            RenderContext context
    );

    /**
     * Returns the original baked model used by this geometry.
     *
     * <p>Geometries that generate their shape directly may return
     * {@code null}.</p>
     */
    @Nullable
    default BakedModel getModel() {return null;}

    @NotNull
    TextureAtlasSprite getParticleIcon();
}