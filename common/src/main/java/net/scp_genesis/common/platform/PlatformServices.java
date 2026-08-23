package net.scp_genesis.common.platform;

import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Provides access to platform-specific implementations.
 *
 * <p>The implementation is loaded through Java's
 * {@link java.util.ServiceLoader}.</p>
 */
public final class PlatformServices {

    private static final PlatformServicesImpl INSTANCE =
            java.util.ServiceLoader
                    .load(PlatformServicesImpl.class)
                    .findFirst()
                    .orElseThrow(
                            () -> new IllegalStateException(
                                    "No PlatformServicesImpl implementation found."
                            )
                    );

    private PlatformServices() {
    }

    /**
     * Returns the platform implementation.
     */
    public static PlatformServicesImpl get() {
        return INSTANCE;
    }

    /**
     * Returns the platform-specific model data provider.
     */
    public static PlatformModelData<?> modelData() {
        return INSTANCE.modelData();
    }

    /**
     * Requests a model data update for the specified BlockEntity.
     */
    public static void requestModelDataUpdate(BlockEntity blockEntity) {
        INSTANCE.requestModelDataUpdate(blockEntity);
    }

    /**
     * Returns the model data associated with the specified BlockEntity.
     */
    public static PlatformModelData.ModelData getModelData(BlockEntity blockEntity) {
        return INSTANCE.getModelData(blockEntity);
    }
}