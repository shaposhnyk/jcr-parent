/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package javax.jcr.api.definitions;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;

/**
 * The <code>TypeDefinition</code> interface provides methods for
 * discovering the static definition of a node type. These are accessible both
 * before and after the node type is registered. Its subclass
 * <code>NodeType</code> adds methods that are relevant only when the node type
 * is "live"; that is, after it has been registered. Note that the separate
 * <code>NodeDefinition</code> interface only plays a significant role in
 * implementations that support node type registration. In those cases it serves
 * as the superclass of both <code>NodeType</code> and <code>NodeTypeTemplate</code>.
 * In implementations that do not support node type registration, only objects
 * implementing the subinterface <code>NodeType</code> will be encountered.
 *
 * @since JCR 2.0
 */
public interface TypeDefinition {

    /**
     * Returns the name of the node type.
     * <p>
     * In implementations that support node type registration, if this
     * <code>TypeDefinition</code> object is actually a newly-created empty
     * <code>NodeTypeTemplate</code>, then this method will return
     * <code>null</code>.
     *
     * @return a <code>String</code>
     * @since JCR 2.0 moved here from JCR 1.0 <code>NodeType</code>.
     */
    String getIdentifier();

    /**
     * Returns an array containing the property definitions of this node type.
     * This includes both those property definitions actually declared in this
     * node type and those inherited from the supertypes of this type.
     *
     * @return an array containing the property definitions.
     * @see #getDeclaredPropertyDefinitions()
     */
    default Collection<PropertyDefinition> getPropertyDefinitions() {
        return getDeclaredPropertyDefinitions();
    }

    /**
     * Returns an array containing the property definitions actually declared in
     * this node type.
     * <p>
     * In implementations that support node type registration, if this
     * <code>TypeDefinition</code> object is actually a newly-created empty
     * <code>NodeTypeTemplate</code>, then this method will return
     * <code>null</code>.
     *
     * @return an array of <code>PropertyDefinition</code>s
     * @since JCR 2.0 moved here from JCR 1.0 <code>NodeType</code>.
     */
    default Collection<PropertyDefinition> getDeclaredPropertyDefinitions() {
        return Collections.emptyList();
    }

    /**
     * Returns the names of the supertypes actually declared in this node type.
     * <p>
     * In implementations that support node type registration, if this
     * <code>TypeDefinition</code> object is actually a newly-created empty
     * <code>NodeTypeTemplate</code>, then this method will return an empty array.
     *
     * @return an array of <code>String</code>s
     * @since JCR 2.0
     */
    default Collection<String> getDeclaredSupertypeNames() {
        return Collections.emptyList();
    }

    /**
     * Returns <code>true</code> if this is an abstract node type; returns
     * <code>false</code> otherwise.
     * <p>
     * An abstract node type is one that cannot be assigned as the primary or
     * mixin type of a node but can be used in the definitions of other node
     * types as a superclass.
     * <p>
     * In implementations that support node type registration, if this
     * <code>TypeDefinition</code> object is actually a newly-created empty
     * <code>NodeTypeTemplate</code>, then this method will return
     * <code>false</code>.
     *
     * @return a <code>boolean</code>
     * @since JCR 2.0
     */
    default boolean isAbstract() {
        return false;
    }

    /**
     * Returns <code>true</code> if this is a mixin type; returns
     * <code>false</code> if it is primary.
     * <p>
     * In implementations that support node type registration, if this
     * <code>TypeDefinition</code> object is actually a newly-created empty
     * <code>NodeTypeTemplate</code>, then this method will return
     * <code>false</code>.
     *
     * @return a <code>boolean</code>
     * @since JCR 2.0 moved here from JCR 1.0 <code>NodeType</code>.
     */
    default boolean isMixin() {
        return false;
    }

    /**
     * Returns <code>true</code> if nodes of this type must support orderable
     * child nodes; returns <code>false</code> otherwise. If a node type returns
     * <code>true</code> on a call to this method, then all nodes of that node
     * type <i>must</i> support the method <code>ImmutableObjectNode.orderBefore</code>. If a
     * node type returns <code>false</code> on a call to this method, then nodes
     * of that node type <i>may</i> support <code>ImmutableObjectNode.orderBefore</code>. Only
     * the primary node type of a node controls that node's status in this
     * regard. This setting on a mixin node type will not have any effect on the
     * node.
     * <p>
     * In implementations that support node type registration, if this
     * <code>TypeDefinition</code> object is actually a newly-created empty
     * <code>NodeTypeTemplate</code>, then this method will return
     * <code>false</code>.
     *
     * @return a <code>boolean</code>
     * @since JCR 2.0 moved here from JCR 1.0 <code>NodeType</code>.
     */
    default boolean hasOrderableChildNodes() {
        return false;
    }

    /**
     * Returns <code>true</code> if the node type is queryable, meaning that the
     * available-query-operators, full-text-searchable and query-orderable
     * attributes of its property definitions take effect. See {@link
     * javax.jcr.api.definitions.PropertyDefinition#getAvailableQueryOperators()},
     * {@link javax.jcr.api.definitions.PropertyDefinition#isFullTextSearchable()} and
     * {@link javax.jcr.api.definitions.PropertyDefinition#isQueryOrderable()}.
     * <p>
     * If a node type is declared non-queryable then these attributes of its
     * property definitions have no effect.
     * <p>
     * In implementations that support node type registration, if this
     * <code>TypeDefinition</code> object is actually a newly-created empty
     * <code>NodeTypeTemplate</code>, then this method will return
     * an implementation-determined defalt value.
     *
     * @return a <code>boolean</code>
     * @since JCR 2.0
     */
    default boolean isQueryable() {
        return true;
    }
}