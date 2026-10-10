package net.scp_genesis.common.scps.readers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.scp_genesis.common.registry.ModMenus;
/** Server-authorized reader session; contains no transferable inventory or secret client data. */
public final class ReaderMenu extends AbstractContainerMenu {
    public final ReaderBlockEntity reader;
    public final boolean configuring;
    public CompoundTag snapshot = new CompoundTag();
    public int revision;
    public long nextRequest;
    public ReaderMenu(int id, Inventory inventory) { this(id,inventory,null,false); }
    public ReaderMenu(int id, Inventory inventory, ReaderBlockEntity reader, boolean configuring) {
        super(ModMenus.READER.get(),id); this.reader=reader; this.configuring=configuring;
    }
    @Override public boolean stillValid(Player player) {
        if (reader==null) return true;
        return !reader.isRemoved() && reader.getLevel()==player.level() && !player.isSpectator()
                && player.distanceToSqr(reader.getBlockPos().getCenter()) <= (configuring?4096:64)
                && (!configuring || (player.mayBuild() && ReaderConfiguratorItem.isHeld(player)));
    }
    @Override public ItemStack quickMoveStack(Player player,int slot) { return ItemStack.EMPTY; }
}
