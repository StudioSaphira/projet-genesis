package net.scp_genesis.fabric.copycatblocks.renderer.geometry;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;

import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatGeometrySlope;

import net.scp_genesis.fabric.copycatblocks.renderer.util.dedicated.FabricCopycatSlopeHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@SuppressWarnings("ClassCanBeRecord")
public final class FabricCopycatGeometrySlope implements FabricCopycatGeometry {

    @Override
    public void emitBlockQuads(
            BlockAndTintGetter blockView,
            BlockState state,
            BlockPos pos,
            Supplier<RandomSource> randomSupplier,
            RenderContext context
    ) {
        Direction facing =
                state.getValue(
                        BlockStateProperties.HORIZONTAL_FACING
                );

        Half half =
                state.getValue(
                        BlockStateProperties.HALF
                );

        CopycatBlockEntity copycat =
                null;

        BlockEntity blockEntity =
                blockView.getBlockEntity(pos);

        if (blockEntity instanceof CopycatBlockEntity copycatBlockEntity) {
            copycat = copycatBlockEntity;
        }

        BlockState copiedState =
                copycat != null
                        ? copycat.getCopiedStates()
                        .get(CopycatPart.MAIN)
                        : null;

        /*
         * ============================================================
         * EMPTY SLOPE
         * ============================================================
         *
         * The Slope has no vanilla base model.
         * Its geometry is generated directly from CopycatSlopeGeometry.
         */

        CopycatGeometrySlope.Face[] faces =
                CopycatGeometrySlope.getFaces(
                        facing,
                        half
                );

        if (copiedState == null || copiedState.isAir()) {

            FabricCopycatSlopeHelper.emitEmpty(
                    faces,
                    particleIcon,
                    context
            );

            return;
        }

        FabricCopycatSlopeHelper.retexture(
                faces,
                copiedState,
                randomSupplier,
                context,
                CopycatPart.MAIN
        );
    }

    private final TextureAtlasSprite particleIcon;

    public FabricCopycatGeometrySlope(@NotNull TextureAtlasSprite particleIcon) {this.particleIcon = particleIcon;}

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return particleIcon;
    }
}