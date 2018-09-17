package com.ljcr.api;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.exceptions.UnsupportedRepositoryOperationException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.stream.Collectors;

@Nonnull
public interface ImmutableNodeCollection extends ImmutableNode {
    @Override
    default boolean isCollection() {
        return true;
    }

    @Override
    default boolean isObject() {
        return false;
    }

    @Override
    default boolean isScalarValue() {
        return false;
    }

    @Override
    default Collection<ImmutableNode> getValue() {
        return getElements().collect(Collectors.toList());
    }

    @Nullable
    @Override
    default <U> U accept(@Nonnull ImmutableItemVisitor<U> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    default String getName() {
        throw new UnsupportedRepositoryOperationException("Unreferencable non type: array");
    }

    @Nullable
    @Override
    default ImmutableNode getItem(@Nonnull PropertyDefinition fieldName) {
        throw new UnsupportedRepositoryOperationException("Unexpected node type: array");
    }
}
