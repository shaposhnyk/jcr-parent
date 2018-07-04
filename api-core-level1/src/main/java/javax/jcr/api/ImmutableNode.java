/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package javax.jcr.api;

import javax.jcr.api.definitions.NodeType;
import javax.jcr.api.exceptions.PathNotFoundException;
import javax.jcr.api.exceptions.RepositoryException;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The <code>ImmutableNode</code> interface represents a read-only node in a workspace.
 */
public interface ImmutableNode extends ImmutableItem {
    /**
     * Returns the node at <code>relPath</code> relative to this node.
     * <p>
     * If <code>relPath</code> contains a path element that refers to a node
     * with same-name sibling nodes without explicitly including an index using
     * the array-style notation (<code>[x]</code>), then the index [1] is
     * assumed (indexing of same name siblings begins at 1, not 0, in order to
     * preserve compatibility with XPath).
     * <p>
     * Within the scope of a single <code>Session</code> object, if a
     * <code>ImmutableNode</code> object has been acquired, any subsequent call of
     * <code>getNode</code> reacquiring the same node must return a
     * <code>ImmutableNode</code> object reflecting the same state as the earlier
     * <code>ImmutableNode</code> object. Whether this object is actually the same
     * <code>ImmutableNode</code> instance, or simply one wrapping the same state, is up
     * to the implementation.
     *
     * @param relPath The relative path of the node to retrieve.
     * @return The node at <code>relPath</code>.
     * @throws PathNotFoundException if no node exists at the specified path or
     *                               the current <code>Session</code> does not read access to the node at the
     *                               specified path.
     * @throws RepositoryException   If another error occurs.
     */
    ImmutableNode getNode(String relPath) throws PathNotFoundException;

    /**
     * Returns all child nodes of this node accessible through the current
     * <code>Session</code>. Does <i>not</i> include properties of this
     * <code>ImmutableNode</code>. The same reacquisition semantics apply as with {@link
     * #getNode(String)}. If this node has no accessible child nodes, then an
     * empty iterator is returned.
     *
     * @return A <code>NodeIterator</code> over all child <code>ImmutableNode</code>s of
     * this <code>ImmutableNode</code>.
     * @throws RepositoryException if an error occurs.
     */
    Collection<ImmutableNode> getNodes();

    default Stream<ImmutableNode> getNodesAsStream() {
        return getNodes().stream();
    }

    /**
     * Returns the property at <code>relPath</code> relative to
     * <code>this</code> node. The same reacquisition semantics apply as with
     * <code>{@link #getNode(String)}</code>.
     *
     * @param relPath The relative path of the property to retrieve.
     * @return The property at <code>relPath</code>.
     * @throws PathNotFoundException if no property exists at the specified path
     *                               or if the current
     *                               <p>
     *                               <code>Session</code> does not have read access to the specified
     *                               property.
     * @throws RepositoryException   If another error occurs.
     */
    ImmutableProperty getProperty(String relPath) throws PathNotFoundException;

    /**
     * Returns all properties of this node accessible through the current
     * <code>Session</code>. Does <i>not</i> include child <i>nodes</i> of this
     * node. The same reacquisition semantics apply as with <code>{@link
     * #getNode(String)}</code>. If this node has no accessible properties, then
     * an empty iterator is returned.
     *
     * @return A <code>PropertyIterator</code>.
     * @throws RepositoryException if an error occurs.
     */
    Collection<ImmutableProperty> getProperties();

    default Stream<ImmutableProperty> getPropertiesAsStream() {
        return getProperties().stream();
    }

    /**
     * Returns the identifier of this node. Applies to both referenceable and
     * non-referenceable nodes.
     * <p>
     * A <code>RepositoryException</code> is thrown if an error occurs.
     *
     * @return the identifier of this node.
     * @throws RepositoryException if an error occurs.
     * @since JCR 2.0
     */
    String getIdentifier();

