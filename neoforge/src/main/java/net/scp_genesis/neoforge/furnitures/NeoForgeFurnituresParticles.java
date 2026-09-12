package net.scp_genesis.neoforge.furnitures;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.scp_genesis.common.furnitures.client.FurnituresParticles;
import net.scp_genesis.common.registry.ModBlocks;

public final class NeoForgeFurnituresParticles implements IClientBlockExtensions {
    public static void register(RegisterClientExtensionsEvent event) {
        event.registerBlock(new NeoForgeFurnituresParticles(), ModBlocks.OFFICE_CHAIR.get());
    }

    @Override
    public boolean addDestroyEffects(BlockState state, Level level, BlockPos pos, ParticleEngine engine) {
        if (!(level instanceof ClientLevel client)) return false;
        FurnituresParticles.destroy(client, pos, state, engine);
        return true;
    }
}
