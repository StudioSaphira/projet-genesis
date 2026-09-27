package net.scp_genesis.fabric.copycatblocks.renderer.geometry;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.geometry.CopycatQuarterGeometry;
import net.scp_genesis.common.registry.ModBlocks;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.fabric.copycatblocks.renderer.util.dedicated.FabricCopycatSlopeHelper;
import java.util.function.Supplier;

/** Renders common quarter meshes with independent materials and tints for both halves. */
public final class FabricCopycatGeometryQuarter implements FabricCopycatGeometry {
    private final TextureAtlasSprite sprite;
    private final TextureAtlasSprite alternate;
    private final BakedModel reference;
    private final boolean slab;
    public FabricCopycatGeometryQuarter(BakedModel reference, TextureAtlasSprite sprite,
                                          TextureAtlasSprite alternate, boolean slab) {
        this.slab=slab;
        this.reference = reference;
        this.sprite = sprite;
        this.alternate = alternate;
    }
    @Override public void emitBlockQuads(BlockAndTintGetter world, BlockState state, BlockPos pos,
                                          Supplier<RandomSource> random, RenderContext context) {
        var entity = world.getBlockEntity(pos) instanceof CopycatBlockEntity copycat ? copycat : null;
        for (var part : CopycatQuarterGeometry.parts(state)) {
            var copied = entity == null ? null : entity.getCopiedState(part.part());
            if (copied == null || copied.isAir()) {
                FabricCopycatSlopeHelper.emitEmpty(part.faces(), part.uv(),
                        part.part() == CopycatPart.TOP ? alternate : sprite, context);
            } else {
                FabricCopycatSlopeHelper.retexture(part.faces(), part.uv(), copied, random, context, part.part());
            }
        }
    }
    /** Emits the single quarter used in inventories and in hand. */
    public void emitItem(RenderContext context) {
        for (var part : CopycatQuarterGeometry.parts(defaultState())) {
            FabricCopycatSlopeHelper.emitEmpty(part.faces(), part.uv(),
                    part.part() == CopycatPart.TOP ? alternate : sprite, context);
        }
    }
    @Override public BakedModel getModel() { return reference; }
    @Override public TextureAtlasSprite getParticleIcon() { return sprite; }
    private BlockState defaultState() {
        return (slab?ModBlocks.COPYCAT_HALF_SLAB:ModBlocks.COPYCAT_HALF_PANEL).get().defaultBlockState();
    }
}
