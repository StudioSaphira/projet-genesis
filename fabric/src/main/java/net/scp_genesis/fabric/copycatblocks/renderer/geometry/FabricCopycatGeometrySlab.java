package net.scp_genesis.fabric.copycatblocks.renderer.geometry;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.slab.CopycatGeometrySlab;
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatQuadHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;

public final class FabricCopycatGeometrySlab
        implements FabricCopycatGeometry {

    private final BakedModel bottomModel;
    private final BakedModel topModel;
    private final BakedModel doubleSecondaryModel;
    private final BakedModel doubleModel;

    public FabricCopycatGeometrySlab(
            @NotNull BakedModel bottomModel,
            @NotNull BakedModel topModel,
            @NotNull BakedModel doubleSecondaryModel,
            @NotNull BakedModel doubleModel
    ) {
        this.bottomModel = bottomModel;
        this.topModel = topModel;
        this.doubleSecondaryModel = doubleSecondaryModel;
        this.doubleModel = doubleModel;
    }

    @Override
    public @NotNull BakedModel getModel() {
        return doubleModel;
    }

    @Override
    public void emitBlockQuads(
            BlockAndTintGetter blockView,
            BlockState state,
            BlockPos pos,
            Supplier<RandomSource> randomSupplier,
            RenderContext context
    ) {
        BlockEntity blockEntity =
                blockView.getBlockEntity(pos);

        if (!(blockEntity instanceof CopycatBlockEntity copycat)) {
            FabricCopycatQuadHelper.emitBaseModel(
                    bottomModel,
                    state,
                    randomSupplier,
                    context
            );

            return;
        }

        SlabType slabType =
                CopycatGeometrySlab.getSlabType(state);

        for (CopycatPart part :
                CopycatGeometrySlab.getParts(slabType)) {

            BlockState copiedState =
                    copycat.getCopiedStates().get(part);

            BakedModel model =
                    switch (part) {
                        case BOTTOM -> bottomModel;
                        case TOP -> slabType == SlabType.TOP
                                ? topModel
                                : doubleSecondaryModel;
                        default -> throw new IllegalArgumentException(
                                "Unsupported slab part: " + part
                        );
                    };

            if (copiedState == null || copiedState.isAir()) {
                FabricCopycatQuadHelper.emitBaseModel(
                        model,
                        state,
                        randomSupplier,
                        context
                );

                continue;
            }

            FabricCopycatQuadHelper.retextureModel(
                    model,
                    blockView,
                    state,
                    pos,
                    copiedState,
                    randomSupplier,
                    context,
                    part
            );
        }
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return bottomModel.getParticleIcon();
    }
}