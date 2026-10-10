package net.scp_genesis.common.scps.readers;

import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

/** Selects a reader, or captures a door for a previously selected configuration slot. */
public final class ReaderConfiguratorItem extends Item {

    private static final String LINK = "scp_reader_link";

    public ReaderConfiguratorItem() { super(new Properties().stacksTo(1)); }

    public static boolean isHeld(Player player) { return held(player)!=null; }

    private static ItemStack held(Player player) {
        for (var hand:InteractionHand.values()) if (player.getItemInHand(hand).getItem() instanceof ReaderConfiguratorItem) return player.getItemInHand(hand);
        return null;
    }

    public static void startLink(ServerPlayer player,ReaderBlockEntity reader,int slot) {
        var stack=held(player); if(stack==null)return;

        var tag=new CompoundTag();tag.putLong("reader",reader.getBlockPos().asLong());tag.putInt("slot",slot);

        tag.putUUID("player",player.getUUID());tag.putString("dimension",player.level().dimension().location().toString());

        tag.putLong("expires",player.level().getGameTime()+2400);

        CustomData.update(DataComponents.CUSTOM_DATA,stack,data->data.put(LINK,tag));

        player.closeContainer();player.displayClientMessage(Component.translatable("gui.scp_genesis.reader.select_door"),true);
    }

    @SuppressWarnings("deprecation")
    @Override public @NotNull InteractionResult useOn(UseOnContext context) {
        if (!(context.getPlayer() instanceof ServerPlayer player)) return InteractionResult.SUCCESS;
        if(!player.mayBuild()||player.isSpectator())return InteractionResult.FAIL;
        var level=player.serverLevel();var stack=context.getItemInHand();
        var data=stack.getOrDefault(DataComponents.CUSTOM_DATA,CustomData.EMPTY).copyTag();
        if(data.contains(LINK)) {
            var link=data.getCompound(LINK);
            if(!link.hasUUID("player")||!link.getUUID("player").equals(player.getUUID())
                    ||!link.getString("dimension").equals(level.dimension().location().toString())
                    ||level.getGameTime()>link.getLong("expires")) {
                CustomData.update(DataComponents.CUSTOM_DATA,stack,tag->tag.remove(LINK)); return InteractionResult.FAIL;
            }
            var readerPos=BlockPos.of(link.getLong("reader"));
            if(!level.hasChunkAt(readerPos)||!(level.getBlockEntity(readerPos) instanceof ReaderBlockEntity reader)
                    ||player.distanceToSqr(readerPos.getCenter())>4096) return InteractionResult.FAIL;
            var door=ReaderDoorController.resolve(level,context.getClickedPos());
            if(door==null) {player.displayClientMessage(Component.translatable("gui.scp_genesis.reader.invalid_door"),true);return InteractionResult.FAIL;}
            if(reader.configuration().type()!=ReaderType.LOCKED) reader.configure(reader.configuration().withDoor(link.getInt("slot"),door));
            CustomData.update(DataComponents.CUSTOM_DATA,stack,tag->tag.remove(LINK));
            ReaderMenus.open(player,reader,true);return InteractionResult.CONSUME;
        }
        if(level.getBlockEntity(context.getClickedPos()) instanceof ReaderBlockEntity reader) {
            ReaderMenus.open(player,reader,true);return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override public @NotNull InteractionResultHolder<ItemStack> use(net.minecraft.world.level.Level level, Player player, InteractionHand hand) {
        if(player.isShiftKeyDown()) CustomData.update(DataComponents.CUSTOM_DATA,player.getItemInHand(hand),tag->tag.remove(LINK));
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand),level.isClientSide);
    }
}
