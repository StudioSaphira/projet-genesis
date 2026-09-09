package net.scp_genesis.fabric.copycatblocks.client;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

import net.minecraft.world.level.block.state.BlockState;

import net.scp_genesis.common.copycatblocks.block.custom.CopycatSlopeBlock;
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatOutlineRenderer;

public final class FabricCopycatOutlineHandler {

    private FabricCopycatOutlineHandler() {
    }

    public static void register() {
        WorldRenderEvents.BLOCK_OUTLINE.register(
                FabricCopycatOutlineHandler::onRenderOutline
        );
    }

    private static boolean onRenderOutline(
            WorldRenderContext context,
            WorldRenderContext.BlockOutlineContext outline
    ) {
        BlockState state = outline.blockState();

        if (!(state.getBlock() instanceof CopycatSlopeBlock)) {
            return true;
        }

        FabricCopycatOutlineRenderer.render(
                context,
                outline
        );

        // Prevents the vanilla cubic outline.
        return false;
    }
}