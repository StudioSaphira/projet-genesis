package net.scp_genesis.fabric.mixin;

import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.scp_genesis.fabric.copycatblocks.renderer.model.FabricCopycatModelDataLoader;
import net.scp_genesis.fabric.copycatblocks.renderer.model.FabricCopycatModelDefinition;
import net.scp_genesis.fabric.copycatblocks.renderer.model.FabricCopycatUnbakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class FabricModelBakeryMixin {

    @ModifyVariable(
            method = "getModel",
            at = @At(
                    value = "STORE"
            ),
            ordinal = 0
    )
    private UnbakedModel scpGenesis$resolveCopycatModel(
            UnbakedModel original,
            ResourceLocation resourceLocation
    ) {
        Map<ResourceLocation, FabricCopycatModelDefinition> definitions =
                FabricCopycatModelDataLoader.getDefinitions();

        FabricCopycatModelDefinition definition =
                definitions.get(resourceLocation);

        if (definition == null) {
            return original;
        }

        if (definition.geometryType()
                == FabricCopycatUnbakedModel.GeometryType.CUBE) {

            return new FabricCopycatUnbakedModel(
                    definition.baseModel()
            );
        }

        return new FabricCopycatUnbakedModel(
                definition.geometryType(),
                definition.baseModels()
        );
    }
}