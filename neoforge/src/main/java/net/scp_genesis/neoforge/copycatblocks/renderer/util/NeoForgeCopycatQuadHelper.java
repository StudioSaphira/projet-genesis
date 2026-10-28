package net.scp_genesis.neoforge.copycatblocks.renderer.util;

import java.util.List;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.saphyra.neoforge.client.rendering.NeoForgeQuadRetexturing;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Maps Genesis material slots to Saphyra rendering parameters. */
public final class NeoForgeCopycatQuadHelper {
    private NeoForgeCopycatQuadHelper() {}

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
        return NeoForgeQuadRetexturing.encodeTintIndex(tintIndex, tintOffset(part));
    }

    /** Delegates rendering to Saphyra with Genesis material-slot values. */
    public static @NotNull BakedQuad remapQuad(
            @NotNull BakedQuad sourceQuad,
            @NotNull TextureAtlasSprite sourceSprite,
            @NotNull BakedQuad copiedQuad,
            @NotNull CopycatPart part
    ) {
        return NeoForgeQuadRetexturing.remapQuad(sourceQuad, sourceSprite, copiedQuad, tintOffset(part));
    }

    /** Delegates rendering to Saphyra with Genesis material-slot values. */
    public static @NotNull List<BakedQuad> retextureModel(
            @NotNull BakedModel geometryModel,
            @Nullable BlockState copycatState,
            @NotNull BlockState copiedState,
            @Nullable Direction side,
            @NotNull RandomSource random,
            @Nullable RenderType renderType,
            @NotNull ModelData modelData,
            @NotNull CopycatPart part
    ) {
        return NeoForgeQuadRetexturing.retextureModel(geometryModel, copycatState, copiedState, side, random, renderType, modelData, tintOffset(part));
    }
}
