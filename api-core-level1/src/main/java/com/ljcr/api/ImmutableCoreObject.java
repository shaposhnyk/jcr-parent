/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.api.exceptions.RepositoryException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The <code>ImmutableNodeObject</code> interface represents a read-only node in a workspace.
 */
@Nonnull
public interface ImmutableCoreObject<T extends ImmutableCoreNode<T>> {
    /**
     * @return type definition of the value
     */
    @Nonnull
    TypeDefinition getTypeDefinition();

    /**
     * Returns property <code>p</code> of this node.
     * Usually this is more efficient as getItem(String)
     * <p>
     */
    @Nullable
    T getItem(@Nonnull PropertyDefinition p);

    /**
     * Returns the node at <code>fieldName</code> relative to this node.
     * Usually this is not so efficient as getItem(PropertyDefinition)
     * <p>
     *
     * @param fieldName field name to retrieve.
     * @return The node at <code>fieldName</code>.
     * @throws PathNotFoundException if no node exists at the specified path or
     *                               the current <code>Session</code> does not read access to the node at the
     *                               specified path.
     * @throws RepositoryException   If another error occurs.
     */
    @Nullable
    default T getItem(@Nonnull String fieldName) throws PathNotFoundException {
        final TypeDefinition type = getTypeDefinition();
        final PropertyDefinition p = type.getFieldDefByName(fieldName);
        if (p == null) {
            throw new PathNotFoundException("Property " + fieldName + " not found on " + type);
        }
        return getItem(p);
    }
}