package net.scp_genesis.common.furnitures.storage;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.scp_genesis.common.registry.ModMenus;

public final class LockerMenu extends AbstractContainerMenu {
    public static final int SIZE = 18;
    public final Container container;

    public LockerMenu(int id, Inventory inventory) { this(id, inventory, new SimpleContainer(SIZE)); }

    public LockerMenu(int id, Inventory inventory, Container container) {
        super(ModMenus.LOCKER.get(), id);
        checkContainerSize(container, SIZE);
        this.container = container;
        container.startOpen(inventory.player);
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 6; col++) addSlot(new Slot(container, row * 6 + col, 35 + col * 18, 18 + row * 18));
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 86 + row * 18));
        for (int col = 0; col < 9; col++) addSlot(new Slot(inventory, col, 8 + col * 18, 144));
    }

    @Override public boolean stillValid(Player player) { return container.stillValid(player); }

    @Override public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) return ItemStack.EMPTY;
        ItemStack stack = slot.getItem(), copy = stack.copy();
        if (index < SIZE) {
            if (!moveItemStackTo(stack, SIZE, slots.size(), true)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(stack, 0, SIZE, false)) return ItemStack.EMPTY;
        if (stack.isEmpty()) slot.setByPlayer(ItemStack.EMPTY); else slot.setChanged();
        return copy;
    }

    @Override public void removed(Player player) { super.removed(player); container.stopOpen(player); }
}
