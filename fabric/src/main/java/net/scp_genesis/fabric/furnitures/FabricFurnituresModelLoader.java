package net.scp_genesis.fabric.furnitures;

import com.google.gson.*;
import net.fabricmc.fabric.api.client.model.loading.v1.*;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.scp_genesis.common.furnitures.client.FurnituresGeometry;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public final class FabricFurnituresModelLoader {
    private static final String LOADER = "scp_genesis:furnitures";

    private FabricFurnituresModelLoader() {}

    private record Definition(JsonObject geometry, JsonObject appearance) {}

    public static void register() {
        PreparableModelLoadingPlugin.register(FabricFurnituresModelLoader::load, (definitions, context) -> {
            context.resolveModel().register(resolver -> definitions.get(resolver.id()));
            context.modifyModelBeforeBake().register(ModelModifier.OVERRIDE_PHASE, (model, bake) -> {
                if (bake.resourceId() != null && definitions.containsKey(bake.resourceId()))
                    return definitions.get(bake.resourceId());
                if (bake.topLevelId() != null) {
                    for (var entry : definitions.entrySet()) {
                        ResourceLocation id = entry.getKey();
                        if (id.getPath().startsWith("item/") && bake.topLevelId().equals(
                                ModelResourceLocation.inventory(id.withPath(path -> path.substring(5)))))
                            return entry.getValue();
                    }
                }
                return model;
            });
        });
    }

    private static CompletableFuture<Map<ResourceLocation, FabricFurnituresUnbakedModel>> load(
            ResourceManager resources, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            Map<ResourceLocation, JsonObject> models = new HashMap<>();
            resources.listResources("models", id -> id.getNamespace().equals("scp_genesis")
                    && id.getPath().endsWith(".json")).forEach((file, resource) -> {
                try (var reader = resource.openAsReader()) {
                    models.put(file.withPath(path -> path.substring(7, path.length() - 5)),
                            JsonParser.parseReader(reader).getAsJsonObject());
                } catch (IOException exception) {
                    throw new JsonParseException("Cannot read " + file, exception);
                }
            });
            Map<ResourceLocation, Definition> definitions = new HashMap<>();
            models.forEach((id, json) -> {
                if (!json.has("loader") || !LOADER.equals(json.get("loader").getAsString())) return;
                ResourceLocation geometry = ResourceLocation.parse(json.get("geometry").getAsString());
                ResourceLocation file = geometry.withPath(path -> "furnitures/" + path + ".json");
                try (var reader = resources.getResourceOrThrow(file).openAsReader()) {
                    definitions.put(id, new Definition(JsonParser.parseReader(reader).getAsJsonObject(), json));
                } catch (IOException exception) {
                    throw new JsonParseException("Cannot load furniture geometry " + file, exception);
                }
            });
            // Flatten child appearances (including inventory models) while retaining the vanilla display parent.
            boolean changed;
            do {
                changed = false;
                for (var entry : models.entrySet()) {
                    JsonObject json = entry.getValue();
                    if (definitions.containsKey(entry.getKey()) || !json.has("parent") || json.has("loader")) continue;
                    Definition parent = definitions.get(ResourceLocation.parse(json.get("parent").getAsString()));
                    if (parent == null) continue;
                    JsonObject appearance = parent.appearance().deepCopy();
                    for (var property : json.entrySet()) {
                        if (property.getKey().equals("parent")) continue;
                        if ((property.getKey().equals("textures") || property.getKey().equals("display"))
                                && appearance.has(property.getKey())) {
                            JsonObject merged = appearance.getAsJsonObject(property.getKey());
                            property.getValue().getAsJsonObject().entrySet().forEach(
                                    value -> merged.add(value.getKey(), value.getValue().deepCopy()));
                        } else appearance.add(property.getKey(), property.getValue().deepCopy());
                    }
                    definitions.put(entry.getKey(), new Definition(parent.geometry(), appearance));
                    changed = true;
                }
            } while (changed);
            Map<ResourceLocation, FabricFurnituresUnbakedModel> result = new HashMap<>();
            definitions.forEach((id, definition) -> result.put(id,
                    new FabricFurnituresUnbakedModel(new FurnituresGeometry(definition.geometry(), definition.appearance()))));
            return Map.copyOf(result);
        }, executor);
    }
}
