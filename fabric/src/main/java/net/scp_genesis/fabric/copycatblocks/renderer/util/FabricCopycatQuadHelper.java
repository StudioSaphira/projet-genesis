package net.scp_genesis.fabric.copycatblocks.renderer.util;

import java.util.function.Supplier;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.saphyra.fabric.client.rendering.FabricQuadRetexturing;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import org.jetbrains.annotations.NotNull;

/** Maps Genesis material slots to Saphyra rendering parameters. */
public final class FabricCopycatQuadHelper {
    private FabricCopycatQuadHelper() {}

    private static int tintOffset(CopycatPart part) {
        return switch (part) {
            case BOTTOM -> 1000;
            case TOP -> 2000;
            default -> 0;
        };
    }

    /** Delegates rendering to Saphyra with Genesis material-slot values. */
    public static int encodeCopycatTintIndex(
            int tintIndex,
            @NotNull CopycatPart part
    ) {
        return FabricQuadRetexturing.encodeTintIndex(tintIndex, tintOffset(part));
    }

    /** Delegates rendering to Saphyra with Genesis material-slot values. */
    public static void emitQuad(
            @NotNull QuadEmitter emitter,
            @NotNull BakedQuad quad,
            @NotNull RenderMaterial material
    ) {
        FabricQuadRetexturing.emitQuad(emitter, quad, material);
    }

    /** Delegates rendering to Saphyra with Genesis material-slot values. */
    public static void emitRetexturedQuad(
            @NotNull QuadEmitter emitter,
            @NotNull BakedQuad geometryQuad,
            @NotNull BakedQuad copiedQuad,
            @NotNull CopycatPart part
    ) {
        FabricQuadRetexturing.emitRetexturedQuad(emitter, geometryQuad, copiedQuad, tintOffset(part));
    }

    /** Delegates rendering to Saphyra with Genesis material-slot values. */
    public static void emitBaseModel(
            @NotNull BakedModel model,
            @NotNull BlockState state,
            @NotNull Supplier<RandomSource> randomSupplier,
            @NotNull RenderContext context
    ) {
        FabricQuadRetexturing.emitBaseModel(model, state, randomSupplier, context);
    }

    /** Delegates rendering to Saphyra with Genesis material-slot values. */
    public static void retextureModel(
            @NotNull BakedModel geometryModel,
            @SuppressWarnings("unused") @NotNull BlockAndTintGetter blockView,
            @NotNull BlockState copycatState,
            @SuppressWarnings("unused") @NotNull BlockPos pos,
            @NotNull BlockState copiedState,
            @NotNull Supplier<RandomSource> randomSupplier,
            @NotNull RenderContext context,
            @NotNull CopycatPart part
    ) {
        FabricQuadRetexturing.retextureModel(geometryModel, blockView, copycatState, pos, copiedState, randomSupplier, context, tintOffset(part));
    }
}
