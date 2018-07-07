/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.RepositoryException;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The <code>ImmutableObjectNode</code> interface represents a read-only node in a workspace.
 */
public interface ImmutableObjectNode extends ImmutableItem {

    /**
     * Returns the identifier of this node.
     * Applies to both referenceable and non-referenceable nodes.
     * <p>
     * A <code>RepositoryException</code> is thrown if an error occurs.
     *
     * @return the identifier of this node.
     * @throws RepositoryException if an error occurs.
     * @since JCR 2.0
     */
    String getIdentifier();

    default Stream<String> getItemNames() {
        return getItems()
                .map(ImmutableItem::getName);
    }

    default Stream<ImmutableObjectNode> getNodes() {
        return getItems()
                .filter(i -> i instanceof ImmutableObjectNode)
                .map(i -> (ImmutableObjectNode) i);
    }

    default Stream<ImmutableProperty> getProperties() {
        return getItems()
                .filter(i -> i instanceof ImmutableProperty)
                .map(i -> (ImmutableProperty) i);
    }

    @Override
    default boolean isObjectNode() {
        return true;
    }

    /**
     * Returns all child nodes of this node accessible through the current
     * <code>Session</code>. Does <i>not</i> include properties of this
     * <code>ImmutableObjectNode</code>. The same reacquisition semantics apply as with {@link
     * #getItem(String)}. If this node has no accessible child nodes, then an
     * empty iterator is returned.
     *
     * @return A <code>NodeIterator</code> over all child <code>ImmutableObjectNode</code>s of
     * this <code>ImmutableObjectNode</code>.
     * @throws RepositoryException if an error occurs.
     */
    default List<ImmutableItem> getItemList() {
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
    default Stream<ImmutableProperty> getReferences() {
        return getProperties()
                .filter(p -> StandardTypes.REFERENCE.equals(p.getTypeDefinition()));
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
    default boolean hasItem(String fieldName) {
        return getItem(fieldName) != null;
    }

    /**
     * Indicates whether this node has child nodes. Returns <code>true</code> if
     * this node has one or more child nodes accessible through the current
     * <code>Session</code>; <code>false</code> otherwise.
     *
     * @return <code>true</code> if this node has one or more child nodes;
     * <code>false</code> otherwise.
     * @throws RepositoryException if an error occurs.
     */
    default boolean hasNodes() {
        return getNodes()
                .findAny()
                .isPresent();
    }

    // type definition methods

    /**
     * Returns an array of <code>NodeType</code> objects representing the mixin
     * node types in effect for this node. This includes only those mixin types
     * explicitly assigned to this node. It does not include mixin types
     * inherited through the addition of supertypes to the primary type
     * hierarchy or through the addition of supertypes to the type hierarchy of
     * any of the declared mixin types.
     *
     * @return an array of  <code>NodeType</code> objects.
     * @throws RepositoryException if an error occurs
     */
    default Collection<TypeDefinition> getMixinNodeTypes() {
        return Collections.emptyList();
    }

    /**
     * Returns <code>true</code> if this node is of the specified primary node
     * type or mixin type, or a subtype thereof. Returns <code>false</code>
     * otherwise.
     * <p>
     * This method respects the effective node type of the node.
     *
     * @param nodeTypeName the name of a node type.
     * @return <code>true</code> If this node is of the specified primary node
     * type or mixin type, or a subtype thereof. Returns
     * <code>false</code> otherwise.
     * @throws RepositoryException if an error occurs.
     */
    default boolean isNodeType(String nodeTypeName) {
        return getMixinNodeTypes().stream()
                .filter(t -> nodeTypeName.equals(t.getIdentifier()))
                .findAny()
                .isPresent();
    }
}