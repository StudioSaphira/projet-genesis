package net.scp_genesis.fabric.copycatblocks.renderer.model;

import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("SpellCheckingInspection")
public final class FabricCopycatBakedModel
        implements FabricBakedModel {

    private final BakedModel baseModel;

    public FabricCopycatBakedModel(
            @NotNull BakedModel baseModel
    ) {
        this.baseModel = baseModel;
    }

    @Override
    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(
            net.minecraft.world.level.BlockAndTintGetter blockView,
            BlockState state,
            BlockPos pos,
            Supplier<net.minecraft.util.math.random.Random> randomSupplier,
            RenderContext context
    ) {
        if (!(blockView.getBlockEntity(pos)
                instanceof CopycatBlockEntity blockEntity)) {

            emitBaseModel(
                    state,
                    randomSupplier,
                    context
            );

            return;
        }

        BlockState copiedState =
                blockEntity.getCopiedState(
                        CopycatPart.MAIN
                );

        if (copiedState == null || copiedState.isAir()) {
            emitBaseModel(
                    state,
                    randomSupplier,
                    context
            );

            return;
        }

        /*
         * Le renderer Fabric recevra ici les données du Copycat.
         *
         * Pour l'instant nous conservons cette première version
         * volontairement minimale : le branchement du retexturing
         * viendra juste après.
         */
        emitBaseModel(
                state,
                randomSupplier,
                context
        );
    }

    private void emitBaseModel(
            BlockState state,
            Supplier<net.minecraft.util.math.random.Random> randomSupplier,
            RenderContext context
    ) {
        /*
         * TODO:
         * Transférer les BakedQuad du modèle vanilla vers le
         * QuadEmitter Fabric.
         */
    }
}