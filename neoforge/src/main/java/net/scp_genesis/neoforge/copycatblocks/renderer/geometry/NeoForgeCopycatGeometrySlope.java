package net.scp_genesis.neoforge.copycatblocks.renderer.geometry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.scp_genesis.common.copycatblocks.block.custom.CopycatSlopeBlock;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.CopycatFace;
import net.scp_genesis.common.copycatblocks.geometry.CopycatUV;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatGeometrySlope;
import net.scp_genesis.common.copycatblocks.provider.CopycatModelProvider;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.NeoForgeCopycatBlockStateHelper;
import net.scp_genesis.neoforge.copycatblocks.renderer.util.dedicated.NeoForgeCopycatSlopeHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * NeoForge renderer geometry for the Copycat Slope.
 *
 * <p>This class connects the platform-independent Common Slope
 * geometry to the NeoForge rendering pipeline.</p>
 *
 * <p>The actual Slope geometry is defined by
 * {@link CopycatGeometrySlope}. This class does not recreate
 * the geometry, calculate the diagonal, or perform the
 * platform-independent transformations.</p>
 *
 * <p>The rendering pipeline is:</p>
 *
 * <pre>
 * BlockState
 *      ↓
 * ModelData
 *      ↓
 * CopycatBlockEntity
 *      ↓
 * CopycatPart.MAIN
 *      ↓
 * CopycatGeometrySlope
 *      ↓
 * CopycatFace + CopycatUV
 *      ↓
 * NeoForgeCopycatSlopeHelper
 *      ↓
 * BakedQuad
 * </pre>
 */
public final class NeoForgeCopycatGeometrySlope implements NeoForgeCopycatGeometry {

    private final TextureAtlasSprite defaultSprite;

    /**
     * Creates a NeoForge Slope geometry.
     *
     * @param defaultSprite default texture used when no copied block
     *                      state is available
     */
    public NeoForgeCopycatGeometrySlope(@NotNull TextureAtlasSprite defaultSprite) {
        this.defaultSprite = defaultSprite;
    }

    /*
     * ================================================================
     * QUADS
     * ================================================================
     */

    @Override
    public @NotNull List<BakedQuad> getQuads(
            @Nullable BlockState state,
            @Nullable Direction side,
            @NotNull RandomSource random,
            @NotNull ModelData modelData,
            @Nullable RenderType renderType
    ) {
        if (!(state != null
                && state.getBlock() instanceof CopycatSlopeBlock)) {
            return Collections.emptyList();
        }

        /*
         * ============================================================
         * BLOCK STATE
         * ============================================================
         */

        Direction facing =
                state.getValue(
                        CopycatSlopeBlock.FACING
                );

        net.minecraft.world.level.block.state.properties.Half half =
                state.getValue(
                        CopycatSlopeBlock.HALF
                );

        /*
         * ============================================================
         * COPYCAT PART
         * ============================================================
         */

        CopycatPart part =
                CopycatPart.MAIN;

        /*
         * ============================================================
         * COPIED STATE
         * ============================================================
         */

        BlockState copiedState =
                NeoForgeCopycatBlockStateHelper.getCopiedState(
                        modelData,
                        part
                );

        boolean hasCopiedState =
                copiedState != null
                        && !copiedState.isAir();

        /*
         * ============================================================
         * COMMON GEOMETRY
         * ============================================================
         */

        CopycatFace[] faces =
                CopycatGeometrySlope.getFaces(
                        facing,
                        half
                );

        CopycatUV[][] uv =
                CopycatGeometrySlope.getUV(
                        facing,
                        half
                );

        /*
         * ============================================================
         * NEOFORGE QUADS
         * ============================================================
         */

        List<BakedQuad> result =
                new java.util.ArrayList<>();

        for (int i = 0; i < faces.length; i++) {

            CopycatFace face =
                    faces[i];

            /*
             * Minecraft asks for one particular face when side != null.
             */
            if (side != null
                    && face.direction() != side) {
                continue;
            }

            if (hasCopiedState) {

                result.addAll(
                        NeoForgeCopycatSlopeHelper.buildQuads(
                                face,
                                uv[i],
                                copiedState,
                                part,
                                random,
                                renderType
                        )
                );

            } else {

                result.addAll(
                        NeoForgeCopycatSlopeHelper.buildDefaultQuads(
                                face,
                                uv[i],
                                defaultSprite
                        )
                );
            }
        }

        return result;
    }

    /*
     * ================================================================
     * RENDER TYPES
     * ================================================================
     */

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(
            @Nullable BlockState state,
            @NotNull RandomSource random,
            @NotNull ModelData modelData
    ) {
        return ChunkRenderTypeSet.of(
                RenderType.cutout()
        );
    }

    /*
     * ================================================================
     * PARTICLE
     * ================================================================
     */

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(
            @NotNull ModelData modelData
    ) {
        BlockState copiedState =
                NeoForgeCopycatBlockStateHelper.getCopiedState(
                        modelData,
                        CopycatPart.MAIN
                );

        if (copiedState == null || copiedState.isAir()) {
            return defaultSprite;
        }

        return CopycatModelProvider
                .getModel(copiedState)
                .getParticleIcon(modelData);
    }
}