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
import net.scp_genesis.common.copycatblocks.geometry.CopycatFace;
import net.scp_genesis.fabric.copycatblocks.renderer.util.dedicated.FabricCopycatSlopeHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * Fabric rendering implementation for Copycat Slopes.
 *
 * <p>The actual Slope geometry is defined in the common module.
 * Fabric is responsible only for emitting that geometry through
 * the Fabric Renderer API.</p>
 */
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
        /*
         * ============================================================
         * SLOPE ORIENTATION
         * ============================================================
         */

        Direction facing =
                state.getValue(
                        BlockStateProperties.HORIZONTAL_FACING
                );

        Half half =
                state.getValue(
                        BlockStateProperties.HALF
                );

        /*
         * ============================================================
         * COPYCAT BLOCK ENTITY
         * ============================================================
         */

        BlockEntity blockEntity =
                blockView.getBlockEntity(pos);

        BlockState copiedState = null;

        if (blockEntity instanceof CopycatBlockEntity copycat) {
            copiedState =
                    copycat.getCopiedStates()
                            .get(CopycatPart.MAIN);
        }

        /*
         * ============================================================
         * SLOPE GEOMETRY
         * ============================================================
         *
         * The geometry itself is entirely defined by the common
         * CopycatGeometrySlope implementation.
         */

        CopycatFace[] faces =
                CopycatGeometrySlope.getFaces(
                        facing,
                        half
                );

        /*
         * ============================================================
         * EMPTY SLOPE
         * ============================================================
         */

        if (copiedState == null || copiedState.isAir()) {

            FabricCopycatSlopeHelper.emitEmpty(
                    faces,
                    particleIcon,
                    context
            );

            return;
        }

        /*
         * ============================================================
         * RETEXTURED SLOPE
         * ============================================================
         */

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