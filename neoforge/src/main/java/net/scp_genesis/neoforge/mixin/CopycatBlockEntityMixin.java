package net.scp_genesis.neoforge.mixin;

import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.neoforge.platform.NeoForgePlatformModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CopycatBlockEntity.class)
public abstract class CopycatBlockEntityMixin {

    @Inject(
            method = "getModelData",
            at = @org.spongepowered.asm.mixin.injection.At("HEAD"),
            cancellable = true
    )
    private void scpGenesis$getModelData(
            CallbackInfoReturnable<ModelData> cir
    ) {
        CopycatBlockEntity blockEntity =
                (CopycatBlockEntity) (Object) this;

        cir.setReturnValue(
                new NeoForgePlatformModelData()
                        .create(
                                blockEntity.getCopiedStates()
                        )
                        .get()
        );
    }
}