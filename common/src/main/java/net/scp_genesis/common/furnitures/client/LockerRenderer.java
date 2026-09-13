package net.scp_genesis.common.furnitures.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.scp_genesis.common.furnitures.notmodular.LockerBlock;
import net.scp_genesis.common.furnitures.storage.LockerBlockEntity;
import java.util.function.Supplier;

public final class LockerRenderer implements BlockEntityRenderer<LockerBlockEntity> {
    public static final ResourceLocation DOOR = ResourceLocation.parse("scp_genesis:block/locker_door");
    private final Supplier<BakedModel> door;

    public LockerRenderer(Supplier<BakedModel> door) { this.door = door; }

    @Override public void render(LockerBlockEntity locker, float partialTick, PoseStack pose,
                                  MultiBufferSource buffers, int light, int overlay) {
        pose.pushPose();
        pose.translate(0.5, 0, 0.5);
        pose.mulPose(Axis.YP.rotationDegrees(180 - locker.getBlockState().getValue(LockerBlock.FACING).toYRot()));
        pose.translate(-0.5, 0, -0.5);
        pose.translate(1.0 / 16, 0, 0);
        pose.mulPose(Axis.YP.rotationDegrees(90 * locker.openness(partialTick)));
        pose.translate(-1.0 / 16, 0, 0);
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(pose.last(),
                buffers.getBuffer(Sheets.solidBlockSheet()), null, door.get(), 1, 1, 1, light, overlay);
        pose.popPose();
    }

    @Override public boolean shouldRenderOffScreen(LockerBlockEntity locker) { return true; }
}
