package net.scp_genesis.common.furnitures.storage;

import net.minecraft.core.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.furnitures.notmodular.LockerBlock;
import net.scp_genesis.common.registry.ModBlockEntities;

public final class LockerBlockEntity extends RandomizableContainerBlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(LockerMenu.SIZE, ItemStack.EMPTY);
    private float openness, previousOpenness;
    private final ContainerOpenersCounter openers = new ContainerOpenersCounter() {
        @Override protected void onOpen(Level level, BlockPos pos, BlockState state) { LockerBlock.setOpen(level, pos, true); }
        @Override protected void onClose(Level level, BlockPos pos, BlockState state) { LockerBlock.setOpen(level, pos, false); }
        @Override protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int oldCount, int newCount) {}
        @Override protected boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof LockerMenu menu && menu.container == LockerBlockEntity.this;
        }
    };

    public LockerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LOCKER.get(), pos, state);
        openness = previousOpenness = state.getValue(LockerBlock.OPEN) ? 1 : 0;
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, LockerBlockEntity locker) {
        locker.previousOpenness = locker.openness;
        locker.openness = state.getValue(LockerBlock.OPEN) ? Math.min(1, locker.openness + 0.1F) : Math.max(0, locker.openness - 0.1F);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, LockerBlockEntity locker) {
        if (level.getGameTime() % 20 == 0 && state.getValue(LockerBlock.OPEN)
                && ((LockerBlock) state.getBlock()).hasShelves()) {
            locker.recheckOpeners();
            if (locker.openers.getOpenerCount() == 0) LockerBlock.setOpen(level, pos, false);
        }
    }

    public float openness(float partialTick) {
        float value = previousOpenness + (openness - previousOpenness) * partialTick;
        return value * value * (3 - 2 * value);
    }

    public void recheckOpeners() { if (!isRemoved() && level != null) openers.recheckOpeners(level, worldPosition, getBlockState()); }

    @Override public void startOpen(Player player) {
        if (!isRemoved() && !player.isSpectator() && level != null && !level.isClientSide)
            openers.incrementOpeners(player, level, worldPosition, getBlockState());
    }

    @Override public void stopOpen(Player player) {
        if (!isRemoved() && !player.isSpectator() && level != null && !level.isClientSide)
            openers.decrementOpeners(player, level, worldPosition, getBlockState());
    }

    @Override public int getContainerSize() {
        return ((LockerBlock) getBlockState().getBlock()).hasShelves() ? LockerMenu.SIZE : 0;
    }
    @Override protected NonNullList<ItemStack> getItems() { return items; }
    @Override protected void setItems(NonNullList<ItemStack> items) { this.items = items; }
    @Override protected Component getDefaultName() { return Component.translatable("container.scp_genesis.locker"); }
    @Override protected AbstractContainerMenu createMenu(int id, Inventory inventory) { return new LockerMenu(id, inventory, this); }

    @Override protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        items = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!tryLoadLootTable(tag)) ContainerHelper.loadAllItems(tag, items, registries);
    }

    @Override protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!trySaveLootTable(tag)) ContainerHelper.saveAllItems(tag, items, registries);
    }
}
