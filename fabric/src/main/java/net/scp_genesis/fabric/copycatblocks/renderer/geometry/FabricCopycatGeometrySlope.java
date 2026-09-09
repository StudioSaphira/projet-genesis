package net.scp_genesis.fabric.copycatblocks.renderer.geometry;

import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.scp_genesis.common.copycatblocks.blockentity.CopycatBlockEntity;
import net.scp_genesis.common.copycatblocks.data.CopycatPart;
import net.scp_genesis.common.copycatblocks.geometry.CopycatFace;
import net.scp_genesis.common.copycatblocks.geometry.CopycatUV;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatGeometrySlope;
import net.scp_genesis.fabric.copycatblocks.renderer.util.dedicated.FabricCopycatSlopeHelper;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Supplier;

public final class FabricCopycatGeometrySlope implements FabricCopycatGeometry {

    private final BakedModel referenceModel;
    private final TextureAtlasSprite defaultSprite;

    public FabricCopycatGeometrySlope(
            @NotNull BakedModel referenceModel,
            @NotNull TextureAtlasSprite defaultSprite
    ) {
        this.referenceModel = referenceModel;
        this.defaultSprite = defaultSprite;
    }

    public @NotNull ItemTransforms getTransforms() {
        ItemTransforms referenceTransforms =
                referenceModel.getTransforms();

        ItemTransform gui =
                referenceTransforms.gui;

        ItemTransform rotatedGui =
                new ItemTransform(
                        new Vector3f(
                                gui.rotation.x(),
                                gui.rotation.y() + 180.0F,
                                gui.rotation.z()
                        ),
                        new Vector3f(gui.translation),
                        new Vector3f(gui.scale)
                );

        return new ItemTransforms(
                referenceTransforms.thirdPersonLeftHand,
                referenceTransforms.thirdPersonRightHand,
                referenceTransforms.firstPersonLeftHand,
                referenceTransforms.firstPersonRightHand,
                referenceTransforms.head,
                rotatedGui,
                referenceTransforms.ground,
                referenceTransforms.fixed
        );
    }

    @Override
    public void emitBlockQuads(
            BlockAndTintGetter blockView,
            BlockState state,
            BlockPos pos,
            Supplier<RandomSource> randomSupplier,
            RenderContext context
    ) {
        /*
         * ============================================================
         * SLOPE ORIENTATION
         * ============================================================
         */

        Direction facing =
                state.getValue(
                        BlockStateProperties.HORIZONTAL_FACING
                );

        Half half =
                state.getValue(
                        BlockStateProperties.HALF
                );

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
         * COPYCAT BLOCK ENTITY
         * ============================================================
         */

        BlockEntity blockEntity = blockView.getBlockEntity(pos);

        BlockState copiedState = null;

        if (blockEntity instanceof CopycatBlockEntity copycat) {
            copiedState =
                    copycat.getCopiedStates()
                            .get(CopycatPart.MAIN);
        }

        /*
         * ============================================================
         * EMPTY SLOPE
         * ============================================================
         */

        if (copiedState == null || copiedState.isAir()) {
            FabricCopycatSlopeHelper.emitEmpty(
                    faces,
                    uv,
                    defaultSprite,
                    context
            );

            return;
        }

        /*
         * ============================================================
         * RETEXTURED SLOPE
         * ============================================================
         */

        FabricCopycatSlopeHelper.retexture(
                faces,
                uv,
                copiedState,
                randomSupplier,
                context,
                CopycatPart.MAIN
        );
    }

    @Override
    public @NotNull BakedModel getModel() {return referenceModel;}

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return defaultSprite;
    }
}