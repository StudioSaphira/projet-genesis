package net.scp_genesis.common.furnitures.client;

import com.google.gson.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Function;

/** Client-only geometry shared by both loaders; angles and UVs come from the original model. */
public final class FurnituresGeometry {
    private final BlockModel template;
    private final List<Matrix4f> elementTransforms;

    public FurnituresGeometry(JsonObject geometry, JsonObject appearance) {
        JsonObject vanilla = appearance.deepCopy();
        vanilla.remove("loader");
        vanilla.remove("geometry");
        JsonArray elements = geometry.getAsJsonArray("elements");
        if (elements == null) throw new JsonParseException("Furniture geometry requires elements");
        JsonArray unrotated = new JsonArray();
        elementTransforms = new ArrayList<>();
        for (JsonElement element : elements) {
            JsonObject copy = element.getAsJsonObject().deepCopy();
            elementTransforms.add(elementTransform(copy.getAsJsonObject("rotation")));
            copy.remove("rotation");
            unrotated.add(copy);
        }
        vanilla.add("elements", unrotated);
        template = BlockModel.fromString(vanilla.toString());
    }

    public BlockModel template() {
        return template;
    }

    /** Blockbench ZYX Euler convention: apply X, then Y, then Z about the element origin. */
    public static Matrix4f elementTransform(JsonObject rotation) {
        if (rotation == null) return new Matrix4f();
        JsonArray origin = rotation.getAsJsonArray("origin");
        if (origin == null || origin.size() != 3) throw new JsonParseException("Rotation requires a three-coordinate origin");
        Vector3f pivot = new Vector3f(origin.get(0).getAsFloat(), origin.get(1).getAsFloat(),
                origin.get(2).getAsFloat()).div(16);
        float x = angle(rotation, "x"), y = angle(rotation, "y"), z = angle(rotation, "z");
        Vector3f scale = new Vector3f(1);
        if (rotation.has("angle")) {
            float angle = angle(rotation, "angle");
            String axis = rotation.get("axis").getAsString();
            x = y = z = 0;
            switch (axis) {
                case "x" -> x = angle;
                case "y" -> y = angle;
                case "z" -> z = angle;
                default -> throw new JsonParseException("Unknown rotation axis: " + axis);
            }
            if (rotation.has("rescale") && rotation.get("rescale").getAsBoolean()) {
                float factor = 1 / (float) Math.cos(angle);
                if (!Float.isFinite(factor) || Math.abs(factor) > 1000)
                    throw new JsonParseException("Invalid rescale angle");
                switch (axis) {
                    case "x" -> scale.set(1, factor, factor);
                    case "y" -> scale.set(factor, 1, factor);
                    case "z" -> scale.set(factor, factor, 1);
                }
            }
        }
        return new Matrix4f().translation(pivot).rotateZ(z).rotateY(y).rotateX(x).scale(scale)
                .translate(-pivot.x, -pivot.y, -pivot.z);
    }

    private static float angle(JsonObject object, String key) {
        float degrees = object.has(key) ? object.get(key).getAsFloat() : 0;
        if (!Float.isFinite(degrees)) throw new JsonParseException("Non-finite rotation: " + key);
        return (float) Math.toRadians(degrees);
    }

    public BakedModel bake(Function<String, Material> materials,
                           Function<Material, TextureAtlasSprite> sprites,
                           ModelState state, ItemTransforms display, ItemOverrides overrides,
                           boolean ambientOcclusion, boolean blockLight) {
        List<BakedQuad> quads = new ArrayList<>();
        FaceBakery bakery = new FaceBakery();
        Matrix4f blockRotation = new Matrix4f().translation(0.5F, 0.5F, 0.5F)
                .mul(state.getRotation().getMatrix()).translate(-0.5F, -0.5F, -0.5F);
        List<BlockElement> elements = template.getElements();
        for (int i = 0; i < elements.size(); i++) {
            BlockElement element = elements.get(i);
            Matrix4f transform = new Matrix4f(blockRotation).mul(elementTransforms.get(i));
            Matrix3f normals = transform.normal(new Matrix3f());
            for (var entry : element.faces.entrySet()) {
                BlockElementFace face = entry.getValue();
                TextureAtlasSprite sprite = sprites.apply(materials.apply(face.texture()));
                // Bake the original rectangular UV mapping before rotating the face in space.
                BakedQuad source = bakery.bakeQuad(element.from, element.to, face, sprite,
                        entry.getKey(), BlockModelRotation.X0_Y0, null, element.shade);
                int[] vertices = source.getVertices().clone();
                int stride = vertices.length / 4;
                Vector3f normal = new Vector3f(entry.getKey().getStepX(), entry.getKey().getStepY(),
                        entry.getKey().getStepZ()).mul(normals).normalize();
                int packedNormal = (Math.round(normal.x * 127) & 255)
                        | ((Math.round(normal.y * 127) & 255) << 8)
                        | ((Math.round(normal.z * 127) & 255) << 16);
                for (int vertex = 0; vertex < 4; vertex++) {
                    int offset = vertex * stride;
                    Vector3f position = new Vector3f(Float.intBitsToFloat(vertices[offset]),
                            Float.intBitsToFloat(vertices[offset + 1]), Float.intBitsToFloat(vertices[offset + 2]));
                    transform.transformPosition(position);
                    vertices[offset] = Float.floatToRawIntBits(position.x);
                    vertices[offset + 1] = Float.floatToRawIntBits(position.y);
                    vertices[offset + 2] = Float.floatToRawIntBits(position.z);
                    vertices[offset + 7] = packedNormal;
                }
                quads.add(new BakedQuad(vertices, source.getTintIndex(),
                        Direction.getNearest(normal.x, normal.y, normal.z), sprite, source.isShade()));
            }
        }
        Map<Direction, List<BakedQuad>> culled = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.values()) culled.put(direction, List.of());
        // Furniture faces are internal to the block, so neighboring blocks must not cull them.
        return new SimpleBakedModel(List.copyOf(quads), culled, ambientOcclusion, true, blockLight,
                sprites.apply(materials.apply("particle")), display, overrides);
    }
}
