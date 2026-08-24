package net.scp_genesis.fabric.copycatblocks.renderer.model;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.fabric.copycatblocks.renderer.geometry.FabricCopycatGeometry;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public final class FabricCopycatBakedModel
        implements BakedModel {

    private final FabricCopycatGeometry geometry;

    public FabricCopycatBakedModel(
            @NotNull FabricCopycatGeometry geometry
    ) {
        this.geometry = geometry;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(
            BlockAndTintGetter blockView,
            BlockState state,
            BlockPos pos,
            Supplier<RandomSource> randomSupplier,
            RenderContext context
    ) {
        geometry.emitBlockQuads(
                blockView,
                state,
                pos,
                randomSupplier,
                context
        );
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(
            BlockState state,
            Direction side,
            RandomSource random
    ) {
        System.out.println(
                "[COPYCAT ITEM DEBUG] getQuads"
                        + " | state=" + state
                        + " | side=" + side
        );

        List<BakedQuad> quads =
                geometry.getModel().getQuads(
                        state,
                        side,
                        random
                );

        System.out.println(
                "[COPYCAT ITEM DEBUG] quads="
                        + quads.size()
        );

        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return geometry.getModel().useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return geometry.getModel().isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return geometry.getModel().usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return geometry.getModel().isCustomRenderer();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return geometry.getParticleIcon();
    }

    @Override
    public @NotNull ItemTransforms getTransforms() {
        return geometry.getModel().getTransforms();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return geometry.getModel().getOverrides();
    }
}