package net.scp_genesis.common.furnitures.storage;

import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.registry.ModBlockEntities;

/** One displayed food portion, saved with its components and synchronized to watching clients. */
public final class PlateBlockEntity extends BlockEntity {
    private ItemStack food=ItemStack.EMPTY;
    public PlateBlockEntity(BlockPos pos,BlockState state){super(ModBlockEntities.PLATE.get(),pos,state);}
    /** Vanilla FOOD components also allow compatible foods supplied by other mods. */
    public static boolean accepts(ItemStack stack){return !stack.isEmpty()&&stack.has(DataComponents.FOOD);}
    public boolean isEmpty(){return food.isEmpty();}
    /** Returns a defensive copy so rendering and external callers cannot mutate the stored serving. */
    public ItemStack food(){return food.copy();}
    /** Stores exactly one item; the caller owns consumption of the held stack. */
    public boolean insert(ItemStack stack){
        if(!isEmpty()||!accepts(stack))return false;
        food=stack.copyWithCount(1);changed();return true;
    }
    /** Clears before returning the food, preventing duplicate drops or reentrant consumption. */
    public ItemStack take(){ItemStack result=food;food=ItemStack.EMPTY;changed();return result;}
    /** Uses the item's normal completion, preserving effects, statistics, advancements and bowls. */
    public boolean eat(Player player){
        if(level==null||level.isClientSide||isEmpty())return false;
        var properties=food.get(DataComponents.FOOD);
        if(properties==null||!player.canEat(properties.canAlwaysEat()))return false;
        ItemStack serving=take();ItemStack original=serving.copy();
        ItemStack remainder=serving.finishUsingItem(level,player);
        // Creative food completion returns the unchanged serving; it must not be duplicated into inventory.
        if(!remainder.isEmpty()&&!(player.hasInfiniteMaterials()&&ItemStack.isSameItemSameComponents(original,remainder))){
            if(!player.addItem(remainder))player.drop(remainder,false);
        }
        return true;
    }
    private void changed(){
        setChanged();
        if(level!=null&&!level.isClientSide)level.sendBlockUpdated(worldPosition,getBlockState(),getBlockState(),Block.UPDATE_CLIENTS);
    }
    @Override protected void saveAdditional(CompoundTag tag,HolderLookup.Provider registries){
        super.saveAdditional(tag,registries);
        if(!food.isEmpty())tag.put("Food",food.save(registries));
    }
    @Override protected void loadAdditional(CompoundTag tag,HolderLookup.Provider registries){
        super.loadAdditional(tag,registries);
        ItemStack loaded=ItemStack.parseOptional(registries,tag.getCompound("Food"));
        food=accepts(loaded)?loaded.copyWithCount(1):ItemStack.EMPTY;
    }
    @Override public CompoundTag getUpdateTag(HolderLookup.Provider registries){
        CompoundTag tag=new CompoundTag();saveAdditional(tag,registries);return tag;
    }
    @Override public ClientboundBlockEntityDataPacket getUpdatePacket(){return ClientboundBlockEntityDataPacket.create(this);}
}
