package net.scp_genesis.common.furnitures.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.scp_genesis.common.furnitures.storage.PlateBlockEntity;

/** Displays the food flat above the plate using the item's normal model, tint and components. */
public final class PlateRenderer implements BlockEntityRenderer<PlateBlockEntity> {
    private final ItemRenderer items;
    public PlateRenderer(BlockEntityRendererProvider.Context context){items=context.getItemRenderer();}
    @Override public void render(PlateBlockEntity plate,float partialTick,PoseStack pose,MultiBufferSource buffers,int light,int overlay){
        if(plate.isEmpty())return;
        pose.pushPose();
        try{
            pose.translate(0.5,0.15,0.5);
            pose.mulPose(Axis.XP.rotationDegrees(90));
            pose.scale(0.65F,0.65F,0.65F);
            items.renderStatic(plate.food(),ItemDisplayContext.FIXED,light,overlay,pose,buffers,plate.getLevel(),(int)plate.getBlockPos().asLong());
        }finally{pose.popPose();}
    }
}
