/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.exceptions.UnsupportedRepositoryOperationException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * A generic holder for the value of a repository item.
 * A <code>ImmutableNodeScalar</code> object can
 * be used without knowing the actual property type (<code>STRING</code>,
 * <code>DOUBLE</code>, <code>BINARY</code> etc.).
 * <p>
 * An implementation is only required to support equality comparisons on
 * <code>ImmutableNodeScalar</code> instances that were acquired from the same
 * <code>Session</code> and whose contents have not been read. The equality
 * comparison must not change the state of the <code>ImmutableNodeScalar</code> instances even
 * though the <code>asString()</code> method in the above definition implies a
 * state change.
 */
@Nonnull
public interface ImmutableNodeScalar extends ImmutableNode {
    @Override
    default boolean isObject() {
        return false;
    }

    @Override
    default boolean isScalarValue() {
        return true;
    }

    @Override
    default boolean isCollection() {
        return false;
    }

    @Nullable
    @Override
    default <U> U accept(@Nonnull ImmutableItemVisitor<U> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    default String getName() {
        throw new UnsupportedRepositoryOperationException("No reference in generic scalars");
    }

    @Nonnull
    @Override
    default Stream<ImmutableNode> getElements() {
        throw new UnsupportedRepositoryOperationException("No elements in generic scalars");
    }

    @Nullable
    @Override
    default ImmutableNode getItem(@Nonnull PropertyDefinition field) {
        throw new UnsupportedRepositoryOperationException("No fields in generic scalars");
    }
}