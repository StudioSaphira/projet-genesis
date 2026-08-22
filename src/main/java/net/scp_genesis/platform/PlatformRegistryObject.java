package net.scp_genesis.platform;

import java.util.function.Supplier;

public interface PlatformRegistryObject<T>
        extends Supplier<T> {

    @Override
    T get();
}