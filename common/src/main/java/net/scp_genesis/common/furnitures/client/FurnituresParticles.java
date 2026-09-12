package net.scp_genesis.common.furnitures.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/** Bounded break effect, independent of the number of collision boxes. */
public final class FurnituresParticles {
    public static final int DESTROY_PARTICLE_COUNT = 24;

    private FurnituresParticles() {}

    public static void destroy(ClientLevel level, BlockPos pos, BlockState state, ParticleEngine engine) {
        var boxes = state.getShape(level, pos).toAabbs();
        if (boxes.isEmpty()) return;
        var random = level.random;
        for (int i = 0; i < DESTROY_PARTICLE_COUNT; i++) {
            var box = boxes.get(random.nextInt(boxes.size()));
            double x = box.minX + random.nextDouble() * box.getXsize();
            double y = box.minY + random.nextDouble() * box.getYsize();
            double z = box.minZ + random.nextDouble() * box.getZsize();
            engine.add(new TerrainParticle(level, pos.getX() + x, pos.getY() + y, pos.getZ() + z,
                    (random.nextDouble() - 0.5) * 0.25,
                    random.nextDouble() * 0.2,
                    (random.nextDouble() - 0.5) * 0.25, state, pos));
        }
    }
}
