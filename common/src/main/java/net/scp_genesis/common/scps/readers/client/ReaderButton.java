package net.scp_genesis.common.scps.readers.client;
import java.util.function.BooleanSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
/** Compact buttons with distinct disabled, hovered, selected and currently applied states. */
public final class ReaderButton extends Button {
    private final BooleanSupplier selected;
    private final BooleanSupplier applied;
    public ReaderButton(int x,int y,int width,Component label,OnPress pressed,BooleanSupplier selected,BooleanSupplier applied) {
        super(x,y,width,20,label,pressed,DEFAULT_NARRATION);this.selected=selected;this.applied=applied;
    }
    @Override protected void renderWidget(GuiGraphics graphics,int mouseX,int mouseY,float partialTick) {
        int fill=!active?0xFF454B51:selected.getAsBoolean()?0xFF326B90:isHoveredOrFocused()?0xFF657584:0xFF525D67;
        int border=!active?0xFF586069:applied.getAsBoolean()?0xFF7ACDA8:selected.getAsBoolean()?0xFF83CFFF:0xFF939FAA;
        graphics.fill(getX(),getY(),getX()+width,getY()+height,border);
        graphics.fill(getX()+1,getY()+1,getX()+width-1,getY()+height-1,fill);
        renderScrollingString(graphics,Minecraft.getInstance().font,2,active?0xFFFFFFFF:0xFF92999F);
    }
}
