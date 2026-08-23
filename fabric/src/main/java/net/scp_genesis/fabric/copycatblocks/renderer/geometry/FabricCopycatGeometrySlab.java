package net.scp_genesis.fabric.copycatblocks.renderer.geometry;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatQuadHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

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
        CopycatBlockEntity copycat = null;

        BlockEntity blockEntity =
                blockView.getBlockEntity(pos);

        if (blockEntity instanceof CopycatBlockEntity copycatBlockEntity) {
            copycat = copycatBlockEntity;
        }

        /*
         * ============================================================
         * NO COPYCAT BLOCK ENTITY
         * ============================================================
         */

        if (copycat == null) {
            emitBaseModel(
                    bottomModel,
                    blockView,
                    state,
                    pos,
                    randomSupplier,
                    context
            );

            return;
        }

        SlabType slabType =
                state.getValue(
                        BlockStateProperties.SLAB_TYPE
                );

        /*
         * ============================================================
         * BOTTOM
         * ============================================================
         */

        if (slabType == SlabType.BOTTOM) {

            BlockState copiedState =
                    copycat.getCopiedStates()
                            .get(CopycatPart.BOTTOM);

            if (copiedState == null || copiedState.isAir()) {

                emitBaseModel(
                        bottomModel,
                        blockView,
                        state,
                        pos,
                        randomSupplier,
                        context
                );

                return;
            }

            FabricCopycatQuadHelper.retextureModel(
                    bottomModel,
                    blockView,
                    state,
                    pos,
                    copiedState,
                    randomSupplier,
                    context,
                    CopycatPart.BOTTOM
            );

            return;
        }

        /*
         * ============================================================
         * TOP
         * ============================================================
         */

        if (slabType == SlabType.TOP) {

            BlockState copiedState =
                    copycat.getCopiedStates()
                            .get(CopycatPart.TOP);

            if (copiedState == null || copiedState.isAir()) {

                emitBaseModel(
                        topModel,
                        blockView,
                        state,
                        pos,
                        randomSupplier,
                        context
                );

                return;
            }

            FabricCopycatQuadHelper.retextureModel(
                    topModel,
                    blockView,
                    state,
                    pos,
                    copiedState,
                    randomSupplier,
                    context,
                    CopycatPart.TOP
            );

            return;
        }

        /*
         * ============================================================
         * DOUBLE
         * ============================================================
         *
         * A DOUBLE slab is composed of two logical Copycat parts:
         *
         *     BOTTOM -> bottomModel
         *     TOP    -> doubleSecondaryModel
         */

        if (slabType == SlabType.DOUBLE) {

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

            /*
             * --------------------------------------------------------
             * BOTTOM HALF
             * --------------------------------------------------------
             */

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

                emitBaseModel(
                        bottomModel,
                        blockView,
                        state,
                        pos,
                        randomSupplier,
                        context
                );
            }

            /*
             * --------------------------------------------------------
             * TOP HALF
             * --------------------------------------------------------
             */

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

                emitBaseModel(
                        doubleSecondaryModel,
                        blockView,
                        state,
                        pos,
                        randomSupplier,
                        context
                );
            }

            return;
        }

        /*
         * ============================================================
         * FALLBACK
         * ============================================================
         */

        emitBaseModel(
                bottomModel,
                blockView,
                state,
                pos,
                randomSupplier,
                context
        );
    }

    private static void emitBaseModel(
            @NotNull BakedModel model,
            @NotNull BlockAndTintGetter blockView,
            @NotNull BlockState state,
            @NotNull BlockPos pos,
            @NotNull Supplier<RandomSource> randomSupplier,
            @NotNull RenderContext context
    ) {
        model.emitBlockQuads(
                blockView,
                state,
                pos,
                randomSupplier,
                context
        );
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return bottomModel.getParticleIcon();
    }
}