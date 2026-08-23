package net.scp_genesis.common.platform;

import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;

import java.util.EnumMap;

/**
 * Platform abstraction for Copycat model data.
 *
 * <p>This interface allows the common Copycat system to provide
 * platform-specific model data without depending on a specific
 * mod loader.</p>
 */
public interface PlatformModelData {

    /**
     * Creates platform-specific model data containing the copied
     * BlockStates of a Copycat Block.
     *
     * @param copiedStates copied states indexed by Copycat part
     * @return platform-specific model data
     */
    Object create(
            EnumMap<CopycatPart, BlockState> copiedStates
    );
}