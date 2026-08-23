package net.scp_genesis.neoforge.mixin;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.neoforge.platform.NeoForgePlatformModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.EnumMap;

@SuppressWarnings("unused")
@Mixin(CopycatBlockEntity.class)
public abstract class CopycatBlockEntityMixin {

    @Shadow
    public abstract EnumMap<CopycatPart, BlockState> getCopiedStates();

    /**
     * Provides the ModelData used by NeoForge's model system.
     */
    public ModelData getModelData() {
        return new NeoForgePlatformModelData()
                .create(
                        getCopiedStates()
                )
                .get();
    }
}