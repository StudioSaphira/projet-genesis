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
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatQuadHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Geometry implementation for standard Copycat Cubes.
 *
 * <p>The Cube geometry represents a standard full-block shape.
 * Its appearance is handled separately by the Copycat rendering system.</p>
 */
public final class FabricCopycatGeometryCube implements FabricCopycatGeometry {

    private final BakedModel baseModel;

    public FabricCopycatGeometryCube(
            @NotNull BakedModel baseModel
    ) {
        this.baseModel = baseModel;
    }

    @Override
    public @NotNull BakedModel getModel() {
        return baseModel;
    }

    @Override
    public void emitBlockQuads(
            BlockAndTintGetter blockView,
            BlockState state,
            BlockPos pos,
            Supplier<RandomSource> randomSupplier,
            RenderContext context
    ) {
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
         * render the original base model.
         */
        if (copiedState == null || copiedState.isAir()) {
            FabricCopycatQuadHelper.emitBaseModel(
                    baseModel,
                    state,
                    randomSupplier,
                    context
            );

            return;
        }

        /*
         * The actual Copycat retexturing will be implemented
         * by FabricCopycatQuadHelper.
         */
        FabricCopycatQuadHelper.retextureModel(
                baseModel,
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
        return baseModel.getParticleIcon();
    }
}