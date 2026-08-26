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

        /*
         * ============================================================
         * SINGLE SLAB
         * ============================================================
         */

        if (!CopycatGeometrySlab.isDouble(slabType)) {

            CopycatPart part =
                    CopycatGeometrySlab.getPart(slabType);

            BlockState copiedState =
                    copycat.getCopiedStates().get(part);

            BakedModel model =
                    slabType == SlabType.BOTTOM
                            ? bottomModel
                            : topModel;

            if (copiedState == null || copiedState.isAir()) {
                FabricCopycatQuadHelper.emitBaseModel(
                        model,
                        state,
                        randomSupplier,
                        context
                );

                return;
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

            return;
        }

        /*
         * ============================================================
         * DOUBLE
         * ============================================================
         *
         * BOTTOM -> bottomModel
         * TOP    -> doubleSecondaryModel
         */

        BlockState bottomState =
                copycat.getCopiedStates()
                        .get(CopycatPart.BOTTOM);

        BlockState topState =
                copycat.getCopiedStates()
                        .get(CopycatPart.TOP);

        boolean hasBottom =
                bottomState != null
                        && !bottomState.isAir();

        boolean hasTop =
                topState != null
                        && !topState.isAir();

        if (hasBottom) {
            FabricCopycatQuadHelper.retextureModel(
                    bottomModel,
                    blockView,
                    state,
                    pos,
                    bottomState,
                    randomSupplier,
                    context,
                    CopycatPart.BOTTOM
            );
        } else {
            FabricCopycatQuadHelper.emitBaseModel(
                    bottomModel,
                    state,
                    randomSupplier,
                    context
            );
        }

        if (hasTop) {
            FabricCopycatQuadHelper.retextureModel(
                    doubleSecondaryModel,
                    blockView,
                    state,
                    pos,
                    topState,
                    randomSupplier,
                    context,
                    CopycatPart.TOP
            );
        } else {
            FabricCopycatQuadHelper.emitBaseModel(
                    doubleSecondaryModel,
                    state,
                    randomSupplier,
                    context
            );
        }
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return bottomModel.getParticleIcon();
    }
}