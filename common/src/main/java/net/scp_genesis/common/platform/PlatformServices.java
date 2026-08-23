package net.scp_genesis.common.platform;

import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ServiceLoader;

/**
 * Provides access to platform-specific services.
 *
 * <p>The common module uses this class to access functionality that
 * cannot be implemented directly without depending on a specific
 * mod loader.</p>
 */
public final class PlatformServices {

    private static final PlatformModelData MODEL_DATA =
            load(PlatformModelData.class);

    private static final PlatformServicesImpl SERVICES =
            load(PlatformServicesImpl.class);

    private PlatformServices() {
    }

    /**
     * Returns the platform implementation responsible for creating
     * model data.
     *
     * @return platform model data service
     */
    public static PlatformModelData modelData() {
        return MODEL_DATA;
    }

    /**
     * Requests the platform to update the model data of a BlockEntity.
     *
     * @param blockEntity BlockEntity whose model data must be updated
     */
    public static void requestModelDataUpdate(
            BlockEntity blockEntity
    ) {
        SERVICES.requestModelDataUpdate(blockEntity);
    }

    /**
     * Loads a platform service implementation using Java's
     * ServiceLoader mechanism.
     *
     * @param service service interface
     * @param <T> service type
     * @return loaded service implementation
     */
    private static <T> T load(Class<T> service) {
        return ServiceLoader.load(service)
                .findFirst()
                .orElseThrow(
                        () -> new IllegalStateException(
                                "No implementation found for platform service: "
                                        + service.getName()
                        )
                );
    }

    /**
     * Internal platform service contract.
     */
    public interface PlatformServicesImpl {

        /**
         * Requests the platform to update the model data of a BlockEntity.
         *
         * @param blockEntity BlockEntity to update
         */
        void requestModelDataUpdate(
                BlockEntity blockEntity
        );
    }
}