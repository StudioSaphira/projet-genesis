package net.scp_genesis.fabric.copycatblocks.renderer.util;

import com.mojang.math.Transformation;
import net.minecraft.client.resources.model.ModelState;
import org.jetbrains.annotations.NotNull;

public final class FabricCopycatModelState
        implements ModelState {

    private final Transformation rotation;
    private final boolean uvLocked;

    public FabricCopycatModelState(
            @NotNull Transformation rotation,
            boolean uvLocked
    ) {
        this.rotation = rotation;
        this.uvLocked = uvLocked;
    }

    @Override
    public @NotNull Transformation getRotation() {
        return rotation;
    }

    @Override
    public boolean isUvLocked() {
        return uvLocked;
    }
}