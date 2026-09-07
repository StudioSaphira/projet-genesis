package net.scp_genesis.neoforge.copycatblocks.renderer.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * NeoForge renderer used to draw the selection outline of Copycat
 * blocks directly from their baked model geometry.
 *
 * <p>Unlike a vanilla {@code VoxelShape} outline, this renderer
 * follows the actual geometry represented by the model's
 * {@link BakedQuad}s.</p>
 *
 * <p>This is especially useful for sloped Copycat blocks, where the
 * physical collision shape may intentionally use a simplified
 * stepped shape while the selection outline follows the rendered
 * slope.</p>
 */
public final class NeoForgeCopycatOutlineRenderer {

    private NeoForgeCopycatOutlineRenderer() {}

    /*
     * ================================================================
     * PUBLIC API
     * ================================================================
     */

    /**
     * Renders the outline of a Copycat block from its baked model.
     *
     * @param poseStack current pose stack
     * @param bufferSource render buffer source
     * @param state block state
     * @param modelData NeoForge model data
     */
    public static void render(
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource bufferSource,
            @NotNull BlockPos pos,
            @NotNull BlockState state,
            @NotNull ModelData modelData
    ) {
        Camera camera = Minecraft
                        .getInstance()
                        .gameRenderer
                        .getMainCamera();

        double cameraX =
                camera.getPosition().x;

        double cameraY =
                camera.getPosition().y;

        double cameraZ =
                camera.getPosition().z;

        Minecraft minecraft = Minecraft.getInstance();

        BakedModel model = minecraft
                        .getBlockRenderer()
                        .getBlockModelShaper()
                        .getBlockModel(state);

        List<BakedQuad> quads =
                collectQuads(
                        model,
                        state,
                        modelData
                );

        if (quads.isEmpty()) {return;}

        List<Line> lines = buildOutlineLines(quads);

        if (lines.isEmpty()) {return;}

        VertexConsumer buffer =
                bufferSource.getBuffer(
                        RenderType.lines()
                );

        poseStack.pushPose();

        poseStack.translate(
                pos.getX() - cameraX,
                pos.getY() - cameraY,
                pos.getZ() - cameraZ
        );

        drawLines(
                poseStack,
                buffer,
                lines
        );

        poseStack.popPose();
    }

    /*
     * ================================================================
     * QUAD COLLECTION
     * ================================================================
     */

    private static @NotNull List<BakedQuad> collectQuads(
            @NotNull BakedModel model,
            @NotNull BlockState state,
            @NotNull ModelData modelData
    ) {
        List<BakedQuad> result = new ArrayList<>();

        RandomSource random = RandomSource.create(42L);

        /*
         * Collect directional quads.
         */
        for (Direction direction : Direction.values()) {

            result.addAll(
                    model.getQuads(
                            state,
                            direction,
                            random,
                            modelData,
                            null));
        }

        /*
         * Collect unculled quads.
         */
        result.addAll(
                model.getQuads(
                        state,
                        null,
                        random,
                        modelData,
                        null));
        return result;
    }

    /*
     * ================================================================
     * OUTLINE EXTRACTION
     * ================================================================
     */

    private static @NotNull List<Line> buildOutlineLines(@NotNull List<BakedQuad> quads) {
        List<Line> lines = new ArrayList<>();

        for (BakedQuad quad : quads) {

            Vector[] vertices = getVertices(quad);

            if (vertices.length < 3) {continue;}

            /*
             * A quad contributes four edges.
             */
            for (int i = 0; i < vertices.length; i++) {

                Vector start = vertices[i];

                Vector end =
                        vertices[
                                (i + 1) % vertices.length
                                ];

                if (start.equals(end)) {continue;}

                lines.add(
                        new Line(
                                start,
                                end,
                                quad.getDirection()
                        )
                );
            }
        }

        return lines;
    }

    /*
     * ================================================================
     * VERTEX EXTRACTION
     * ================================================================
     */

