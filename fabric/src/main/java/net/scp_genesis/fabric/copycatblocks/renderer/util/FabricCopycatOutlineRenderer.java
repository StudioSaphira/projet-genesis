package net.scp_genesis.fabric.copycatblocks.renderer.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;

import net.scp_genesis.common.copycatblocks.block.custom.CopycatSlopeBlock;
import net.scp_genesis.common.copycatblocks.geometry.CopycatFace;
import net.scp_genesis.common.copycatblocks.geometry.slope.CopycatGeometrySlope;
import net.scp_genesis.common.copycatblocks.geometry.CopycatVertex;

import java.util.ArrayList;
import java.util.List;

public final class FabricCopycatOutlineRenderer {

    private static final float EPSILON = 1.0E-5F;

    private FabricCopycatOutlineRenderer() {}

    public static void render(
            WorldRenderContext context,
            WorldRenderContext.BlockOutlineContext outline
    ) {
        BlockPos pos = outline.blockPos();
        BlockState state = outline.blockState();

        if (!(state.getBlock() instanceof CopycatSlopeBlock)) {
            return;
        }

        Direction facing = state.getValue(
                BlockStateProperties.HORIZONTAL_FACING
        );

        Half half = state.getValue(
                BlockStateProperties.HALF
        );

        CopycatFace[] faces = CopycatGeometrySlope.getFaces(
                facing,
                half
        );

        List<Line> lines = buildOutlineLines(faces);

        if (lines.isEmpty()) {
            return;
        }

        /*
         * Kept for future Copycat geometries where internal
         * edges may need to be removed.
         *
         * Not used for the Slope.
         */
        // lines = removeInternalLines(lines);

        @SuppressWarnings("deprecation") VertexConsumer buffer = outline.vertexConsumer();

        if (buffer == null) {return;}

        PoseStack poseStack = context.matrixStack();

        if (poseStack == null) {return;}

        PoseStack.Pose pose = poseStack.last();

        double cameraX = outline.cameraX();
        double cameraY = outline.cameraY();
        double cameraZ = outline.cameraZ();

        float blockX = (float) (pos.getX() - cameraX);
        float blockY = (float) (pos.getY() - cameraY);
        float blockZ = (float) (pos.getZ() - cameraZ);

        drawLines(
                pose,
                buffer,
                lines,
                blockX,
                blockY,
                blockZ
        );
    }

    private static List<Line> buildOutlineLines(CopycatFace[] faces) {
        List<Line> lines = new ArrayList<>();

        for (CopycatFace face : faces) {

            CopycatVertex[] vertices = face.vertices();

            if (vertices.length < 3) {continue;}

            for (int i = 0; i < vertices.length; i++) {

                CopycatVertex start = vertices[i];

                CopycatVertex end = vertices[
                        (i + 1) % vertices.length
                        ];

                Vector startVector = new Vector(
                        start.x(),
                        start.y(),
                        start.z()
                );

                Vector endVector = new Vector(
                        end.x(),
                        end.y(),
                        end.z()
                );

                if (startVector.equals(endVector)) {continue;}

                lines.add(
                        new Line(
                                startVector,
                                endVector,
                                face.direction()
                        )
                );
            }
        }

        return lines;
    }

    private static List<Line> removeInternalLines(
            List<Line> lines
    ) {
        List<Line> result = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {

            Line current = lines.get(i);

            boolean internal = false;

            for (int j = 0; j < lines.size(); j++) {

                if (i == j) {continue;}

                Line other = lines.get(j);

                if (!current.isSameSegment(other)) {continue;}

                if (!current.hasSameDirection(other)) {
                    internal = true;
                    break;
                }
            }

            if (!internal) {result.add(current);}
        }

        return result;
    }

    private static void drawLines(
            PoseStack.Pose pose,
            VertexConsumer buffer,
            List<Line> lines,
            float blockX,
            float blockY,
            float blockZ
    ) {
        for (Line line : lines) {

            Vector start = line.start();
            Vector end = line.end();

            Vector normal = line.normal();

            buffer.addVertex(
                            pose,
                            blockX + start.x(),
                            blockY + start.y(),
                            blockZ + start.z()
                    )
                    .setColor(0, 0, 0, 102)
                    .setNormal(
                            pose,
                            normal.x(),
                            normal.y(),
                            normal.z()
                    );

            buffer.addVertex(
                            pose,
                            blockX + end.x(),
                            blockY + end.y(),
                            blockZ + end.z()
                    )
                    .setColor(0, 0, 0, 102)
                    .setNormal(
                            pose,
                            normal.x(),
                            normal.y(),
                            normal.z()
                    );
        }
    }

    private record Vector(
            float x,
            float y,
            float z
    ) {

        private boolean equals(Vector other) {
            return Math.abs(x - other.x) < EPSILON
                    && Math.abs(y - other.y) < EPSILON
                    && Math.abs(z - other.z) < EPSILON;
        }
    }

    private record Line(
            Vector start,
            Vector end,
            Direction direction
    ) {

        private Vector normal() {
            return new Vector(
                    direction.getStepX(),
                    direction.getStepY(),
                    direction.getStepZ()
            );
        }

        private boolean isSameSegment(Line other) {
            return (
                    start.equals(other.start)
                            && end.equals(other.end)
            ) || (
                    start.equals(other.end)
                            && end.equals(other.start)
            );
        }

        private boolean hasSameDirection(Line other) {
            return direction == other.direction;
        }
    }
}