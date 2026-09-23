package net.scp_genesis.common.copycatblocks.util.abstracts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.api.CopycatBlocksAPI;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.util.CopycatItemHelper;
import net.scp_genesis.common.copycatblocks.util.records.CopycatRemovalData;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

/** Removes a whole Copycat or delegates removal of one component to its block-specific hook. */
public final class CopycatRemovalActions {
    private CopycatRemovalActions() {}

    /** Captures refunds before mutation; multipart blocks retain ownership of their remaining shape. */
    public static InteractionResult remove(Level level, BlockPos pos, BlockState state,
            @Nullable CopycatBlockEntity entity, CopycatPart part, @Nullable Player player,
            boolean multipart, Function<CopycatRemovalData, InteractionResult> removePart) {
        if (entity == null) return InteractionResult.PASS;
        var data = new CopycatRemovalData(part, CopycatItemHelper.getCopycatItem(state),
                CopycatItemHelper.getCopiedBlockItem(entity, part));
        if (multipart) return removePart.apply(data);
        CopycatBlocksAPI.clear(level, pos, part);
        level.removeBlock(pos, false);
        if (player != null && !player.isCreative()) {
            CopycatItemHelper.giveOrDrop(player, data.copycatItem());
            CopycatItemHelper.giveOrDrop(player, data.copiedBlockItem());
        }
        return InteractionResult.SUCCESS;
    }
}
