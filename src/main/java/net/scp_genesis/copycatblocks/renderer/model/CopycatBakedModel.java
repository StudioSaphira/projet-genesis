package net.scp_genesis.copycatblocks.renderer.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Blocks;
import net.scp_genesis.copycatblocks.provider.CopycatModelProvider;

import java.util.List;

public final class CopycatBakedModel implements BakedModel {


    public CopycatBakedModel() {
    }

    /**
     * @deprecated Forge: Use {@link #getQuads(BlockState, Direction, RandomSource, ModelData, RenderType)}
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

        BlockState copiedState =
                modelData.get(CopycatModelProperties.COPIED_STATE);

        BakedModel model = CopycatModelProvider.getModel(copiedState);

        return model.getQuads(
                state,
                side,
                random,
                modelData,
                renderType
        );
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    /**
     * @deprecated Forge: Use {@link #getParticleIcon(ModelData)}
     */
    @Deprecated
    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return Minecraft.getInstance()
                .getBlockRenderer()
                .getBlockModel(Blocks.STONE.defaultBlockState())
                .getParticleIcon();
    }

    /**
     * @deprecated Forge: Use {@link #applyTransform(ItemDisplayContext, PoseStack, boolean)} instead
     */
    @Deprecated
    @Override
    public @NotNull ItemTransforms getTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
