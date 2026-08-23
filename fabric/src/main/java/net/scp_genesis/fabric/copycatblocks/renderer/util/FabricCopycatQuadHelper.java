package net.scp_genesis.fabric.copycatblocks.renderer.util;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.fabric.copycatblocks.provider.FabricCopycatModelProvider;
import net.scp_genesis.fabric.copycatblocks.renderer.FabricCopycatRenderer;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public final class FabricCopycatQuadHelper {

    private FabricCopycatQuadHelper() {
    }

    /**
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
     * Emits a vanilla BakedQuad through the Fabric Renderer API.
     */
    public static void emitQuad(
            @NotNull QuadEmitter emitter,
            @NotNull BakedQuad quad,
            @NotNull RenderMaterial material
    ) {
        emitter.fromVanilla(
                quad,
                material,
                quad.getDirection()
        );

        emitter.emit();
    }

    /**
     * Emits a copied quad while replacing its texture with the
     * texture of the geometry quad.
     */
    public static void emitRetexturedQuad(
            @NotNull QuadEmitter emitter,
            @NotNull BakedQuad geometryQuad,
            @NotNull BakedQuad copiedQuad,
            @NotNull CopycatPart part
    ) {
        emitter.fromVanilla(
                geometryQuad,
                FabricCopycatRenderer.getStandardMaterial(),
                geometryQuad.getDirection()
        );

        emitter.spriteBake(
                copiedQuad.getSprite(),
                0
        );

        emitter.colorIndex(
                encodeCopycatTintIndex(
                        copiedQuad.getTintIndex(),
                        part
                )
        );

        emitter.emit();
    }

    /**
     * Emits a retextured Copycat model through the Fabric Renderer API.
     */
    public static void retextureModel(
            @NotNull BakedModel geometryModel,
            @NotNull BlockAndTintGetter blockView,
            @NotNull BlockState copycatState,
            @NotNull BlockPos pos,
            @NotNull BlockState copiedState,
            @NotNull Supplier<RandomSource> randomSupplier,
            @NotNull RenderContext context,
            @NotNull CopycatPart part
    ) {
        BakedModel copiedModel =
                FabricCopycatModelProvider.getModel(copiedState);

        QuadEmitter emitter =
                context.getEmitter();

        RandomSource random =
                randomSupplier.get();

        List<BakedQuad> geometryQuads =
                geometryModel.getQuads(
                        copycatState,
                        null,
                        random
                );

        if (geometryQuads.isEmpty()) {
            return;
        }

        for (BakedQuad geometryQuad : geometryQuads) {

            Direction direction =
                    geometryQuad.getDirection();

            List<BakedQuad> copiedQuads =
                    FabricCopycatModelHelper.findMatchingCopiedQuads(
                            copiedModel,
                            copiedState,
                            direction,
                            geometryQuad,
                            random
                    );

            if (copiedQuads.isEmpty()) {
                emitQuad(
                        emitter,
                        geometryQuad,
                        FabricCopycatRenderer.getStandardMaterial()
                );

                continue;
            }

            for (BakedQuad copiedQuad : copiedQuads) {
                emitRetexturedQuad(
                        emitter,
                        geometryQuad,
                        copiedQuad,
                        part
                );
            }
        }
    }
}