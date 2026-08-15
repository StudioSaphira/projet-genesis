package net.scp_genesis.copycatblocks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
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

        CopycatPart part = getCopycatPart(state);

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

    private static CopycatPart getCopycatPart(
            @NotNull BlockState state
    ) {
        if (!state.hasProperty(BlockStateProperties.SLAB_TYPE)) {
            return CopycatPart.MAIN;
        }

        return switch (
                state.getValue(BlockStateProperties.SLAB_TYPE)
                ) {
            case BOTTOM -> CopycatPart.BOTTOM;
            case TOP -> CopycatPart.TOP;

            /*
             * DOUBLE will need additional handling later
             * because both BOTTOM and TOP can have
             * independent copied states.
             */
            case DOUBLE -> CopycatPart.BOTTOM;
        };
    }
}