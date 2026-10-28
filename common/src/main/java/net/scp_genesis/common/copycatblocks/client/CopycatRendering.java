package net.scp_genesis.common.copycatblocks.client;

import net.minecraft.resources.ResourceLocation;
import net.saphyra.common.api.rendering.OutlineProviders;
import net.scp_genesis.common.copycatblocks.block.custom.basic.CopycatSlopeBlock;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatFullSlopeGeometry;
import net.scp_genesis.common.copycatblocks.geometry.slope.half.CopycatHalfSlopeGeometry;
import net.scp_genesis.common.copycatblocks.util.abstracts.CopycatHalfSlopeBehavior;

/** Registers Genesis-owned shape selection; Saphyra owns both loaders' outline rendering. */
public final class CopycatRendering {

    private CopycatRendering() {}

    public static void register() {
        OutlineProviders.register(ResourceLocation.fromNamespaceAndPath("scp_genesis", "copycats"),
                (level, pos, state) -> {
                    if (CopycatHalfSlopeBehavior.isSlope(state)) return CopycatHalfSlopeGeometry.faces(state);
                    if (state.getBlock() instanceof CopycatSlopeBlock) return CopycatFullSlopeGeometry.faces(state);
                    return null;
                });
    }
}