    /**
     * This method returns the index of this node within the ordered set of its
     * same-name sibling nodes. This index is the one used to address same-name
     * siblings using the square-bracket notation, e.g.,
     * <code>/a[3]/b[4]</code>. Note that the index always starts at 1 (not 0),
     * for compatibility with XPath. As a result, for nodes that do not have
     * same-name-siblings, this method will always return 1.
     *
     * @return The index of this node within the ordered set of its same-name
     * sibling nodes.
     * @throws RepositoryException if an error occurs.
     */
    int getIndex();

    /**
     * This method returns all <code>REFERENCE</code> properties that refer to
     * this node and that are accessible through the current
     * <code>Session</code>. Equivalent to <code>ImmutableNode.getReferences(null)</code>.
     * <p>
     * If this node has no referring <code>REFERENCE</code> properties, an
     * empty iterator is returned. This includes the case where this node is not referenceable.
     *
     * @return A <code>PropertyIterator</code>.
     * @throws RepositoryException if an error occurs.
     * @see #getReferences(String).
     */
    default Collection<ImmutableProperty> getReferences() {
        return getPropertiesAsStream()
                .filter(p -> PropertyType.REFERENCE == p.getType())
                .collect(Collectors.toList());
    }

    /**
     * This method returns all <code>REFERENCE</code> properties that refer to
     * this node, have the specified <code>name</code> and that are accessible
     * through the current <code>Session</code>.
     * <p>
     * If the <code>name</code> parameter is <code>null</code> then all
     * referring <code>REFERENCES</code> are returned regardless of name.
     * <p>
     * Some implementations may only return properties that have been persisted.
     * Some may return both properties that have been persisted and those that
     * have been dispatched but not persisted (for example, those saved within a
     * transaction but not yet committed) while others implementations may
     * return these two categories of property as well as properties that are
     * still pending and not yet dispatched.
     * <p>
     * In implementations that support versioning, this method does not return
     * properties that are part of the frozen state of a version in version
     * storage.
     * <p>
     * If this node has no referring <code>REFERENCE</code> properties with the specified name, an
     * empty iterator is returned. This includes the case where this node is not referenceable.
     *
     * @param name name of referring <code>REFERENCE</code> properties to be
     *             returned; if <code>null</code> then all referring <code>REFERENCE</code>s
     *             are returned.
     * @return A <code>PropertyIterator</code>.
     * @throws RepositoryException if an error occurs.
     * @since JCR 2.0
     */
    default Collection<ImmutableProperty> getReferences(String name) {
        final Predicate<ImmutableProperty> nameFilter = name == null ? p -> true : p -> name.equals(p.getName());
        return getPropertiesAsStream()
                .filter(p -> PropertyType.REFERENCE == p.getType())
                .filter(nameFilter)
                .collect(Collectors.toList());
    }

    /**
     * This method returns all <code>WEAKREFERENCE</code> properties that refer
     * to this node and that are accessible through the current
     * <code>Session</code>. Equivalent to <code>ImmutableNode.getWeakReferences(null)</code>.
     * <p>
     * If this node has no referring <code>WEAKREFERENCE</code> properties, an
     * empty iterator is returned. This includes the case where this node is not referenceable.
     *
     * @return A <code>PropertyIterator</code>.
     * @throws RepositoryException if an error occurs.
     * @see #getWeakReferences(String).
     * @since JCR 2.0
     */
    default Collection<ImmutableProperty> getWeakReferences() {
        return getPropertiesAsStream()
                .filter(p -> PropertyType.WEAKREFERENCE == p.getType())
                .collect(Collectors.toList());
    }

