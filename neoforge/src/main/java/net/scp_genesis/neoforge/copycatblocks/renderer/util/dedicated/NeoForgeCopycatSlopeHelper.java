package net.scp_genesis.neoforge.copycatblocks.renderer.util.dedicated;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.CopycatFace;
import net.scp_genesis.common.copycatblocks.provider.CopycatModelProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class NeoForgeCopycatSlopeHelper {

    private NeoForgeCopycatSlopeHelper() {
    }

    public static List<BakedQuad> retexture(
            @NotNull CopycatFace[] faces,
            @NotNull BlockState copiedState,
            @NotNull RandomSource random,
            @NotNull CopycatPart part
    ) {
        BakedModel copiedModel =
                CopycatModelProvider.getModel(
                        copiedState
                );

        List<BakedQuad> result =
                new ArrayList<>();

        for (@NotNull CopycatFace face : faces) {

            BakedQuad copiedQuad =
                    findCopiedQuad(
                            copiedModel,
                            copiedState,
                            face.direction(),
                            random
                    );

            if (copiedQuad == null) {
                continue;
            }

            result.add(
                    createRetexturedQuad(
                            face,
                            copiedQuad,
                            part
                    )
            );
        }

        return result;
    }

    private static @Nullable BakedQuad findCopiedQuad(
            @NotNull BakedModel copiedModel,
            @NotNull BlockState copiedState,
            @NotNull Direction direction,
            @NotNull RandomSource random
    ) {
        List<BakedQuad> quads =
                copiedModel.getQuads(
                        copiedState,
                        direction,
                        random
                );

        if (!quads.isEmpty()) {
            return quads.getFirst();
        }

        List<BakedQuad> generalQuads =
                copiedModel.getQuads(
                        copiedState,
                        null,
                        random
                );

        if (!generalQuads.isEmpty()) {
            return generalQuads.getFirst();
        }

        return null;
    }

    private static BakedQuad createRetexturedQuad(
            @NotNull CopycatFace face,
            @NotNull BakedQuad copiedQuad,
            @NotNull CopycatPart part
    ) {
        /*
         * À compléter avec la construction NativeImage de BakedQuad
         * correspondant aux données du quad copié.
         */
        throw new UnsupportedOperationException(
                "NeoForge Slope quad creation not implemented yet"
        );
    }
}