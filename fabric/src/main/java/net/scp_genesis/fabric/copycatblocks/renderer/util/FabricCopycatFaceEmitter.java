package net.scp_genesis.fabric.copycatblocks.renderer.util;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.*;
import org.jetbrains.annotations.Nullable;

/** Emits triangular or quadrilateral Copycat faces; callers supply their own culling policy. */
public final class FabricCopycatFaceEmitter {
    private FabricCopycatFaceEmitter() {}

    /** Copies material appearance, then replaces geometry and normalized UVs. */
    public static void retexture(QuadEmitter emitter, CopycatFace face, CopycatUV[] uv,
                                 BakedQuad source, CopycatPart part, RenderMaterial material,
                                 @Nullable Direction cullFace) {
        emitter.fromVanilla(source, material, cullFace);
        geometry(emitter, face, uv);
        emitter.spriteBake(source.getSprite(), MutableQuadView.BAKE_NORMALIZED);
        emitter.nominalFace(cullFace);
        emitter.colorIndex(FabricCopycatQuadHelper.encodeCopycatTintIndex(source.getTintIndex(), part));
        emitter.emit();
    }

    /** Emits an untinted default face, retaining the default mesh's unculled behavior. */
    public static void empty(QuadEmitter emitter, CopycatFace face, CopycatUV[] uv,
                             TextureAtlasSprite sprite, RenderMaterial material,
                             @Nullable Direction nominalFace) {
        geometry(emitter, face, uv);
        emitter.color(-1, -1, -1, -1);
        emitter.spriteBake(sprite, MutableQuadView.BAKE_NORMALIZED);
        emitter.material(material);
        emitter.nominalFace(nominalFace);
        emitter.colorIndex(-1);
        emitter.emit();
    }

    /** A triangle repeats its last vertex because Fabric always consumes four vertices. */
    private static void geometry(QuadEmitter emitter, CopycatFace face, CopycatUV[] uv) {
        int count = face.vertices().length;
        if ((count != 3 && count != 4) || uv.length != count) {
            throw new IllegalArgumentException("Copycat faces require 3 or 4 vertices with matching UVs");
        }
        CopycatVector normal = CopycatGeometryMath.normalize(CopycatGeometryMath.normal(face));
        for (int i = 0; i < 4; i++) {
            int index = Math.min(i, count - 1);
            CopycatVertex vertex = face.vertices()[index];
            emitter.pos(i, vertex.x(), vertex.y(), vertex.z());
            emitter.normal(i, normal.x(), normal.y(), normal.z());
            emitter.uv(i, uv[index].u(), uv[index].v());
        }
    }
}
