package net.scp_genesis.fabric.copycatblocks.renderer.model;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record FabricCopycatModelDefinition(
        FabricCopycatUnbakedModel.GeometryType geometryType,
        ResourceLocation baseModel,
        Map<String, ResourceLocation> baseModels
) {

    public static FabricCopycatModelDefinition cube(
            ResourceLocation baseModel
    ) {
        return new FabricCopycatModelDefinition(
                FabricCopycatUnbakedModel.GeometryType.CUBE,
                baseModel,
                null
        );
    }

    public static FabricCopycatModelDefinition multipart(
            FabricCopycatUnbakedModel.GeometryType geometryType,
            Map<String, ResourceLocation> baseModels
    ) {
        return new FabricCopycatModelDefinition(
                geometryType,
                null,
                baseModels
        );
    }
}