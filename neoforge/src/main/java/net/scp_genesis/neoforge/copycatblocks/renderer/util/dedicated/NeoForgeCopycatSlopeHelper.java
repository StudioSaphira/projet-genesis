package net.scp_genesis.neoforge.copycatblocks.renderer.util.dedicated;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.CopycatFace;
import net.scp_genesis.common.copycatblocks.geometry.CopycatUV;
import net.scp_genesis.common.copycatblocks.provider.CopycatModelProvider;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatBlockStateHelper;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatModelHelper;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatFaceBaker;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

/** Resolves slope materials and delegates vertex packing to the reusable custom-face baker. */
public final class NeoForgeCopycatSlopeHelper {
    private NeoForgeCopycatSlopeHelper() {}

    /** Returns the main copied material from the current NeoForge model data. */
    @Nullable
    public static BlockState getCopiedState(ModelData data) {
        return NeoForgeCopycatBlockStateHelper.getCopiedState(data, CopycatPart.MAIN);
    }

    /** Builds one face for each matching source quad, retaining render-layer selection. */
    public static List<BakedQuad> buildQuads(CopycatFace face, CopycatUV[] uv, BlockState state,
                                             CopycatPart part, RandomSource random, @Nullable RenderType renderType) {
        if (face.vertices().length < 3) return List.of();
        if (uv.length != face.vertices().length) {
            throw new IllegalArgumentException("Copycat face and UV vertex counts do not match");
        }
        var model = CopycatModelProvider.getModel(state);
        var sources = NeoForgeCopycatModelHelper.getCopiedQuads(model, state, face.direction(), random, renderType);
        List<BakedQuad> result = new ArrayList<>();
        for (BakedQuad source : sources) {
            result.add(NeoForgeCopycatFaceBaker.bake(face, uv, source.getSprite(), source, part));
        }
        return result;
    }

    /** Builds an untinted face with the default Copycat sprite. */
    public static List<BakedQuad> buildDefaultQuads(CopycatFace face, CopycatUV[] uv, TextureAtlasSprite sprite) {
        if (face.vertices().length < 3) return List.of();
        return List.of(NeoForgeCopycatFaceBaker.bake(face, uv, sprite, null, CopycatPart.MAIN));
    }

    /** Returns the copied block's particle sprite, or the missing-model fallback. */
    @SuppressWarnings("deprecation")
    public static TextureAtlasSprite getParticleSprite(ModelData data) {
        BlockState state = getCopiedState(data);
        var shaper = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper();
        return state == null || state.isAir() ? shaper.getModelManager().getMissingModel().getParticleIcon()
                : shaper.getParticleIcon(state);
    }
}
