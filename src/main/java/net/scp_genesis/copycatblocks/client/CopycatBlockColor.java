package net.scp_genesis.copycatblocks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.scp_genesis.copycatblocks.api.CopycatBlocksAPI;
import net.scp_genesis.copycatblocks.data.CopycatPart;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CopycatBlockColor implements BlockColor {

    /**
     * Offset used to encode the Copycat part inside a tint index.
     *
     * <p>This is required for DOUBLE slabs because Minecraft does not
     * provide the quad's vertical position to a BlockColor.</p>
     */
    private static final int PART_TINT_OFFSET = 1000;

    /**
     * Decodes the original tint index.
     */
    private static int decodeTintIndex(int tintIndex) {
        return tintIndex % PART_TINT_OFFSET;
    }

    /**
     * Returns the Copycat part encoded in the tint index.
     */
    @Nullable
    private static CopycatPart getEncodedPart(int tintIndex) {

        int partId = tintIndex / PART_TINT_OFFSET;

        return switch (partId) {
            case 1 -> CopycatPart.BOTTOM;
            case 2 -> CopycatPart.TOP;
            default -> null;
        };
    }

    @Override
    public int getColor(
            @NotNull BlockState state,
            @Nullable BlockAndTintGetter level,
            @Nullable BlockPos pos,
            int tintIndex
    ) {

        if (level == null || pos == null) {
            return -1;
        }

        CopycatPart encodedPart =
                getEncodedPart(tintIndex);

        int originalTintIndex =
                decodeTintIndex(tintIndex);

        /*
         * ------------------------------------------------------------
         * Explicitly encoded part
         * ------------------------------------------------------------
         *
         * Used by Copycat Slab DOUBLE.
         */
        if (encodedPart != null) {

            BlockState copiedState =
                    CopycatBlocksAPI.getCopiedState(
                            level,
                            pos,
                            encodedPart
                    );

            if (copiedState == null || copiedState.isAir()) {
                return -1;
            }

            return Minecraft.getInstance()
                    .getBlockColors()
                    .getColor(
                            copiedState,
                            level,
                            pos,
                            originalTintIndex
                    );
        }

        /*
         * ------------------------------------------------------------
         * Copycat Cube
         * ------------------------------------------------------------
         */
        if (!state.hasProperty(BlockStateProperties.SLAB_TYPE)) {

            BlockState copiedState =
                    CopycatBlocksAPI.getCopiedState(
                            level,
                            pos,
                            CopycatPart.MAIN
                    );

            if (copiedState == null || copiedState.isAir()) {
                return -1;
            }

            return Minecraft.getInstance()
                    .getBlockColors()
                    .getColor(
                            copiedState,
                            level,
                            pos,
                            originalTintIndex
                    );
        }

        /*
         * ------------------------------------------------------------
         * Copycat Slab
         * ------------------------------------------------------------
         */

        SlabType slabType =
                state.getValue(
                        BlockStateProperties.SLAB_TYPE
                );

        /*
         * BOTTOM
         */
        if (slabType == SlabType.BOTTOM) {

            BlockState copiedState =
                    CopycatBlocksAPI.getCopiedState(
                            level,
                            pos,
                            CopycatPart.BOTTOM
                    );

            if (copiedState == null || copiedState.isAir()) {
                return -1;
            }

            return Minecraft.getInstance()
                    .getBlockColors()
                    .getColor(
                            copiedState,
                            level,
                            pos,
                            originalTintIndex
                    );
        }

        /*
         * TOP
         */
        if (slabType == SlabType.TOP) {

            BlockState copiedState =
                    CopycatBlocksAPI.getCopiedState(
                            level,
                            pos,
                            CopycatPart.TOP
                    );

            if (copiedState == null || copiedState.isAir()) {
                return -1;
            }

            return Minecraft.getInstance()
                    .getBlockColors()
                    .getColor(
                            copiedState,
                            level,
                            pos,
                            originalTintIndex
                    );
        }

        /*
         * DOUBLE without an encoded part.
         *
         * This should normally not happen because CopycatBakedModel
         * encodes the part when generating the quads.
         */
        return -1;
    }
}