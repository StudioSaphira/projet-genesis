package net.scp_genesis.fabric.copycatblocks.renderer.geometry;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
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
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatQuadHelper;

import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public final class FabricCopycatGeometrySlope
        implements FabricCopycatGeometry {

    @Override
    public @NotNull BakedModel getModel() {
        throw new UnsupportedOperationException(
                "Copycat Slope does not use a vanilla BakedModel"
        );
    }

    @Override
    public void emitBlockQuads(
            @NotNull BlockAndTintGetter blockView,
            @NotNull BlockState state,
            @NotNull BlockPos pos,
            @NotNull Supplier<RandomSource> randomSupplier,
            @NotNull RenderContext context
    ) {
        Direction facing =
                state.getValue(
                        BlockStateProperties.HORIZONTAL_FACING
                );

        Half half =
                state.getValue(
                        BlockStateProperties.HALF
                );

        CopycatSlopeGeometry.Face[] faces =
                CopycatSlopeGeometry.getFaces(
                        facing,
                        half
                );

        BlockState copiedState = null;

        BlockEntity blockEntity =
                blockView.getBlockEntity(pos);

        if (blockEntity instanceof CopycatBlockEntity copycat) {
            copiedState =
                    copycat.getCopiedStates()
                            .get(CopycatPart.MAIN);
        }

        /*
         * Empty Copycat:
         * render the raw Slope geometry.
         */
        if (copiedState == null || copiedState.isAir()) {

            emitGeometry(
                    faces,
                    context
            );

            return;
        }

        /*
         * Non-empty Copycat:
         * the geometry remains the Slope geometry,
         * only its appearance is copied.
         */
        emitGeometry(
                faces,
                context
        );
    }

    private static void emitGeometry(
            @NotNull CopycatSlopeGeometry.Face[] faces,
            @NotNull RenderContext context
    ) {
        QuadEmitter emitter =
                context.getEmitter();

        RenderMaterial material =
                FabricCopycatRenderer.getCutoutMaterial();

        for (CopycatSlopeGeometry.Face face : faces) {

            CopycatSlopeGeometry.Vertex[] vertices =
                    face.vertices();

            /*
             * A face must contain either 3 or 4 vertices.
             */
            if (vertices.length == 3) {

                emitter.pos(
                        0,
                        vertices[0].x(),
                        vertices[0].y(),
                        vertices[0].z()
                );

                emitter.pos(
                        1,
                        vertices[1].x(),
                        vertices[1].y(),
                        vertices[1].z()
                );

                emitter.pos(
                        2,
                        vertices[2].x(),
                        vertices[2].y(),
                        vertices[2].z()
                );

                /*
                 * Duplicate the third vertex to form
                 * a degenerate quad.
                 */
                emitter.pos(
                        3,
                        vertices[2].x(),
                        vertices[2].y(),
                        vertices[2].z()
                );

            } else if (vertices.length == 4) {

                for (int i = 0; i < 4; i++) {

                    CopycatSlopeGeometry.Vertex vertex =
                            vertices[i];

                    emitter.pos(
                            i,
                            vertex.x(),
                            vertex.y(),
                            vertex.z()
                    );
                }

            } else {
                throw new IllegalStateException(
                        "Invalid Copycat Slope face vertex count: "
                                + vertices.length
                );
            }

            emitter.nominalFace(
                    face.direction()
            );

            emitter.material(
                    material
            );

            emitter.emit();
        }
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        throw new UnsupportedOperationException(
                "Copycat Slope has no vanilla particle model"
        );
    }
}