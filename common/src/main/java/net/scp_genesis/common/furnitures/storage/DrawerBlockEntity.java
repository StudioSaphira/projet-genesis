package net.scp_genesis.common.furnitures.storage;

import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.registry.ModBlockEntities;
import org.jetbrains.annotations.NotNull;

/** Slots 0..5 are the bottom drawer; slots 6..11 are the top drawer. */
public final class DrawerBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(12, ItemStack.EMPTY);
    public DrawerBlockEntity(BlockPos pos, BlockState state) { super(ModBlockEntities.DRAWER.get(), pos, state); }
    @Override public int getContainerSize() { return 12; }
    @Override protected @NotNull NonNullList<ItemStack> getItems() { return items; }
    @Override protected void setItems(NonNullList<ItemStack> value) { items = value; }
    @Override protected @NotNull Component getDefaultName() { return Component.translatable("container.scp_genesis.drawer.lower"); }
    @Override protected @NotNull AbstractContainerMenu createMenu(int id, Inventory inventory) { return new DrawerMenu(id, inventory, compartment(false)); }
    @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items = NonNullList.withSize(12, ItemStack.EMPTY);
        if (!tryLoadLootTable(tag)) ContainerHelper.loadAllItems(tag, items, registries);
    }
    @Override protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!trySaveLootTable(tag)) ContainerHelper.saveAllItems(tag, items, registries);
    }
    public Container compartment(boolean upper) {
        int offset = upper ? 6 : 0;
        return new Container() {
            private int index(int slot) {
                if (slot < 0 || slot >= 6) throw new IndexOutOfBoundsException(slot);
                return offset + slot;
            }
            @Override public int getContainerSize() { return 6; }
            @Override public boolean isEmpty() { for(int i=0;i<6;i++) if(!getItem(i).isEmpty()) return false; return true; }
            @Override public @NotNull ItemStack getItem(int slot) { return DrawerBlockEntity.this.getItem(index(slot)); }
            @Override public @NotNull ItemStack removeItem(int slot, int count) { return DrawerBlockEntity.this.removeItem(index(slot),count); }
            @Override public @NotNull ItemStack removeItemNoUpdate(int slot) { return DrawerBlockEntity.this.removeItemNoUpdate(index(slot)); }
            @Override public void setItem(int slot, ItemStack stack) { DrawerBlockEntity.this.setItem(index(slot),stack); }
            @Override public void setChanged() { DrawerBlockEntity.this.setChanged(); }
            @Override public boolean stillValid(Player player) { return DrawerBlockEntity.this.stillValid(player); }
            @Override public void clearContent() { for(int i=0;i<6;i++) setItem(i,ItemStack.EMPTY); }
        };
    }
}
