package net.scp_genesis.fabric.copycatblocks.renderer.util.dedicated;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.CopycatFace;
import net.scp_genesis.common.copycatblocks.geometry.CopycatUV;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatSlopeOcclusion;
import net.scp_genesis.common.copycatblocks.provider.CopycatModelProvider;
import net.scp_genesis.fabric.copycatblocks.renderer.FabricCopycatRenderer;
import net.scp_genesis.fabric.copycatblocks.renderer.util.FabricCopycatFaceEmitter;
import java.util.List;
import java.util.function.Supplier;

/** Applies slope-specific face culling while delegating mesh emission to reusable tools. */
public final class FabricCopycatSlopeHelper {
    private FabricCopycatSlopeHelper() {}

    /** Emits the slope using each corresponding source quad, preserving its tint and appearance. */
    public static void retexture(CopycatFace[] faces, CopycatUV[][] uv, BlockState state,
                                 Supplier<RandomSource> randomSupplier, RenderContext context, CopycatPart part) {
        BakedModel model = CopycatModelProvider.getModel(state);
        var emitter = context.getEmitter();
        RandomSource random = randomSupplier.get();
        for (int i = 0; i < faces.length; i++) {
            CopycatFace face = faces[i];
            for (BakedQuad source : findCopiedQuads(model, state, face.direction(), random)) {
                FabricCopycatFaceEmitter.retexture(emitter, face, uv[i], source, part,
                        FabricCopycatRenderer.getCutoutMaterial(), cullFace(face));
            }
        }
    }

    /** Prefers directional quads, falling back to the copied model's unculled quads. */
    public static List<BakedQuad> findCopiedQuads(BakedModel model, BlockState state,
                                                 Direction direction, RandomSource random) {
        List<BakedQuad> quads = model.getQuads(state, direction, random);
        return quads.isEmpty() ? model.getQuads(state, null, random) : quads;
    }

    /** Emits the default Copycat texture for both empty blocks and inventory items. */
    public static void emitEmpty(CopycatFace[] faces, CopycatUV[][] uv,
                                 TextureAtlasSprite sprite, RenderContext context) {
        var emitter = context.getEmitter();
        var material = FabricCopycatRenderer.getCutoutMaterial();
        for (int i = 0; i < faces.length; i++) {
            FabricCopycatFaceEmitter.empty(emitter, faces[i], uv[i], sprite, material, cullFace(faces[i]));
        }
    }

    private static Direction cullFace(CopycatFace face) {
        return CopycatSlopeOcclusion.supportsFaceCulling(face) ? face.direction() : null;
    }
}
