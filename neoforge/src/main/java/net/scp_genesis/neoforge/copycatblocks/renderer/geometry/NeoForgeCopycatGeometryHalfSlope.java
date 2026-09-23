package net.scp_genesis.neoforge.copycatblocks.renderer.geometry;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.data.*;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatHalfSlopeGeometry;
import net.scp_genesis.common.registry.ModBlocks;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.copycatblocks.geometry.CopycatFaceBounds;
import net.scp_genesis.common.copycatblocks.provider.CopycatModelProvider;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatBlockStateHelper;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.dedicated.NeoForgeCopycatSlopeHelper;
import java.util.ArrayList;
import java.util.List;

/** Bakes independently tinted parts, retaining the full slope's lighting and culling rules. */
public final class NeoForgeCopycatGeometryHalfSlope extends NeoForgeCopycatGeometrySlope {
    private final TextureAtlasSprite sprite;
    private final TextureAtlasSprite alternate;
    private final CopycatHalfSlopeForm form;
    public NeoForgeCopycatGeometryHalfSlope(BakedModel reference, TextureAtlasSprite sprite,
                                            TextureAtlasSprite alternate, CopycatHalfSlopeForm form) {
        super(reference, sprite);
        this.sprite = sprite;
        this.alternate = alternate;
        this.form = form;
    }
    @Override public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource random,
                                              ModelData data, RenderType renderType) {
        if (state == null) state = defaultState();
        List<BakedQuad> result = new ArrayList<>();
        for (var part : CopycatHalfSlopeGeometry.parts(state)) {
            var copied = NeoForgeCopycatBlockStateHelper.getCopiedState(data, part.part());
            for (int i = 0; i < part.faces().length; i++) {
                var face = part.faces()[i];
                if (side != CopycatFaceBounds.boundaryDirection(face)) continue;
                if (copied == null || copied.isAir()) {
                    result.addAll(NeoForgeCopycatSlopeHelper.buildDefaultQuads(face, part.uv()[i],
                            part.part() == CopycatPart.TOP ? alternate : sprite));
                } else {
                    result.addAll(NeoForgeCopycatSlopeHelper.buildQuads(face, part.uv()[i], copied,
                            part.part(), random, renderType));
                }
            }
        }
        return result;
    }
    @Override public TextureAtlasSprite getParticleIcon(ModelData data) {
        for (CopycatPart part : new CopycatPart[]{CopycatPart.MAIN, CopycatPart.BOTTOM, CopycatPart.TOP}) {
            var copied = NeoForgeCopycatBlockStateHelper.getCopiedState(data, part);
            if (copied != null && !copied.isAir()) return CopycatModelProvider.getModel(copied).getParticleIcon(data);
        }
        return sprite;
    }
    private BlockState defaultState() {
        return switch (form) {
            case SINGLE -> ModBlocks.COPYCAT_HALF_SLOPE.get().defaultBlockState();
            case HORIZONTAL -> ModBlocks.COPYCAT_HORIZONTAL_HALF_SLOPE.get().defaultBlockState();
            case VERTICAL -> ModBlocks.COPYCAT_VERTICAL_HALF_SLOPE.get().defaultBlockState();
        };
    }
}
