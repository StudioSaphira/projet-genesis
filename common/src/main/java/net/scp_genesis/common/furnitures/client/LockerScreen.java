package net.scp_genesis.common.furnitures.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.scp_genesis.common.furnitures.storage.LockerMenu;

public final class LockerScreen extends AbstractContainerScreen<LockerMenu> {
    public LockerScreen(LockerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 176;
        imageHeight = 168;
        inventoryLabelY = 74;
    }

    @Override protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos, y = topPos;
        graphics.fill(x, y, x + imageWidth, y + imageHeight, 0xFF373737);
        graphics.fill(x + 1, y + 1, x + imageWidth - 1, y + imageHeight - 1, 0xFFFFFFFF);
        graphics.fill(x + 3, y + 3, x + imageWidth - 3, y + imageHeight - 3, 0xFFC6C6C6);
        for (var slot : menu.slots) {
            int sx = x + slot.x - 1, sy = y + slot.y - 1;
            graphics.fill(sx, sy, sx + 18, sy + 18, 0xFFFFFFFF);
            graphics.fill(sx, sy, sx + 17, sy + 17, 0xFF373737);
            graphics.fill(sx + 1, sy + 1, sx + 17, sy + 17, 0xFF8B8B8B);
        }
    }

    @Override public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
