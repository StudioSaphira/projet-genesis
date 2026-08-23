package net.scp_genesis.fabric.copycatblocks.renderer.geometry;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatQuadHelper;
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatStairsModelHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public final class FabricCopycatGeometryStairs
        implements FabricCopycatGeometry {

    private final Map<
            FabricCopycatStairsModelHelper.StairModelKey,
            BakedModel
            > models;

    public FabricCopycatGeometryStairs(
            @NotNull Map<
                    FabricCopycatStairsModelHelper.StairModelKey,
                    BakedModel
                    > models
    ) {
        this.models = models;
    }

    private static final FabricCopycatStairsModelHelper.StairModelKey DEFAULT_MODEL_KEY =
            new FabricCopycatStairsModelHelper.StairModelKey(
                    Direction.EAST,
                    Half.BOTTOM,
                    StairsShape.STRAIGHT
            );

    /**
     * Returns the BakedModel corresponding to the current
     * Stair BlockState.
     */
    private BakedModel getModel(
            @Nullable BlockState state
    ) {
        if (state == null) {
            return getModel();
        }

        Direction facing =
                state.getValue(
                        BlockStateProperties.HORIZONTAL_FACING
                );

        Half half =
                state.getValue(
                        BlockStateProperties.HALF
                );

        StairsShape shape =
                state.getValue(
                        BlockStateProperties.STAIRS_SHAPE
                );

        FabricCopycatStairsModelHelper.StairModelKey key =
                new FabricCopycatStairsModelHelper.StairModelKey(
                        facing,
                        half,
                        shape
                );

        BakedModel model =
                models.get(key);

        if (model == null) {
            throw new IllegalStateException(
                    "Missing Copycat Stairs model for: "
                            + key
            );
        }

        return model;
    }

    @Override
    public @NotNull BakedModel getModel() {
        BakedModel model =
                models.get(DEFAULT_MODEL_KEY);

        if (model == null) {
            throw new IllegalStateException(
                    "Missing default Copycat Stairs model"
            );
        }

        return model;
    }

    @Override
    public void emitBlockQuads(
            BlockAndTintGetter blockView,
            BlockState state,
            BlockPos pos,
            Supplier<RandomSource> randomSupplier,
            RenderContext context
    ) {
        BakedModel geometryModel =
                getModel(state);

        BlockState copiedState = null;

        BlockEntity blockEntity =
                blockView.getBlockEntity(pos);

        if (blockEntity instanceof CopycatBlockEntity copycat) {
            copiedState =
                    copycat.getCopiedStates()
                            .get(CopycatPart.MAIN);
        }

        /*
         * No copied state:
         * render the original stair model.
         */
        if (copiedState == null || copiedState.isAir()) {
            geometryModel.emitBlockQuads(
                    blockView,
                    state,
                    pos,
                    randomSupplier,
                    context
            );

            return;
        }

        /*
         * Retexture the stair geometry using
         * the copied block model.
         */
        FabricCopycatQuadHelper.retextureModel(
                geometryModel,
                blockView,
                state,
                pos,
                copiedState,
                randomSupplier,
                context,
                CopycatPart.MAIN
        );
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return getModel(null).getParticleIcon();
    }
}