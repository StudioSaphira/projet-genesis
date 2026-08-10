package net.scp_genesis.copycatblocks.renderer.model;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.scp_genesis.constants.ModConstants;
import org.jetbrains.annotations.NotNull;
import java.util.function.Function;

/**
 * Unbaked geometry for Copycat Blocks.
 *
 * <p>This class is responsible for creating the baked model used
 * to render Copycat Blocks.</p>
 */
public final class CopycatUnbakedGeometry
        implements IUnbakedGeometry<CopycatUnbakedGeometry> {

    @Override
    public @NotNull BakedModel bake(
            @NotNull IGeometryBakingContext context,
            @NotNull ModelBaker baker,
            @NotNull Function<Material, TextureAtlasSprite> spriteGetter,
            @NotNull ModelState modelState,
            @NotNull ItemOverrides overrides
    ) {
        ModConstants.LOGGER.info("[COPYCAT] Baking Copycat Model");

        return new CopycatBakedModel();
    }

}
