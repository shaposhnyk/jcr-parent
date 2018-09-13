/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.api.exceptions.RepositoryException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The <code>ImmutableObjectNode</code> interface represents a read-only node in a workspace.
 */
@Nonnull
public interface ImmutableCoreObject {
    /**
     * @return type definition of the value
     */
    @Nonnull
    TypeDefinition getTypeDefinition();

    /**
     * Returns the node at <code>fieldName</code> relative to this node.
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
    ImmutableCoreNode getItem(@Nonnull String fieldName) throws PathNotFoundException;
}