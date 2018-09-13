package com.ljcr.api;

import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.api.exceptions.RepositoryException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * Generic minimal interface for container-alike types,
 * p.ex. list, collection or map
 */
@Nonnull
public interface ImmutableCoreContainer extends ImmutableCoreObject {
    /**
     * @return type definition for that value
     */
    @Nonnull
    @Override
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
    @Override
    ImmutableCoreNode getItem(@Nonnull String fieldName) throws PathNotFoundException;

    /**
     * @return a steam of array items
     */
    @Nonnull
    Stream<ImmutableNode> getElements();
}
