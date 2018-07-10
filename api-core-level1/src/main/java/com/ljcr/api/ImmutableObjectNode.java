/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.exceptions.RepositoryException;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The <code>ImmutableObjectNode</code> interface represents a read-only node in a workspace.
 */
@Nonnull
public interface ImmutableObjectNode extends ImmutableNode {

    @Override
    default boolean isObjectNode() {
        return true;
    }

    /**
     * Returns the identifier of this node.
     * Applies to both referenceable and non-referenceable nodes.
     * Null should be returned if node is non-referenceable.
     * <p>
     * A <code>RepositoryException</code> is thrown if an error occurs.
     *
     * @return the identifier of this node.
     * @throws RepositoryException if an error occurs.
     * @since JCR 2.0
     */
    String getReference();

    default Stream<String> getFieldNames() {
        return getItems()
                .map(ImmutableNode::getName);
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

    /**
     * Returns all child nodes of this node accessible through the current
     * <code>Session</code>.
     * The same reacquisition semantics apply as with {@link
     * #getItem(String)}. If this node has no accessible child nodes, then an
     * empty iterator is returned.
     *
     * @return A <code>NodeIterator</code> over all child <code>ImmutableObjectNode</code>s of
     * this <code>ImmutableObjectNode</code>.
     * @throws RepositoryException if an error occurs.
     */
    default List<ImmutableNode> getFieldsList() {
        return getItems().collect(Collectors.toList());
    }

    /**
     * This method returns all <code>REFERENCE</code> properties that refer to
     * this node and that are accessible through the current
     * <code>Session</code>. Equivalent to <code>ImmutableObjectNode.getReferences(null)</code>.
     * <p>
     * If this node has no referring <code>REFERENCE</code> properties, an
     * empty iterator is returned. This includes the case where this node is not referenceable.
     *
     * @return A <code>PropertyIterator</code>.
     * @throws RepositoryException if an error occurs.
     */
    default Stream<ImmutableNode> getReferences() {
        return getItems()
                .filter(p -> StandardTypes.REFERENCE.equals(p.getTypeDefinition()));
    }
}