package net.scp_genesis.fabric.copycatblocks.renderer.model;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.fabric.copycatblocks.renderer.geometry.FabricCopycatGeometry;
import net.scp_genesis.fabric.copycatblocks.renderer.geometry.FabricCopycatGeometrySlope;
import net.scp_genesis.fabric.copycatblocks.renderer.model.dedicated.FabricCopycatSlopeItemModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public final class FabricCopycatBakedModel implements BakedModel, FabricBakedModel {

    private final FabricCopycatGeometry geometry;

    public FabricCopycatBakedModel(@NotNull FabricCopycatGeometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public boolean isVanillaAdapter() {return false;}

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
    public void emitItemQuads(
            ItemStack stack,
            Supplier<RandomSource> randomSupplier,
            RenderContext context
    ) {
        if (geometry instanceof FabricCopycatGeometrySlope) {

            FabricCopycatSlopeItemModel.emit(
                    geometry.getParticleIcon(),
                    context
            );

            return;
        }
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(
            BlockState state,
            Direction side,
            RandomSource random
    ) {
        BakedModel model = geometry.getModel();

        if (model == null) {return List.of();}

        return model.getQuads(
                state,
                side,
                random
        );
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return geometry.getParticleIcon();
    }

    @Override
    public @NotNull ItemTransforms getTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}