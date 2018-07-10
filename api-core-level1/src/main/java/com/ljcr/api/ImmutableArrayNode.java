package com.ljcr.api;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Nonnull
public interface ImmutableArrayNode extends ImmutableNode {

    @Override
    default boolean isArrayNode() {
        return true;
    }

    @Override
    default Collection<ImmutableNode> getValue() {
        return getElements().collect(Collectors.toList());
    }

    /**
     * @return a steam of array items
     */
    default Stream<ImmutableNode> getElements() {
        return getItems();
    }
}
