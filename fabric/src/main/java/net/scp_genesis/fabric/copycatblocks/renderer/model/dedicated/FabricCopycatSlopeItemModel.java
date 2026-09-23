package net.scp_genesis.fabric.copycatblocks.renderer.model.dedicated;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatGeometrySlope;
import net.scp_genesis.fabric.copycatblocks.renderer.util.dedicated.FabricCopycatSlopeHelper;

/** Uses the canonical NORTH/BOTTOM mesh for the Copycat slope inventory item. */
public final class FabricCopycatSlopeItemModel {
    private FabricCopycatSlopeItemModel() {}

    /** Emits the item with the same face writer and default material as an empty slope. */
    public static void emit(TextureAtlasSprite sprite, RenderContext context) {
        FabricCopycatSlopeHelper.emitEmpty(CopycatGeometrySlope.getFaces(Direction.NORTH, Half.BOTTOM),
                CopycatGeometrySlope.getUV(Direction.NORTH, Half.BOTTOM), sprite, context);
    }
}
