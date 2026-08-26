package net.scp_genesis.fabric.copycatblocks.renderer.geometry;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.stairs.CopycatGeometryStairs;
import net.scp_genesis.common.copycatblocks.geometry.stairs.CopycatStairsModelKey;
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatQuadHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public final class FabricCopycatGeometryStairs
        implements FabricCopycatGeometry {

    private static final CopycatStairsModelKey DEFAULT_MODEL_KEY =
            new CopycatStairsModelKey(
                    net.minecraft.core.Direction.EAST,
                    net.minecraft.world.level.block.state.properties.Half.BOTTOM,
                    net.minecraft.world.level.block.state.properties.StairsShape.STRAIGHT
            );

    private final Map<CopycatStairsModelKey, BakedModel> models;

    public FabricCopycatGeometryStairs(
            @NotNull Map<CopycatStairsModelKey, BakedModel> models
    ) {
        this.models = models;
    }

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

        CopycatStairsModelKey key =
                CopycatGeometryStairs.getModelKey(state);

        BakedModel model = models.get(key);

        if (model == null) {
            throw new IllegalStateException(
                    "Missing Copycat Stairs model for: " + key
            );
        }

        return model;
    }

    @Override
    public @NotNull BakedModel getModel() {
        BakedModel model = models.get(DEFAULT_MODEL_KEY);

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
        BakedModel geometryModel = getModel(state);

        BlockState copiedState = null;

        BlockEntity blockEntity =
                blockView.getBlockEntity(pos);

        if (blockEntity instanceof CopycatBlockEntity copycat) {
            copiedState =
                    copycat.getCopiedStates()
                            .get(CopycatPart.MAIN);
        }

        if (copiedState == null || copiedState.isAir()) {
            FabricCopycatQuadHelper.emitBaseModel(
                    geometryModel,
                    state,
                    randomSupplier,
                    context
            );

            return;
        }

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