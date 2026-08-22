package net.scp_genesis.common.copycatblocks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.scp_genesis.common.copycatblocks.api.CopycatBlocksAPI;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
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
                        getOriginalTintIndex(tintIndex)
                );
    }

    private static CopycatPart getPart(
            @NotNull BlockState state,
            int tintIndex
    ) {

        /*
         * ============================================================
         * SLAB
         * ============================================================
         */

        if (state.hasProperty(BlockStateProperties.SLAB_TYPE)) {

            SlabType slabType =
                    state.getValue(
                            BlockStateProperties.SLAB_TYPE
                    );

            return switch (slabType) {

                case BOTTOM ->
                        CopycatPart.BOTTOM;

                case TOP ->
                        CopycatPart.TOP;

                case DOUBLE -> {

                    /*
                     * Encoded tint indices allow DOUBLE to distinguish
                     * the two logical Copycat parts.
                     */
                    if (tintIndex >= 2000) {
                        yield CopycatPart.TOP;
                    }

                    if (tintIndex >= 1000) {
                        yield CopycatPart.BOTTOM;
                    }

                    /*
                     * Fallback.
                     */
                    yield CopycatPart.BOTTOM;
                }
            };
        }

        /*
         * ============================================================
         * SIMPLE COPYCAT
         * ============================================================
         */

        return CopycatPart.MAIN;
    }

    private static int getOriginalTintIndex(int tintIndex) {

        if (tintIndex >= 2000) {
            return tintIndex - 2000;
        }

        if (tintIndex >= 1000) {
            return tintIndex - 1000;
        }

        return tintIndex;
    }
}