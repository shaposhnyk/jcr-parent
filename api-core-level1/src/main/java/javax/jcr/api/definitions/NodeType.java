/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package javax.jcr.api.definitions;

import javax.jcr.api.ImmutableValue;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * A <code>NodeType</code> object represents a "live" node type that is
 * registered in the repository.
 */
public interface NodeType extends NodeTypeDefinition {

    /**
     * Returns all supertypes of this node type in the node type inheritance
     * hierarchy. For primary types apart from <code>nt:base</code>, this list
     * will always include at least <code>nt:base</code>. For mixin types, there
     * is no required supertype.
     *
     * @return an array of <code>NodeType</code> objects.
     * @see #getDeclaredSupertypes
     */
    Collection<NodeType> getSupertypes();

    /**
     * Returns the <i>direct</i> supertypes of this node type in the node type
     * inheritance hierarchy, that is, those actually declared in this node
     * type. In single-inheritance systems, this will always be an array of size
     * 0 or 1. In systems that support multiple inheritance of node types this
     * array may be of size greater than 1.
     *
     * @return an array of <code>NodeType</code> objects.
     * @see #getSupertypes
     */
    Collection<NodeType> getDeclaredSupertypes();

    /**
     * Returns all subtypes of this node type in the node type inheritance
     * hierarchy.
     *
     * @return a <code>NodeTypeIterator</code>.
     * @see #getDeclaredSubtypes
     * @since JCR 2.0
     */
    Stream<NodeType> getSubtypes();

    /**
     * Returns the <i>direct</i> subtypes of this node type in the node type
     * inheritance hierarchy, that is, those which actually declared this node
     * type in their list of supertypes.
     *
     * @return an <code>NodeTypeIterator</code>.
     * @see #getSubtypes
     * @since JCR 2.0
     */
    Stream<NodeType> getDeclaredSubtypes();

    /**
     * Returns <code>true</code> if the name of <i>this</i> node type or any of
     * its direct or indirect supertypes is equal to <code>nodeTypeName</code>,
     * otherwise returns <code>false</code>.
     *
     * @param nodeTypeName the name of a node type.
     * @return a boolean
     */
    boolean isNodeType(String nodeTypeName);

    /**
     * Returns an array containing the property definitions of this node type.
     * This includes both those property definitions actually declared in this
     * node type and those inherited from the supertypes of this type.
     *
     * @return an array containing the property definitions.
     * @see #getDeclaredPropertyDefinitions()
     */
    Collection<PropertyDefinition> getPropertyDefinitions();

    /**
     * Returns an array containing the child node definitions of this node type.
     * This includes both those child node definitions actually declared in this
     * node type and those inherited from the supertypes of this node type.
     *
     * @return an array containing the child node definitions.
     * @see #getDeclaredChildNodeDefinitions()
     */
    Collection<NodeDefinition> getChildNodeDefinitions();

    /**
     * Returns <code>true</code> if setting <code>propertyName</code> to
     * <code>value</code> is allowed by this node type. Otherwise returns
     * <code>false</code>.
     *
     * @param propertyName The name of the property
     * @param value        A <code>ImmutableValue</code> object.
     * @return a boolean
     */
    default boolean canSetProperty(String propertyName, ImmutableValue value) {
        return false;
    }

    /**
     * Returns <code>true</code> if setting <code>propertyName</code> to
     * <code>values</code> is allowed by this node type. Otherwise returns
     * <code>false</code>.
     *
     * @param propertyName The name of the property
     * @param values       A <code>ImmutableValue</code> array.
     * @return a boolean
     */
    default boolean canSetProperty(String propertyName, ImmutableValue[] values) {
        return false;
    }

    /**
     * Returns <code>true</code> if this node type allows the addition of a
     * child node called <code>childNodeName</code> without specific node type
     * information (that is, given the definition of this parent node type, the
     * child node name is sufficient to determine the intended child node type).
     * Returns <code>false</code> otherwise.
     *
     * @param childNodeName The name of the child node.
     * @return a boolean
     */
    default boolean canAddChildNode(String childNodeName) {
        return false;
    }

    /**
     * Returns <code>true</code> if this node type allows the addition of a
     * child node called <code>childNodeName</code> of node type
     * <code>nodeTypeName</code>. Returns <code>false</code> otherwise.
     *
     * @param childNodeName The name of the child node.
     * @param nodeTypeName  The name of the node type of the child node.
     * @return a boolean
     */
    default boolean canAddChildNode(String childNodeName, String nodeTypeName) {
        return false;
    }

    /**
     * Returns <code>true</code> if removing the child item called
     * <code>itemName</code> is allowed by this node type. Returns
     * <code>false</code> otherwise.
     *
     * @param itemName The name of the child item
     * @return a boolean
     * @deprecated As of JCR 2.0, clients should use {@link #canRemoveNode(String)}
     * and {@link #canRemoveProperty(String)} instead.
     */
    default boolean canRemoveItem(String itemName) {
        return false;
    }

    /**
     * Returns <code>true</code> if removing the child node called
     * <code>nodeName</code> is allowed by this node type. Returns
     * <code>false</code> otherwise.
     *
     * @param nodeName The name of the child node
     * @return a boolean
     * @since JCR 2.0
     */
    default boolean canRemoveNode(String nodeName) {
        return false;
    }

    /**
     * Returns <code>true</code> if removing the property called
     * <code>propertyName</code> is allowed by this node type. Returns
     * <code>false</code> otherwise.
     *
     * @param propertyName The name of the property
     * @return a boolean
     * @since JCR 2.0
     */
    default boolean canRemoveProperty(String propertyName) {
        return false;
    }
}