    private static @NotNull Vector[] getVertices(@NotNull BakedQuad quad) {
        int[] data = quad.getVertices();

        Vector[] vertices = new Vector[4];

        for (int i = 0; i < 4; i++) {

            int offset = i * 8;

            float x =
                    Float.intBitsToFloat(
                            data[offset]
                    );

            float y =
                    Float.intBitsToFloat(
                            data[offset + 1]
                    );

            float z =
                    Float.intBitsToFloat(
                            data[offset + 2]
                    );

            vertices[i] =
                    new Vector(
                            x,
                            y,
                            z
                    );
        }

        return vertices;
    }

    /*
     * ================================================================
     * INTERNAL EDGE REMOVAL
     * ================================================================
     */

    private static @NotNull List<Line> removeInternalLines(@NotNull List<Line> lines) {
        List<Line> result = new ArrayList<>();

        boolean[] removed = new boolean[lines.size()];

        for (int i = 0; i < lines.size(); i++) {

            if (removed[i]) {continue;}

            Line current =
                    lines.get(i);

            for (int j = i + 1; j < lines.size(); j++) {

                if (removed[j]) {
                    continue;
                }

                Line other =
                        lines.get(j);

                if (!current.isSameSegment(other)) {
                    continue;
                }

                /*
                 * The same geometric segment belongs to two faces.
                 *
                 * If both faces have the same normal, it is usually
                 * duplicated geometry.
                 *
                 * If their normals differ, the segment is an internal
                 * shared edge and must not be rendered as an outline.
                 */
                if (!current.hasSameDirection(other)) {
                    removed[i] = true;
                    removed[j] = true;
                    break;
                }

                /*
                 * Identical geometry generated more than once.
                 *
                 * Keep only one copy.
                 */
                removed[j] = true;
            }

            if (!removed[i]) {
                result.add(current);
            }
        }

        return result;
    }

    /*
     * ================================================================
     * DRAWING
     * ================================================================
     */

    private static void drawLines(
            @NotNull PoseStack poseStack,
            @NotNull VertexConsumer buffer,
            @NotNull List<Line> lines
    ) {
        PoseStack.Pose pose = poseStack.last();

        for (Line line : lines) {

            Vector normal = line.normal();

            buffer
                    .addVertex(
                            pose,
                            line.start.x,
                            line.start.y,
                            line.start.z
                    )
                    .setColor(
                            0,
                            0,
                            0,
                            102
                    )
                    .setNormal(
                            pose,
                            normal.x,
                            normal.y,
                            normal.z
                    );

            buffer
                    .addVertex(
                            pose,
                            line.end.x,
                            line.end.y,
                            line.end.z
                    )
                    .setColor(
                            0,
                            0,
                            0,
                            102
                    )
                    .setNormal(
                            pose,
                            normal.x,
                            normal.y,
                            normal.z
                    );
        }
    }

    /*
     * ================================================================
     * DATA TYPES
     * ================================================================
     */

    private record Vector(
            float x,
            float y,
            float z
    ) {

        private boolean equals(@NotNull Vector other) {
            return Math.abs(x - other.x) < 1.0E-5F
                    && Math.abs(y - other.y) < 1.0E-5F
                    && Math.abs(z - other.z) < 1.0E-5F;
        }
    }

    private record Line(
            @NotNull Vector start,
            @NotNull Vector end,
            @NotNull Direction direction
    ) {

        private Vector normal() {
            return new Vector(
                    direction.getStepX(),
                    direction.getStepY(),
                    direction.getStepZ()
            );
        }

        private boolean isSameSegment(@NotNull Line other) {
            return (
                    start.equals(other.start) && end.equals(other.end)
            ) || (
                    start.equals(other.end) && end.equals(other.start)
            );
        }

        private boolean hasSameDirection(@NotNull Line other) {
            return direction == other.direction;
        }
    }
}