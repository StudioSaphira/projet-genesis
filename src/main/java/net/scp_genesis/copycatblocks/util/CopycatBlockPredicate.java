package net.scp_genesis.copycatblocks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.copycatblocks.api.CopycatBlocksAPI;

public final class CopycatBlockPredicate {

    private CopycatBlockPredicate() {
    }

    public static boolean isValidForCube(
            Level level,
            BlockPos pos,
            BlockState state
    ) {

        if (state.isAir()) {
            return false;
        }

        return !CopycatBlocksAPI.isCopycat(state);
    }
}