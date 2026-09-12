package net.scp_genesis.common.furnitures.client;

import com.google.gson.*;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.RandomSource;
import org.joml.Vector3f;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** Standalone regression checks; no game window or third-party testing dependency required. */
public final class FurnituresGeometryTest {
    private static JsonObject json(String path) throws Exception {
        try (var stream = FurnituresGeometryTest.class.getResourceAsStream("/assets/scp_genesis/" + path)) {
            if (stream == null) throw new AssertionError(path);
            return JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
        }
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static Vector3f position(BakedQuad quad, int vertex) {
        int[] data = quad.getVertices();
        int offset = vertex * (data.length / 4);
        return new Vector3f(Float.intBitsToFloat(data[offset]), Float.intBitsToFloat(data[offset + 1]),
                Float.intBitsToFloat(data[offset + 2]));
    }

    private static final class TestSprite extends TextureAtlasSprite {
        TestSprite(SpriteContents contents) {
            super(TextureAtlas.LOCATION_BLOCKS, contents, 16, 16, 0, 0);
        }
    }

    public static void main(String[] args) throws Exception {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        JsonObject original = json("furnitures/office_chair.json");
        JsonObject appearance = json("models/custom/office_chair.json");
        appearance.add("textures", json("models/block/office_chair.json").get("textures"));
        FurnituresGeometry geometry = new FurnituresGeometry(original, appearance);
        geometry.template().resolveParents(id -> BlockModel.fromString("{}"));
        check(geometry.template().getElements().size() == 35, "Original elements were lost");
        JsonObject rotation = JsonParser.parseString(
                "{'origin':[8,1.5,8],'x':30,'y':72,'z':0}".replace('\'', '"')).getAsJsonObject();
        Vector3f pivot = new Vector3f(0.5F, 1.5F/16, 0.5F);
        var transform = FurnituresGeometry.elementTransform(rotation);
        check(transform.transformPosition(new Vector3f(pivot)).distance(pivot) < 1e-6, "Pivot moved");
        Vector3f point = new Vector3f(pivot).add(0, 0, 1);
        Vector3f expected = new Vector3f((float)(Math.sin(Math.toRadians(72))*Math.cos(Math.toRadians(30))),
                -0.5F, (float)(Math.cos(Math.toRadians(72))*Math.cos(Math.toRadians(30)))).add(pivot);
        check(transform.transformPosition(point).distance(expected) < 1e-6, "Incorrect Euler order");

        try (NativeImage image = new NativeImage(16, 16, false);
             SpriteContents contents = new SpriteContents(ResourceLocation.parse("scp_genesis:block/chair_office"),
                     new FrameSize(16, 16), image, ResourceMetadata.EMPTY)) {
            TextureAtlasSprite sprite = new TestSprite(contents);
            BakedModel north = geometry.bake(geometry.template()::getMaterial, material -> sprite,
                    BlockModelRotation.X0_Y0, ItemTransforms.NO_TRANSFORMS, ItemOverrides.EMPTY, true, true);
            List<BakedQuad> northQuads = north.getQuads(null, null, RandomSource.create(0));
            check(northQuads.size() == 210, "Expected 35 original cuboids / 210 faces");
            for (Direction direction : Direction.values())
                check(north.getQuads(null, direction, RandomSource.create(0)).isEmpty(), "Internal faces were culled");
            for (BlockModelRotation orientation : new BlockModelRotation[]{BlockModelRotation.X0_Y0,
                    BlockModelRotation.X0_Y90, BlockModelRotation.X0_Y180, BlockModelRotation.X0_Y270}) {
                List<BakedQuad> rotated = geometry.bake(geometry.template()::getMaterial, material -> sprite,
                        orientation, ItemTransforms.NO_TRANSFORMS, ItemOverrides.EMPTY, true, true)
                        .getQuads(null, null, RandomSource.create(0));
                check(rotated.size() == 210, "Rotation changed face count");
                for (int q = 0; q < 210; q++) {
                    BakedQuad a = northQuads.get(q), b = rotated.get(q);
                    for (int v = 0; v < 4; v++) {
                        Vector3f wanted = position(a, v).sub(0.5F, 0.5F, 0.5F);
                        orientation.getRotation().getMatrix().transformPosition(wanted);
                        wanted.add(0.5F, 0.5F, 0.5F);
                        check(wanted.distance(position(b, v)) < 1e-5, "Blockstate rotation differs");
                        int offset = v * 8;
                        check(a.getVertices()[offset+4] == b.getVertices()[offset+4]
                                && a.getVertices()[offset+5] == b.getVertices()[offset+5], "UV changed with rotation");
                    }
                    Vector3f edge1 = position(b, 1).sub(position(b, 0));
                    Vector3f edge2 = position(b, 2).sub(position(b, 0));
                    int packed = b.getVertices()[7];
                    Vector3f normal = new Vector3f((byte)packed, (byte)(packed>>8), (byte)(packed>>16)).normalize();
                    check(Math.abs(edge1.normalize().dot(normal)) < 0.015
                            && Math.abs(edge2.normalize().dot(normal)) < 0.015, "Normal not perpendicular");
                }
            }
        }
        System.out.println("PASS: original 35 elements, 210 faces, Euler rotations, fixed pivots, four blockstates, UVs and normals.");
    }
}
