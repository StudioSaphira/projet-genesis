package net.scp_genesis.fabric.copycatblocks.renderer.geometry;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatHalfSlopeGeometry;
import net.scp_genesis.common.registry.ModBlocks;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.fabric.copycatblocks.renderer.util.dedicated.FabricCopycatSlopeHelper;
import java.util.function.Supplier;

/** Emits each half-slope component through the same face/tint pipeline as the full slope. */
public final class FabricCopycatGeometryHalfSlope extends FabricCopycatGeometrySlope {
    private final TextureAtlasSprite sprite;
    private final TextureAtlasSprite alternate;
    private final CopycatHalfSlopeForm form;
    public FabricCopycatGeometryHalfSlope(BakedModel reference, TextureAtlasSprite sprite,
                                          TextureAtlasSprite alternate, CopycatHalfSlopeForm form) {
        super(reference, sprite);
        this.sprite = sprite;
        this.alternate = alternate;
        this.form = form;
    }
    @Override public void emitBlockQuads(BlockAndTintGetter world, BlockState state, BlockPos pos,
                                          Supplier<RandomSource> random, RenderContext context) {
        var entity = world.getBlockEntity(pos) instanceof CopycatBlockEntity copycat ? copycat : null;
        for (var part : CopycatHalfSlopeGeometry.parts(state)) {
            var copied = entity == null ? null : entity.getCopiedState(part.part());
            if (copied == null || copied.isAir()) {
                FabricCopycatSlopeHelper.emitEmpty(part.faces(), part.uv(),
                        part.part() == CopycatPart.TOP ? alternate : sprite, context);
            } else {
                FabricCopycatSlopeHelper.retexture(part.faces(), part.uv(), copied, random, context, part.part());
            }
        }
    }
    /** Inventory geometry must remain half-width instead of using the full-slope item emitter. */
    public void emitItem(RenderContext context) {
        for (var part : CopycatHalfSlopeGeometry.parts(defaultState())) {
            FabricCopycatSlopeHelper.emitEmpty(part.faces(), part.uv(),
                    part.part() == CopycatPart.TOP ? alternate : sprite, context);
        }
    }
    private BlockState defaultState() {
        return switch (form) {
            case SINGLE -> ModBlocks.COPYCAT_HALF_SLOPE.get().defaultBlockState();
            case HORIZONTAL -> ModBlocks.COPYCAT_HORIZONTAL_HALF_SLOPE.get().defaultBlockState();
            case VERTICAL -> ModBlocks.COPYCAT_VERTICAL_HALF_SLOPE.get().defaultBlockState();
        };
    }
}
