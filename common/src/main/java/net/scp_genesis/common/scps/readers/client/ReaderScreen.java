package net.scp_genesis.common.scps.readers.client;
import java.util.*;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.scp_genesis.common.scps.readers.*;
/** Responsive configuration and numeric keypad, using the server menu as authorization. */
public final class ReaderScreen extends AbstractContainerScreen<ReaderMenu> {
    public static Consumer<ReaderPacket> sender=packet -> {};
    private ReaderConfiguration applied=ReaderConfiguration.DEFAULT;
    private ReaderType chosen=ReaderType.KEYCARD;
    private int chosenLevel,selectedSlot,seenRevision=-1,probeTicks;
    private EditBox identifier,code;
    private final EditBox[][] coordinates=new EditBox[2][3];
    private final List<ReaderButton> levelButtons=new ArrayList<>(),doorButtons=new ArrayList<>();
    private ReaderButton warning,copy,open,close,codeConfirm;
    private boolean configuring,ready,duplicate;
    private String checkedId="",message="";

    public ReaderScreen(ReaderMenu menu,Inventory inventory,Component title) {
        super(menu,inventory,title);imageWidth=320;imageHeight=238;
    }
    private static Component text(String key) {return Component.translatable("gui.scp_genesis.reader."+key);}
    @Override protected void init() {
        super.init();build();send("sync",new CompoundTag());
    }
    /** Receives snapshots only for the currently open reader menu, never an arbitrary screen. */
    public static void receive(ReaderPacket packet) {
        var player=Minecraft.getInstance().player;var tag=packet.data();
        if(player==null||tag==null||!(player.containerMenu instanceof ReaderMenu menu)||tag.getInt("menu")!=menu.containerId)return;
        if(tag.getBoolean("probe")) {
            if(Minecraft.getInstance().screen instanceof ReaderScreen screen && screen.identifier!=null
                    && screen.identifier.getValue().trim().equals(tag.getString("identifier")))screen.duplicate=tag.getBoolean("duplicate");
        } else {menu.snapshot=tag;menu.revision++;}
    }
    private EditBox field(int x,int y,int width,String label,String value,int max) {
        var field=new EditBox(font,leftPos+x,topPos+y,width,18,text(label));field.setMaxLength(max);field.setValue(value);
        addRenderableWidget(field);return field;
    }
    private ReaderButton button(int x,int y,int width,String label,Runnable action) {
        return button(x,y,width,text(label),action,()->false,()->false);
    }
    private ReaderButton button(int x,int y,int width,Component label,Runnable action,
                                java.util.function.BooleanSupplier selected,java.util.function.BooleanSupplier current) {
        var button=new ReaderButton(leftPos+x,topPos+y,width,label,b->action.run(),selected,current);addRenderableWidget(button);return button;
    }
    private void build() {
        clearWidgets();levelButtons.clear();doorButtons.clear();
        if(!ready)return;
        imageWidth=configuring?320:190;imageHeight=configuring?238:224;
        leftPos=(width-imageWidth)/2;topPos=(height-imageHeight)/2;
        if(configuring)buildConfiguration();else buildKeypad();
    }
    private void buildConfiguration() {
        identifier=field(60,25,150,"identifier",applied.identifier(),32);
        warning=button(216,24,20,Component.literal("!"),()->{},()->duplicate,()->false);
        warning.setTooltip(Tooltip.create(text("duplicate")));
        copy=button(240,24,72,"copy",()->{var tag=new CompoundTag();tag.putString("identifier",identifier.getValue().trim());send("copy",tag);});
        for(var type:ReaderType.values()) {
            var button=button(8+type.ordinal()*47,52,44,text("type."+type.getSerializedName()),()->{chosen=type;updateEnabled();},
                    ()->chosen==type,()->applied.type()==type);
            button.active=type!=ReaderType.ID;
            if(type==ReaderType.ID)button.setTooltip(Tooltip.create(text("id_unavailable")));
        }
        button(248,52,64,"confirm",()->save("save",-1));
        for(int i=0;i<=6;i++) {
            final int level=i;
            levelButtons.add(button(70+i*34,78,30,Component.literal(Integer.toString(i)),()->chosenLevel=level,
                    ()->chosenLevel==level,()->applied.type()==ReaderType.KEYCARD&&applied.level()==level));
        }
        code=field(60,105,170,"code",applied.code(),12);code.setFilter(value->value.matches("[0-9]*"));
        codeConfirm=button(248,104,64,"confirm",()->save("save",-1));
        for(int slot=0;slot<2;slot++) {
            final int selected=slot;int y=143+slot*26;
            doorButtons.add(button(8,y,44,Component.literal("Slot "+(slot+1)),()->selectedSlot=selected,()->selectedSlot==selected,()->false));
            BlockPos pos=applied.door(slot);
            for(int axis=0;axis<3;axis++) {
                int value=pos==null?0:axis==0?pos.getX():axis==1?pos.getY():pos.getZ();
                coordinates[slot][axis]=field(60+axis*57,y+1,52,"coordinate",pos==null?"":Integer.toString(value),10);
                coordinates[slot][axis].setFilter(v->v.matches("-?[0-9]*"));
            }
            doorButtons.add(button(234,y,40,"confirm_short",()->save("save",selected)));
            doorButtons.add(button(278,y,34,"set",()->save("link",selected)));
        }
        open=button(8,202,66,"open",()->send("open",new CompoundTag()));
        close=button(78,202,66,"close",()->send("close",new CompoundTag()));
        button(172,202,66,"save",()->save("save",-1));
        button(242,202,70,"done",this::onClose);
        updateEnabled();
    }
    private void buildKeypad() {
        code=field(18,29,154,"code","",12);code.setFilter(value->value.matches("[0-9]*"));
        String digits="789456123";
        for(int i=0;i<9;i++) {
            String digit=digits.substring(i,i+1);
            button(18+(i%3)*53,60+(i/3)*28,48,Component.literal(digit),()->code.setValue(code.getValue()+digit),()->false,()->false);
        }
        button(71,144,48,Component.literal("0"),()->code.setValue(code.getValue()+"0"),()->false,()->false);
        button(18,144,48,"reset",()->code.setValue(""));
        button(124,144,48,"confirm_short",this::submitCode);
        button(18,176,154,"done",this::onClose);setInitialFocus(code);
    }
    private void submitCode() {var tag=new CompoundTag();tag.putString("code",code.getValue());send("code",tag);}
    private void updateEnabled() {
        if(!configuring||identifier==null)return;
        boolean unlocked=chosen!=ReaderType.LOCKED;
        identifier.setEditable(unlocked);code.setEditable(chosen==ReaderType.CODE);
        identifier.active=unlocked;code.active=chosen==ReaderType.CODE;codeConfirm.active=chosen==ReaderType.CODE;
        for(var button:levelButtons)button.active=chosen==ReaderType.KEYCARD;
        for(var button:doorButtons)button.active=unlocked;
        for(var row:coordinates)for(var field:row){field.active=unlocked;field.setEditable(unlocked);}
        warning.active=duplicate&&unlocked;copy.active=duplicate&&unlocked;
        boolean linked=applied.first()!=null||applied.second()!=null;
        open.active=close.active=unlocked&&linked&&applied.type()!=ReaderType.LOCKED;
    }
    private BlockPos readDoor(int slot) {
        var row=coordinates[slot];
        if(Arrays.stream(row).allMatch(field->field.getValue().isBlank()))return null;
        return new BlockPos(Integer.parseInt(row[0].getValue()),Integer.parseInt(row[1].getValue()),Integer.parseInt(row[2].getValue()));
    }
    private void save(String action,int slot) {
        try {
            var draft=new ReaderConfiguration(identifier.getValue(),chosen,chosenLevel,code.getValue(),readDoor(0),readDoor(1));
            var tag=new CompoundTag();tag.put("configuration",draft.save());tag.putInt("slot",slot);send(action,tag);
        } catch(NumberFormatException invalid) {message="gui.scp_genesis.reader.invalid_coordinates";}
    }
    private void send(String action,CompoundTag tag) {tag.putInt("menu",menu.containerId);tag.putString("action",action);sender.accept(new ReaderPacket(tag));}
    @Override protected void containerTick() {
        super.containerTick();
        if(seenRevision!=menu.revision&&!menu.snapshot.isEmpty()) {
            seenRevision=menu.revision;configuring=menu.snapshot.getBoolean("configuring");ready=true;
            message=menu.snapshot.getString("message");
            if(configuring) {applied=ReaderConfiguration.load(menu.snapshot.getCompound("configuration"));chosen=applied.type();chosenLevel=Math.max(0,applied.level());}
            build();
        }
        if(configuring&&identifier!=null) {
            String id=identifier.getValue().trim();
            if(!id.equals(checkedId)) {checkedId=id;probeTicks=6;duplicate=false;}
            if(probeTicks>0&&--probeTicks==0) {var tag=new CompoundTag();tag.putString("identifier",id);send("probe",tag);}
            updateEnabled();
        }
    }
    @Override public boolean keyPressed(int key,int scan,int modifiers) {
        if(!configuring&&ready&&(key==257||key==335)){submitCode();return true;}
        return super.keyPressed(key,scan,modifiers);
    }
    @Override protected void renderBg(GuiGraphics graphics,float partialTick,int mouseX,int mouseY) {
        graphics.fill(leftPos,topPos,leftPos+imageWidth,topPos+imageHeight,0xFF1C242D);
        graphics.fill(leftPos+1,topPos+1,leftPos+imageWidth-1,topPos+imageHeight-1,0xFF303B47);
        graphics.fill(leftPos+1,topPos+1,leftPos+imageWidth-1,topPos+19,0xFF18232E);
    }
    @Override protected void renderLabels(GuiGraphics graphics,int mouseX,int mouseY) {
        graphics.drawString(font,text(!ready?"loading":configuring?"configure":"keypad"),8,6,0xFFE6EDF3,false);
        if(configuring&&ready) {
            graphics.drawString(font,text("identifier"),8,30,0xFFCBD5DF,false);
            graphics.drawString(font,text("level"),8,84,0xFFCBD5DF,false);
            graphics.drawString(font,text("code"),8,110,0xFFCBD5DF,false);
            for(int axis=0;axis<3;axis++)graphics.drawString(font,"XYZ".substring(axis,axis+1),82+axis*57,132,0xFFCBD5DF,false);
        }
        if(!message.isEmpty())graphics.drawString(font,font.plainSubstrByWidth(Component.translatable(message).getString(),imageWidth-16),8,imageHeight-12,0xFFFFCC80,false);
    }
    @Override public void render(GuiGraphics graphics,int mouseX,int mouseY,float partialTick) {
        super.render(graphics,mouseX,mouseY,partialTick);renderTooltip(graphics,mouseX,mouseY);
    }
}
