package net.scp_genesis.copycatblocks.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.scp_genesis.copycatblocks.block.AbstractCopycatBlock;
import org.jetbrains.annotations.NotNull;

public class CopycatRemoverItem extends Item {

    public CopycatRemoverItem() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public @NotNull InteractionResult useOn(
            @NotNull UseOnContext context
    ) {
        BlockState state =
                context.getLevel().getBlockState(
                        context.getClickedPos()
                );

        if (!(state.getBlock() instanceof AbstractCopycatBlock copycat)) {
            return InteractionResult.PASS;
        }

        return copycat.onRemove(
                context.getLevel(),
                context.getClickedPos(),
                state,
                new BlockHitResult(
                        context.getClickLocation(),
                        context.getClickedFace(),
                        context.getClickedPos(),
                        context.isInside()
                ),
                context.getPlayer()
        );
    }
}