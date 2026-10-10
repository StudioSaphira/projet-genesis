package net.scp_genesis.common.scps.readers;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.scp_genesis.common.scps.doors.ScpSlidingDoorBlock;

/** Normalizes door links to their lower block without loading remote chunks. */

public final class ReaderDoorController {

    private ReaderDoorController() {}

    @SuppressWarnings("deprecation")
    public static BlockPos resolve(ServerLevel level, BlockPos pos) {
        if (!level.isInWorldBounds(pos) || !level.hasChunkAt(pos)) return null;
        var state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof ScpSlidingDoorBlock)) return null;
        return state.getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER ? pos.below() : pos;
    }

    public static void refresh(ServerLevel level, ReaderConfiguration config) {
        for (int slot = 0; slot < 2; slot++) {
            var pos = config.door(slot);
            if (pos != null) refresh(level, pos);
        }
    }

    public static void refresh(ServerLevel level, BlockPos pos) {
        if (resolve(level, pos) == null) return;
        var state = level.getBlockState(pos);
        boolean open = ReaderDirectory.get(level).wantsOpen(pos, level.getGameTime());
        ((DoorBlock) state.getBlock()).setOpen(null, level, state, pos, open);
    }
}
