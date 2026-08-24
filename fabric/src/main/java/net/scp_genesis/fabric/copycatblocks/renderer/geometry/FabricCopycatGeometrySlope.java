package net.scp_genesis.fabric.copycatblocks.renderer.geometry;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
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
import net.scp_genesis.common.copycatblocks.geometry.CopycatSlopeGeometry;

import net.scp_genesis.fabric.copycatblocks.renderer.FabricCopycatRenderer;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class FabricCopycatGeometrySlope
        implements FabricCopycatGeometry {

    private static final TextureAtlasSprite EMPTY_PARTICLE =
            null;

    @Override
    public @NotNull BakedModel getModel() {
        throw new UnsupportedOperationException(
                "Copycat Slope does not use a vanilla BakedModel"
        );
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

        if (copiedState == null || copiedState.isAir()) {
            emitGeometry(
                    facing,
                    half,
                    context,
                    FabricCopycatRenderer.getCutoutMaterial(),
                    null
            );

            return;
        }

        /*
         * ============================================================
         * CAMOUFLAGED SLOPE
         * ============================================================
         *
         * The geometry remains the Slope geometry.
         * Only the texture is taken from the copied block.
         */

        emitGeometry(
                facing,
                half,
                context,
                FabricCopycatRenderer.getCutoutMaterial(),
                copiedState
        );
    }

    private static void emitGeometry(
            @NotNull Direction facing,
            @NotNull Half half,
            @NotNull RenderContext context,
            @NotNull RenderMaterial material,
            BlockState copiedState
    ) {
        CopycatSlopeGeometry.Face[] faces =
                CopycatSlopeGeometry.getFaces(
                        facing,
                        half
                );

        QuadEmitter emitter =
                context.getEmitter();

        for (CopycatSlopeGeometry.Face face : faces) {

            CopycatSlopeGeometry.Vertex[] vertices =
                    face.vertices();

            if (vertices.length == 4) {
                emitQuad(
                        emitter,
                        vertices,
                        face.direction(),
                        material
                );
            }
        }
    }

    private static void emitQuad(
            @NotNull QuadEmitter emitter,
            @NotNull CopycatSlopeGeometry.Vertex[] vertices,
            @NotNull Direction direction,
            @NotNull RenderMaterial material
    ) {
        emitter.pos(
                MutableQuadView.BAKE_LOCK_UV,
                vertices[0].x(),
                vertices[0].y(),
                vertices[0].z()
        );

        emitter.pos(
                MutableQuadView.BAKE_LOCK_UV,
                vertices[1].x(),
                vertices[1].y(),
                vertices[1].z()
        );

        emitter.pos(
                MutableQuadView.BAKE_LOCK_UV,
                vertices[2].x(),
                vertices[2].y(),
                vertices[2].z()
        );

        emitter.pos(
                MutableQuadView.BAKE_LOCK_UV,
                vertices[3].x(),
                vertices[3].y(),
                vertices[3].z()
        );

        emitter.nominalFace(direction);

        emitter.emit();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return EMPTY_PARTICLE;
    }
}