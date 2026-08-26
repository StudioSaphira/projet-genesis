package net.scp_genesis.fabric.platform;

import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.platform.PlatformModelData;

import java.util.EnumMap;

public final class FabricPlatformModelData implements PlatformModelData<FabricPlatformModelData.Data> {

    @Override
    public Data create(
            EnumMap<CopycatPart, BlockState> copiedStates
    ) {
        return new Data(copiedStates);
    }

    public static Data empty() {
        return new Data(
                new EnumMap<>(CopycatPart.class)
        );
    }

    public static final class Data
            implements PlatformModelData.ModelData {

        private final EnumMap<CopycatPart, BlockState> copiedStates;

        private Data(
                EnumMap<CopycatPart, BlockState> copiedStates
        ) {
            this.copiedStates = copiedStates;
        }

        public EnumMap<CopycatPart, BlockState> getCopiedStates() {
            return copiedStates;
        }
    }
}