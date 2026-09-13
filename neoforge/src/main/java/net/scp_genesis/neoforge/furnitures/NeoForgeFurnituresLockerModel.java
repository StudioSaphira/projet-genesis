package net.scp_genesis.neoforge.furnitures;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.util.TriState;

/** Keeps world light and directional shading, without cube AO on a two-block-tall mesh. */
public final class NeoForgeFurnituresLockerModel extends BakedModelWrapper<BakedModel> {
    public NeoForgeFurnituresLockerModel(BakedModel original) { super(original); }

    @Override public boolean useAmbientOcclusion() { return false; }

    @Override public TriState useAmbientOcclusion(BlockState state, ModelData data, RenderType renderType) {
        // The housing extends to y=2. Cube AO extrapolates its corner weights outside [0,1].
        return TriState.FALSE;
    }
}
