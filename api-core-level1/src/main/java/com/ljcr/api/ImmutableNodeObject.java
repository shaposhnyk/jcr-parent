/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.api.exceptions.RepositoryException;
import com.ljcr.api.exceptions.UnsupportedRepositoryOperationException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * The <code>ImmutableNodeObject</code> interface represents a read-only node in a workspace.
 */
@Nonnull
public interface ImmutableNodeObject extends ImmutableNode {
    @Override
    default boolean isCollection() {
        return false;
    }

    @Override
    default boolean isObject() {
        return true;
    }

    @Override
    default boolean isScalarValue() {
        return false;
    }

    default Stream<String> getFieldNames() {
        return getTypeDefinition()
                .getPropertyDefinitions()
                .stream()
                .map(PropertyDefinition::getIdentifier);
    }

    /**
     * Indicates whether a node exists at <code>fieldName</code> Returns
     * <code>true</code> if a node accessible through the current
     * <code>Session</code> exists at <code>fieldName</code> and
     * <code>false</code> otherwise.
     *
     * @param fieldName The path of a (possible) node.
     * @return <code>true</code> if a node exists at <code>fieldName</code>;
     * <code>false</code> otherwise.
     * @throws RepositoryException if an error occurs.
     */
    default boolean hasField(String fieldName) {
        return getItem(fieldName) != null;
    }

    @Nullable
    @Override
    default <U> U accept(@Nonnull ImmutableItemVisitor<U> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    default Stream<ImmutableNode> getElements() {
        return getFieldNames().map(this::getItem);
    }

    @Nullable
    @Override
    default ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        return null;
    }

    @Nullable
    @Override
    default Object getValue() {
        throw new UnsupportedRepositoryOperationException("Ambigous call. Use getItem(fieldName) instead");
    }
}