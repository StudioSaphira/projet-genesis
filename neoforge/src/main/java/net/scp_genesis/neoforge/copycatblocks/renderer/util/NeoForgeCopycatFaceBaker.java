package net.scp_genesis.neoforge.copycatblocks.renderer.util;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.saphyra.common.api.geometry.GeometryFace;
import net.saphyra.common.api.geometry.GeometryUV;
import net.saphyra.common.api.rendering.*;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.*;
import org.jetbrains.annotations.Nullable;

/** Supplies Genesis material-slot values to the shared rendering platform. */
public final class NeoForgeCopycatFaceBaker {
    private NeoForgeCopycatFaceBaker() {}

    public static BakedQuad bake(GeometryFace face, GeometryUV[] uv, TextureAtlasSprite sprite,
                                 @Nullable BakedQuad source, CopycatPart part) {
        BakedQuad[] result = new BakedQuad[1];
        int tint = source == null ? -1 : NeoForgeCopycatQuadHelper.encodeCopycatTintIndex(source.getTintIndex(), part);
        SaphyraRendering.renderFace(new FaceRenderData(face, uv, sprite, source, tint),
                new BakedQuadTarget(quad -> result[0] = quad));
        return result[0];
    }
}
