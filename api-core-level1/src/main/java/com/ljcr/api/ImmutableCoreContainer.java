package com.ljcr.api;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

/**
 * Generic minimal interface for container-alike types,
 * p.ex. list, collection or map
 */
@Nonnull
public interface ImmutableCoreContainer<T extends ImmutableCoreNode<T>> extends ImmutableCoreObject<T> {
    /**
     * @return a steam of items contained in the container
     */
    @Nonnull
    Stream<T> getElements();
}
