package net.scp_genesis.neoforge.furnitures;

import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.scp_genesis.common.furnitures.client.FurnituresGeometry;

import java.io.IOException;

public final class NeoForgeFurnituresModelLoader implements IGeometryLoader<NeoForgeFurnituresGeometry> {
    public static final ResourceLocation ID = ResourceLocation.parse("scp_genesis:furnitures");

    public static void register(ModelEvent.RegisterGeometryLoaders event) {
        event.register(ID, new NeoForgeFurnituresModelLoader());
    }

    @Override
    public NeoForgeFurnituresGeometry read(JsonObject json, JsonDeserializationContext context) {
        ResourceLocation geometry = ResourceLocation.parse(json.get("geometry").getAsString());
        ResourceLocation file = geometry.withPath(path -> "furnitures/" + path + ".json");
        try (var reader = Minecraft.getInstance().getResourceManager().getResourceOrThrow(file).openAsReader()) {
            return new NeoForgeFurnituresGeometry(new FurnituresGeometry(
                    JsonParser.parseReader(reader).getAsJsonObject(), json));
        } catch (IOException exception) {
            throw new JsonParseException("Cannot load furniture geometry " + file, exception);
        }
    }
}
