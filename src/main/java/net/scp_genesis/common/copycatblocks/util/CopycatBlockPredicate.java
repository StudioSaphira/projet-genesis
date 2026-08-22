package net.scp_genesis.common.copycatblocks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.api.CopycatBlocksAPI;

public final class CopycatBlockPredicate {

    private CopycatBlockPredicate() {
    }

    public static boolean isValid(
            Level level,
            BlockPos pos,
            BlockState state
    ) {

        // Air
        if (state.isAir()) {
            return false;
        }

        // Copycat
        if (CopycatBlocksAPI.isCopycat(state)) {
            return false;
        }

        // BlockEntity
        if (state.hasBlockEntity()) {
            return false;
        }

        // Liquid
        if (!state.getFluidState().isEmpty()) {
            return false;
        }

        // Should be a model
        if (state.getRenderShape() != RenderShape.MODEL) {
            return false;
        }

        // Should be a Full Block
        if (!state.isCollisionShapeFullBlock(level, pos)) {
            return false;
        }

        return true;
    }
}