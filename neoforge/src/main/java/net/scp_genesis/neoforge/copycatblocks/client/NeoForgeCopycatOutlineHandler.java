package net.scp_genesis.neoforge.copycatblocks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderHighlightEvent;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.copycatblocks.block.custom.CopycatSlopeBlock;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatOutlineRenderer;
import org.jetbrains.annotations.NotNull;

/**
 * Handles custom selection outlines for Copycat blocks.
 *
 * <p>This class bridges NeoForge's block selection highlight event
 * with {@link NeoForgeCopycatOutlineRenderer}.</p>
 */
public final class NeoForgeCopycatOutlineHandler {

    private NeoForgeCopycatOutlineHandler() {}

    /**
     * Handles the rendering of a block selection outline.
     *
     * <p>Copycat blocks are rendered using their baked model geometry
     * instead of the default VoxelShape outline.</p>
     */
    @SubscribeEvent
    public static void onRenderHighlight(@NotNull RenderHighlightEvent.Block event) {
        BlockHitResult target = event.getTarget();

        BlockPos pos = target.getBlockPos();

        Minecraft minecraft = Minecraft.getInstance();

        Level level = minecraft.level;

        if (level == null) {return;}

        BlockState state = level.getBlockState(pos);

        /*
         * ============================================================
         * COPYCAT SLOPE
         * ============================================================
         */

        if (!(state.getBlock() instanceof CopycatSlopeBlock)) {return;}

        /*
         * ============================================================
         * MODEL DATA
         * ============================================================
         */

        ModelData modelData = level.getModelData(pos);

        /*
         * ============================================================
         * CUSTOM OUTLINE
         * ============================================================
         */

        NeoForgeCopycatOutlineRenderer.render(
                event.getPoseStack(),
                event.getMultiBufferSource(),
                pos,
                state,
                modelData
        );

        /*
         * ============================================================
         * CANCEL VANILLA OUTLINE
         * ============================================================
         */

        event.setCanceled(true);
    }
}