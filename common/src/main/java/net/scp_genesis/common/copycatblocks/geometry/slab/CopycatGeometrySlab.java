package net.scp_genesis.common.copycatblocks.geometry.slab;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;

import java.util.List;

/**
 * Provides the common Minecraft logic used by Copycat Slab geometries.
 *
 * <p>This class contains no Fabric or NeoForge specific code.</p>
 */
public final class CopycatGeometrySlab {

    private CopycatGeometrySlab() {}

    /**
     * Returns the Minecraft SlabType of the given block state.
     *
     * @param state the slab block state
     * @return the slab type
     */
    public static SlabType getSlabType(BlockState state) {
        return state.getValue(BlockStateProperties.SLAB_TYPE);
    }

    /**
     * Returns the Copycat parts used by the given slab type.
     *
     * @param slabType the slab type
     * @return the logical Copycat parts used by this slab
     */
    public static List<CopycatPart> getParts(SlabType slabType) {
        return switch (slabType) {
            case BOTTOM -> List.of(CopycatPart.BOTTOM);
            case TOP -> List.of(CopycatPart.TOP);
            case DOUBLE -> List.of(CopycatPart.BOTTOM, CopycatPart.TOP);
        };
    }

    public static List<CopycatPart> getParts(BlockState state) {
        return getParts(getSlabType(state));
    }
}