    /**
     * This method returns all <code>WEAKREFERENCE</code> properties that refer
     * to this node, have the specified <code>name</code> and that are
     * accessible through the current <code>Session</code>.
     * <p>
     * If the <code>name</code> parameter is <code>null</code> then all
     * referring <code>WEAKREFERENCE</code> are returned regardless of name.
     * <p>
     * Some implementations may only return properties that have been persisted.
     * Some may return both properties that have been persisted and those that
     * have been dispatched but not persisted (for example, those saved within a
     * transaction but not yet committed) while others implementations may
     * return these two categories of property as well as properties that are
     * still pending and not yet dispatched.
     * <p>
     * In implementations that support versioning, this method does not return
     * properties that are part of the frozen state of a version in version
     * storage.
     * <p>
     * If this node has no referring <code>WEAKREFERENCE</code> properties with the specified name, an
     * empty iterator is returned. This includes the case where this node is not referenceable.
     *
     * @param name name of referring <code>WEAKREFERENCE</code> properties to be
     *             returned; if <code>null</code> then all referring
     *             <code>WEAKREFERENCE</code>s are returned.
     * @return A <code>PropertyIterator</code>.
     * @throws RepositoryException if an error occurs.
     * @since JCR 2.0
     */
    default Collection<ImmutableProperty> getWeakReferences(String name) {
        final Predicate<ImmutableProperty> nameFilter = name == null ? p -> true : p -> name.equals(p.getName());
        return getPropertiesAsStream()
                .filter(p -> PropertyType.WEAKREFERENCE == p.getType())
                .filter(nameFilter)
                .collect(Collectors.toList());
    }

    /**
     * Indicates whether a node exists at <code>relPath</code> Returns
     * <code>true</code> if a node accessible through the current
     * <code>Session</code> exists at <code>relPath</code> and
     * <code>false</code> otherwise.
     *
     * @param relPath The path of a (possible) node.
     * @return <code>true</code> if a node exists at <code>relPath</code>;
     * <code>false</code> otherwise.
     * @throws RepositoryException if an error occurs.
     */
    default boolean hasNode(String relPath) {
        return getNode(relPath) != null;
    }

    /**
     * Indicates whether a property exists at <code>relPath</code> Returns
     * <code>true</code> if a property accessible through the current
     * <code>Session</code> exists at <code>relPath</code> and
     * <code>false</code> otherwise.
     *
     * @param relPath The path of a (possible) property.
     * @return <code>true</code> if a property exists at <code>relPath</code>;
     * <code>false</code> otherwise.
     * @throws RepositoryException if an error occurs.
     */
    default boolean hasProperty(String relPath) {
        return getProperty(relPath) != null;
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
    boolean hasNodes();

    /**
     * Indicates whether this node has properties. Returns <code>true</code> if
     * this node has one or more properties accessible through the current
     * <code>Session</code>; <code>false</code> otherwise.
     *
     * @return <code>true</code> if this node has one or more properties;
     * <code>false</code> otherwise.
     * @throws RepositoryException if an error occurs.
     */
    boolean hasProperties();

    /**
     * Returns the primary node type in effect for this node. Which
     * <code>NodeType</code> is returned when this method is called on the root
     * node of a workspace is up to the implementation.
     *
     * @return a <code>NodeType</code> object.
     * @throws RepositoryException if an error occurs
     */
    NodeType getPrimaryNodeType();

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
    Collection<NodeType> getMixinNodeTypes() throws RepositoryException;

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
                .filter(t -> nodeTypeName.equals(t.getName()))
                .findAny()
                .isPresent();
    }

    /**
     * Returns the node definition that applies to this node. In some cases
     * there may appear to be more than one definition that could apply to this
     * node. However, it is assumed that upon creation of this node, a single
     * particular definition was used and it is <i>that</i> definition that this
     * method returns. How this governing definition is selected upon node
     * creation from among others which may have been applicable is an
     * implementation issue and is not covered by this specification. The
     * <code>NodeDefinition</code> returned when this method is called on the
     * root node of a workspace is also up to the implementation.
     *
     * @return a <code>NodeDefinition</code> object.
     * @throws RepositoryException if an error occurs.
     * @see NodeType#getChildNodeDefinitions
     */
    Object getDefinition() throws RepositoryException;
}