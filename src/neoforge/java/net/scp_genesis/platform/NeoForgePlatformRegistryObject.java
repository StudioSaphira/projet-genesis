package net.scp_genesis.platform;

import java.util.function.Supplier;

public final class NeoForgePlatformRegistryObject<T>
        implements PlatformRegistryObject<T> {

    private final Supplier<T> supplier;

    public NeoForgePlatformRegistryObject(
            Supplier<T> supplier
    ) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        return supplier.get();
    }
}