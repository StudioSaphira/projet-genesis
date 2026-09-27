package net.scp_genesis.fabric.copycatblocks.renderer.geometry;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatFullSlopeGeometry;
import net.scp_genesis.common.registry.ModBlocks;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.fabric.copycatblocks.renderer.util.dedicated.FabricCopycatSlopeHelper;
import java.util.function.Supplier;

/** Emits each full-slope component through the same face/tint pipeline as the full slope. */
public final class FabricCopycatGeometryFullSlope extends FabricCopycatGeometrySlope {
    private final TextureAtlasSprite sprite;
    private final TextureAtlasSprite alternate;
    private final boolean vertical;
    public FabricCopycatGeometryFullSlope(BakedModel reference, TextureAtlasSprite sprite,
                                          TextureAtlasSprite alternate, boolean vertical) {
        super(reference, sprite);
        this.sprite = sprite;
        this.alternate = alternate;
        this.vertical = vertical;
    }
    @Override public void emitBlockQuads(BlockAndTintGetter world, BlockState state, BlockPos pos,
                                          Supplier<RandomSource> random, RenderContext context) {
        var entity = world.getBlockEntity(pos) instanceof CopycatBlockEntity copycat ? copycat : null;
        for (var part : CopycatFullSlopeGeometry.parts(state)) {
            var copied = entity == null ? null : entity.getCopiedState(part.part());
            if (copied == null || copied.isAir()) {
                FabricCopycatSlopeHelper.emitEmpty(part.faces(), part.uv(),
                        part.part() == CopycatPart.TOP ? alternate : sprite, context);
            } else {
                FabricCopycatSlopeHelper.retexture(part.faces(), part.uv(), copied, random, context, part.part());
            }
        }
    }
    /** Emits the horizontal or vertical single prism in inventories. */
    public void emitItem(RenderContext context) {
        for (var part : CopycatFullSlopeGeometry.parts(defaultState())) {
            FabricCopycatSlopeHelper.emitEmpty(part.faces(), part.uv(),
                    part.part() == CopycatPart.TOP ? alternate : sprite, context);
        }
    }
    private BlockState defaultState() {
        return (vertical ? ModBlocks.COPYCAT_VERTICAL_SLOPE : ModBlocks.COPYCAT_SLOPE).get().defaultBlockState();
    }
}
