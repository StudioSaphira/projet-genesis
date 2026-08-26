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
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatSlopeFace;
import net.scp_genesis.fabric.copycatblocks.renderer.util.dedicated.FabricCopycatSlopeHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@SuppressWarnings("ClassCanBeRecord")
public final class FabricCopycatGeometrySlope
        implements FabricCopycatGeometry {

    private final TextureAtlasSprite particleIcon;

    public FabricCopycatGeometrySlope(
            @NotNull TextureAtlasSprite particleIcon
    ) {
        this.particleIcon = particleIcon;
    }

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

        CopycatSlopeFace[] faces =
                CopycatGeometrySlope.getFaces(
                        facing,
                        half
                );

        BlockEntity blockEntity =
                blockView.getBlockEntity(pos);

        BlockState copiedState = null;

        if (blockEntity instanceof CopycatBlockEntity copycat) {
            copiedState =
                    copycat.getCopiedStates()
                            .get(CopycatPart.MAIN);
        }

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

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return particleIcon;
    }
}