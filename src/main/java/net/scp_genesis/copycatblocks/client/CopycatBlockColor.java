package net.scp_genesis.copycatblocks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.scp_genesis.copycatblocks.api.CopycatBlocksAPI;
import net.scp_genesis.copycatblocks.data.CopycatPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CopycatBlockColor implements BlockColor {

    @Override
    public int getColor(
            @NotNull BlockState state,
            @Nullable BlockAndTintGetter level,
            @Nullable BlockPos pos,
            int tintIndex
    ) {

        if (level == null || pos == null) {
            return -1;
        }

        CopycatPart part = getPart(state, tintIndex);

        BlockState copiedState =
                CopycatBlocksAPI.getCopiedState(
                        level,
                        pos,
                        part
                );

        if (copiedState == null || copiedState.isAir()) {
            return -1;
        }

        return Minecraft.getInstance()
                .getBlockColors()
                .getColor(
                        copiedState,
                        level,
                        pos,
                        tintIndex
                );
    }

    private static CopycatPart getPart(
            @NotNull BlockState state,
            int tintIndex
    ) {
        /*
         * Simple Copycat Blocks such as the Cube.
         */
        if (!state.hasProperty(BlockStateProperties.SLAB_TYPE)) {
            return CopycatPart.MAIN;
        }

        SlabType slabType =
                state.getValue(BlockStateProperties.SLAB_TYPE);

        return switch (slabType) {
            case TOP -> CopycatPart.TOP;
            case BOTTOM -> CopycatPart.BOTTOM;

            /*
             * DOUBLE is handled separately below.
             *
             * For now, use the tint index to distinguish
             * the two halves.
             */
            case DOUBLE ->
                    tintIndex >= 1000
                            ? CopycatPart.TOP
                            : CopycatPart.BOTTOM;
        };
    }
}