package net.scp_genesis.fabric.copycatblocks.renderer.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.block.custom.CopycatSlopeBlock;
import net.scp_genesis.common.copycatblocks.client.CopycatOutlineDrawing;
import net.scp_genesis.common.copycatblocks.geometry.CopycatOutlineGeometry;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatGeometrySlope;

/** Adapts Fabric outline events to the common geometry and vanilla line renderer. */
public final class FabricCopycatOutlineRenderer {
    private FabricCopycatOutlineRenderer() {}

    /** Draws the slope's exact mesh rather than its stepped collision shape. */
    public static void render(WorldRenderContext context, WorldRenderContext.BlockOutlineContext outline) {
        BlockState state = outline.blockState();
        if (!(state.getBlock() instanceof CopycatSlopeBlock)) return;
        var faces = CopycatGeometrySlope.getFaces(state.getValue(CopycatSlopeBlock.FACING),
                state.getValue(CopycatSlopeBlock.HALF));
        var lines = CopycatOutlineGeometry.lines(faces);
        @SuppressWarnings("deprecation") VertexConsumer buffer = outline.vertexConsumer();
        PoseStack stack = context.matrixStack();
        if (lines.isEmpty() || buffer == null || stack == null) return;
        BlockPos pos = outline.blockPos();
        CopycatOutlineDrawing.draw(stack.last(), buffer, lines,
                (float) (pos.getX() - outline.cameraX()), (float) (pos.getY() - outline.cameraY()),
                (float) (pos.getZ() - outline.cameraZ()));
    }
}
