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
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;

/** Material copying and scraping shared by all Copycats, including subclass lifecycle callbacks. */
public final class CopycatMaterialActions {
    private CopycatMaterialActions() {}

    /** Resolves a Copycat entity without assuming that a matching entity is already loaded. */
    public static @Nullable CopycatBlockEntity entity(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof CopycatBlockEntity entity ? entity : null;
    }

    /** Copies a material, then notifies the block only after the API accepted it. */
    public static boolean copy(Level level, BlockPos pos, CopycatPart part, BlockState material,
                                Supplier<CopycatBlockEntity> lookup, Consumer<CopycatBlockEntity> afterCopy) {
        if (!CopycatBlocksAPI.copy(level, pos, part, material)) return false;
        CopycatBlockEntity entity = lookup.get();
        if (entity != null) afterCopy.accept(entity);
        return true;
    }

    /** Clears one material, refunds it outside creative mode and invokes the block's clear hook. */
    public static InteractionResult scrape(Level level, BlockPos pos, @Nullable CopycatBlockEntity entity,
            CopycatPart part, @Nullable Player player, Consumer<CopycatBlockEntity> afterClear) {
        if (entity == null || !entity.hasCopiedState(part)) return InteractionResult.PASS;
        var refund = CopycatItemHelper.getCopiedBlockItem(entity, part);
        CopycatBlocksAPI.clear(level, pos, part);
        if (player != null && !player.isCreative()) CopycatItemHelper.giveOrDrop(player, refund);
        afterClear.accept(entity);
        return InteractionResult.SUCCESS;
    }
}
