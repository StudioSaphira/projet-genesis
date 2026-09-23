package net.scp_genesis.common.copycatblocks.util.records;
import net.minecraft.world.item.ItemStack;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
/** Snapshot of the targeted part and its refunds, taken before the block is changed. */
public record CopycatRemovalData(CopycatPart part, ItemStack copycatItem, ItemStack copiedBlockItem) {}
