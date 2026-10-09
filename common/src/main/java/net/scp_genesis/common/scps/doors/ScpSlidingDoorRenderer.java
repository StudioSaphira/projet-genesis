package net.scp_genesis.common.scps.doors;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;

/** Renders the hollow leaves; front-right is defined from an observer facing the front. */
public final class ScpSlidingDoorRenderer implements BlockEntityRenderer<ScpSlidingDoorBlockEntity> {
    public static final ResourceLocation FRONT = ResourceLocation.parse("scp_genesis:block/scp_sliding_door_front");
    public static final ResourceLocation BACK = ResourceLocation.parse("scp_genesis:block/scp_sliding_door_back");
    private final Supplier<BakedModel> front;
    private final Supplier<BakedModel> back;

    public ScpSlidingDoorRenderer(Supplier<BakedModel> front, Supplier<BakedModel> back) {
        this.front = front;
        this.back = back;
    }

    @Override public void render(ScpSlidingDoorBlockEntity door, float partialTick, PoseStack pose,
                                 MultiBufferSource buffers, int light, int overlay) {
        pose.pushPose();
        pose.translate(0.5, 0, 0.5);
        pose.mulPose(Axis.YP.rotationDegrees(180 - door.getBlockState().getValue(ScpSlidingDoorBlock.FACING).toYRot()));
        pose.translate(-0.5, 0, -0.5);
        float offset = door.openness(partialTick);
        var facing = door.getBlockState().getValue(ScpSlidingDoorBlock.FACING);
        if (offset < 1 || !hiddenInWall(door, facing.getCounterClockWise()))
            renderLeaf(pose, buffers, front.get(), -offset, light, overlay);
        if (offset < 1 || !hiddenInWall(door, facing.getClockWise()))
            renderLeaf(pose, buffers, back.get(), offset, light, overlay);
        pose.popPose();
    }

    /** Avoids coplanar flicker when a leaf is entirely enclosed by an opaque wall. */
    private static boolean hiddenInWall(ScpSlidingDoorBlockEntity door, net.minecraft.core.Direction side) {
        var level = door.getLevel();
        if (level == null) return false;
        var pos = door.getBlockPos().relative(side);
        return level.getBlockState(pos).isSolidRender(level, pos)
                && level.getBlockState(pos.above()).isSolidRender(level, pos.above());
    }

    private static void renderLeaf(PoseStack pose, MultiBufferSource buffers, BakedModel model,
                                   float offset, int light, int overlay) {
        pose.pushPose();
        pose.translate(offset, 0, 0);
        Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(pose.last(),
                buffers.getBuffer(Sheets.solidBlockSheet()), null, model, 1, 1, 1, light, overlay);
        pose.popPose();
    }

    @Override public boolean shouldRenderOffScreen(ScpSlidingDoorBlockEntity door) { return true; }
}
