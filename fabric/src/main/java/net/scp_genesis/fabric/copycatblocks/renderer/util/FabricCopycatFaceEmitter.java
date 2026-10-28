package net.scp_genesis.fabric.copycatblocks.renderer.util;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.saphyra.common.api.geometry.GeometryFace;
import net.saphyra.common.api.geometry.GeometryUV;
import net.saphyra.common.api.rendering.*;
import net.saphyra.fabric.client.rendering.FabricFaceTarget;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.*;
import org.jetbrains.annotations.Nullable;

/** Supplies Genesis material-slot values to the shared rendering platform. */
public final class FabricCopycatFaceEmitter {
    private FabricCopycatFaceEmitter() {}

    public static void retexture(QuadEmitter emitter, GeometryFace face, GeometryUV[] uv,
                                 BakedQuad source, CopycatPart part, RenderMaterial material,
                                 @Nullable Direction cullFace) {
        SaphyraRendering.renderFace(new FaceRenderData(face, uv, source.getSprite(), source,
                FabricCopycatQuadHelper.encodeCopycatTintIndex(source.getTintIndex(), part)),
                new FabricFaceTarget(emitter, material, cullFace));
    }

    public static void empty(QuadEmitter emitter, GeometryFace face, GeometryUV[] uv,
                             TextureAtlasSprite sprite, RenderMaterial material,
                             @Nullable Direction nominalFace) {
        SaphyraRendering.renderFace(new FaceRenderData(face, uv, sprite, null, -1),
                new FabricFaceTarget(emitter, material, nominalFace));
    }
}
