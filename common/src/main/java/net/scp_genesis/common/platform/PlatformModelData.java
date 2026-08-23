package net.scp_genesis.common.platform;

import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;

import java.util.EnumMap;

/**
 * Platform abstraction for Minecraft model data.
 *
 * <p>The actual model data implementation is provided by the
 * active mod loader.</p>
 *
 * @param <T> platform-specific model data type
 */
public interface PlatformModelData<T> {

    /**
     * Creates platform-specific model data from Copycat states.
     *
     * @param copiedStates copied BlockStates indexed by Copycat part
     * @return platform-specific model data
     */
    T create(
            EnumMap<CopycatPart, BlockState> copiedStates
    );
}