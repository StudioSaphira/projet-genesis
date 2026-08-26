package net.scp_genesis.fabric.copycatblocks.renderer.util;

import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.fabric.platform.FabricPlatformModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

public final class FabricCopycatBlockStateHelper {

    private FabricCopycatBlockStateHelper() {}

    /**
     * Returns the copied BlockState associated with a Copycat part.
     */
    public static @Nullable BlockState getCopiedState(
            @NotNull FabricPlatformModelData.Data modelData,
            @NotNull CopycatPart part
    ) {
        EnumMap<CopycatPart, BlockState> copiedStates = modelData.getCopiedStates();

        if (copiedStates == null) {return null;}

        return copiedStates.get(part);
    }
}