package net.scp_genesis.copycatblocks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.copycatblocks.api.CopycatBlocksAPI;
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

        BlockState copiedState = CopycatBlocksAPI.getCopiedState(level, pos);

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
}