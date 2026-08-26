package net.scp_genesis.neoforge.copycatblocks.renderer.util;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.neoforge.platform.NeoForgePlatformModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

public final class NeoForgeCopycatBlockStateHelper {

    private NeoForgeCopycatBlockStateHelper() {}

    /**
     * Returns the copied BlockState associated with a Copycat part.
     */
    public static @Nullable BlockState getCopiedState(
            @NotNull ModelData modelData,
            @NotNull CopycatPart part
    ) {
        EnumMap<CopycatPart, BlockState> copiedStates =
                modelData.get(
                        NeoForgePlatformModelData.COPIED_STATES
                );

        if (copiedStates == null) {
            return null;
        }

        return copiedStates.get(part);
    }
}