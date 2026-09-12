package net.scp_genesis.fabric.furnitures.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.furnitures.client.FurnituresParticles;
import net.scp_genesis.common.furnitures.notmodular.office.FurnitureOfficeChair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public abstract class FabricFurnituresParticlesMixin {
    @Shadow protected ClientLevel level;

    @Inject(method = "destroy", at = @At("HEAD"), cancellable = true)
    private void genesis$officeChairDebris(BlockPos pos, BlockState state, CallbackInfo callback) {
        if (state.getBlock() instanceof FurnitureOfficeChair) {
            FurnituresParticles.destroy(level, pos, state, (ParticleEngine) (Object) this);
            callback.cancel();
        }
    }
}
