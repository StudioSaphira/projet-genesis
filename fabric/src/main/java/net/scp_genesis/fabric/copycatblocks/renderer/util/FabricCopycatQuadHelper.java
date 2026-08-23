package net.scp_genesis.fabric.copycatblocks.renderer.util;

import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.fabric.copycatblocks.provider.FabricCopycatModelProvider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public final class FabricCopycatQuadHelper {

    private FabricCopycatQuadHelper() {
    }

    /**
     * <h1>TINT</h1>
     * ================================================================
     * Encodes the tint index of a Copycat part.
     */
    public static int encodeCopycatTintIndex(
            int tintIndex,
            @NotNull CopycatPart part
    ) {
        if (tintIndex < 0) {
            return tintIndex;
        }

        return switch (part) {
            case BOTTOM -> tintIndex + 1000;
            case TOP -> tintIndex + 2000;
            default -> tintIndex;
        };
    }

    /**
     * <h1>QUAD REMAPPING</h1>
     * ================================================================
     * Copies a vanilla quad into a Fabric QuadEmitter and remaps
     * its UV coordinates from the source sprite to the copied sprite.
     */
    public static void remapQuad(
            @NotNull QuadEmitter emitter,
            @NotNull BakedQuad sourceQuad,
            @NotNull TextureAtlasSprite sourceSprite,
            @NotNull BakedQuad copiedQuad,
            @NotNull CopycatPart part
    ) {
        TextureAtlasSprite targetSprite =
                copiedQuad.getSprite();

        int[] vertices =
                sourceQuad.getVertices();

        final int vertexSize = 8;

        float sourceSpriteMinU = sourceSprite.getU0();
        float sourceSpriteMaxU = sourceSprite.getU1();
        float sourceSpriteMinV = sourceSprite.getV0();
        float sourceSpriteMaxV = sourceSprite.getV1();

        float targetSpriteMinU = targetSprite.getU0();
        float targetSpriteMaxU = targetSprite.getU1();
        float targetSpriteMinV = targetSprite.getV0();
        float targetSpriteMaxV = targetSprite.getV1();

        float sourceSpriteUSize =
                sourceSpriteMaxU - sourceSpriteMinU;

        float sourceSpriteVSize =
                sourceSpriteMaxV - sourceSpriteMinV;

        float targetSpriteUSize =
                targetSpriteMaxU - targetSpriteMinU;

        float targetSpriteVSize =
                targetSpriteMaxV - targetSpriteMinV;

        if (sourceSpriteUSize == 0.0F
                || sourceSpriteVSize == 0.0F
                || targetSpriteUSize == 0.0F
                || targetSpriteVSize == 0.0F) {
            return;
        }

        emitter.fromVanilla(
                sourceQuad.getVertices(),
                0
        );

        for (int vertex = 0; vertex < 4; vertex++) {

            int offset = vertex * vertexSize;

            float u =
                    Float.intBitsToFloat(
                            vertices[offset + 4]
                    );

            float v =
                    Float.intBitsToFloat(
                            vertices[offset + 5]
                    );

            float normalizedU =
                    (u - sourceSpriteMinU)
                            / sourceSpriteUSize;

            float normalizedV =
                    (v - sourceSpriteMinV)
                            / sourceSpriteVSize;

            float newU =
                    targetSpriteMinU
                            + normalizedU
                            * targetSpriteUSize;

            float newV =
                    targetSpriteMinV
                            + normalizedV
                            * targetSpriteVSize;

            emitter.uv(
                    vertex,
                    newU,
                    newV
            );
        }

        int tintIndex =
                copiedQuad.getTintIndex();

        if (tintIndex >= 0) {
            tintIndex =
                    encodeCopycatTintIndex(
                            tintIndex,
                            part
                    );
        }

        emitter.colorIndex(tintIndex);
    }

    /**
     * <h1>RETEXTURING</h1>
     * ================================================================
     * Retextures a Copycat geometry model using the corresponding
     * copied block model.
     */
    public static void retextureModel(
            @NotNull BakedModel geometryModel,
            @NotNull BlockAndTintGetter blockView,
            @Nullable BlockState copycatState,
            @NotNull BlockState copiedState,
            @NotNull BlockPos pos,
            @NotNull Supplier<RandomSource> randomSupplier,
            @NotNull RenderContext context,
            @NotNull CopycatPart part
    ) {
        BakedModel copiedModel =
                FabricCopycatModelProvider.getModel(
                        copiedState
                );

        for (Direction direction :
                Direction.values()) {

            if (!direction.getAxis().isHorizontal()
                    && direction != Direction.UP
                    && direction != Direction.DOWN) {
                continue;
            }

            RandomSource random =
                    randomSupplier.get();

            List<BakedQuad> geometryQuads =
                    geometryModel.getQuads(
                            copycatState,
                            direction,
                            random
                    );

            if (geometryQuads.isEmpty()) {
                continue;
            }

            for (BakedQuad geometryQuad :
                    geometryQuads) {

                List<BakedQuad> copiedQuads =
                        FabricCopycatModelHelper
                                .findMatchingCopiedQuads(
                                        copiedModel,
                                        copiedState,
                                        direction,
                                        geometryQuad,
                                        random
                                );

                if (copiedQuads.isEmpty()) {

                    context.getEmitter()
                            .fromVanilla(
                                    geometryQuad.getVertices(),
                                    0
                            )
                            .emit();

                    continue;
                }

                for (BakedQuad copiedQuad :
                        copiedQuads) {

                    QuadEmitter emitter =
                            context.getEmitter();

                    remapQuad(
                            emitter,
                            geometryQuad,
                            geometryQuad.getSprite(),
                            copiedQuad,
                            part
                    );

                    emitter.emit();
                }
            }
        }
    }
}