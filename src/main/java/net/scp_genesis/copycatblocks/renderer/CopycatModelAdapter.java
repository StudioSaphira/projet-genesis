package net.scp_genesis.copycatblocks.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Adapts a copied BlockState to Minecraft's vanilla rendering system.
 *
 * <p>This class acts as a bridge between the Copycat system and the
 * vanilla model renderer. It does not perform any custom rendering.</p>
 */
public final class CopycatModelAdapter {

    /**
     * Prevent instantiation.
     */
    private CopycatModelAdapter() {
    }

    /**
     * Returns the vanilla baked model corresponding to the given BlockState.
     *
     * @param state the copied BlockState
     * @return the vanilla BakedModel
     */
    public static BakedModel getModel(BlockState state) {

        BlockRenderDispatcher dispatcher =
                Minecraft.getInstance().getBlockRenderer();

        if (state == null || state.isAir()) {
            return dispatcher.getBlockModel(Blocks.AIR.defaultBlockState());
        }

        return dispatcher.getBlockModel(state);
    }

}