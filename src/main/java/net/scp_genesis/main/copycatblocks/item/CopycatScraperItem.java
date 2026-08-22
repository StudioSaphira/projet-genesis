package net.scp_genesis.main.copycatblocks.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

import net.scp_genesis.main.copycatblocks.block.AbstractCopycatBlock;
import org.jetbrains.annotations.NotNull;

/**
 * Tool used to remove the copied BlockState from Copycat Blocks.
 */
public class CopycatScraperItem extends Item {

    public CopycatScraperItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {

        BlockState state = context.getLevel().getBlockState(context.getClickedPos());

        if (!(state.getBlock() instanceof AbstractCopycatBlock copycat)) {
            return InteractionResult.PASS;
        }

        return copycat.onScrape(
                context.getLevel(),
                context.getClickedPos(),
                context.getPlayer()
        );
    }
}