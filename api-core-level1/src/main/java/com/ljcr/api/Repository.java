/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import com.ljcr.api.definitions.ContainerTypeDefinition;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.ItemNotFoundException;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.api.exceptions.RepositoryException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * A <code>Repository</code> object represents a view onto a persitent workspace
 * within a repository. This view is defined by the authorization settings of
 * the <code>Session</code> object associated with the <code>Repository</code>
 * object. Each <code>Repository</code> object is associated one-to-one with a
 * <code>Session</code> object. The <code>Repository</code> object can be
 * acquired by calling <code>{@link Session#getWorkspace()}</code> on the
 * associated <code>Session</code> object.
 */
@Nonnull
public interface Repository {

    /**
     * Returns the name of the actual persistent workspace represented by this
     * <code>Repository</code> object. This the name used in
     * <code>Repository.login</code>.
     *
     * @return the name of this workspace.
     */
    public String getName();

    /**
     * Returns the root node of the workspace, "/". This node is the main access
     * point to the content of the workspace.
     *
     * @return The root node of the workspace: a <code>{@link ImmutableNode}</code>
     * object.
     * @throws RepositoryException if an error occurs.
     */
    ImmutableNode getRootNode();

    /**
     * Returns the node specified by the given identifier. Applies to both
     * referenceable and non-referenceable nodes.
     *
     * @param id An identifier.
     * @return A <code>ImmutableObjectNode</code>.
     * @throws ItemNotFoundException if no node with the specified identifier
     *                               exists or if this <code>Session<code> does not have read access to the
     *                               node with the specified identifier.
     * @throws RepositoryException   if another error occurs.
     * @since JCR 2.0
     */
    @Nullable
    default ImmutableObjectNode getNodeByReference(TypeDefinition type, String id) throws ItemNotFoundException {
        if (type instanceof ContainerTypeDefinition) {
            return ((ContainerTypeDefinition) type).findByReference(id);
        }
        return null;
    }

    @Nullable
    default ImmutableObjectNode getNodeByReference(String typeName, Object id) {
        if (!(id instanceof String)) {
            return null;
        }
        ContainerTypeDefinition type = findContainerType(typeName);
        if (type == null) {
            return null;
        }
        return getNodeByReference(type, (String) id);
    }

    /**
     * Returns the node at the specified absolute path in the workspace. If no
     * such node exists, then it returns the property at the specified path.
     * <p>
     * This method should only be used if the application does not know whether
     * the item at the indicated path is property or node. In cases where the
     * application has this information, either {@link #getNode} or {@link
     * #getProperty} should be used, as appropriate. In many repository
     * implementations the node and property-specific methods are likely to be
     * more efficient than <code>getItem</code>.
     *
     * @param absPath An absolute path.
     * @return the specified <code>ImmutableNode</code>.
     * @throws PathNotFoundException if no accessible item is found at the
     *                               specified path.
     * @throws RepositoryException   if another error occurs.
     */
    @Nullable
    default ImmutableNode getItem(Path absPath) throws PathNotFoundException {
        int idx = 0;
        Path path = absPath.normalize();
        ImmutableNode obj = getRootNode();
        while (idx < path.getNameCount()) {
            String fieldName = path.getName(idx++).toString();
            obj = obj.getItem(fieldName);
        }
        return obj;
    }

    default Collection<TypeDefinition> getKnownTypes() {
        return Collections.emptyList();
    }

    @Nullable
    default ContainerTypeDefinition findContainerType(String typeName) {
        Collection<TypeDefinition> knownTypes = getKnownTypes();
        return knownTypes.stream()
                .filter(t -> t instanceof ContainerTypeDefinition)
                .filter(t -> typeName.equals(t.getIdentifier()))
                .map(t -> (ContainerTypeDefinition) t)
                .findFirst()
                .orElse(null);
    }
}