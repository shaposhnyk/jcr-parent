/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package javax.jcr.api;

import javax.jcr.api.definitions.NodeType;
import javax.jcr.api.exceptions.ItemNotFoundException;
import javax.jcr.api.exceptions.RepositoryException;
import javax.jcr.api.exceptions.ValueFormatException;
import java.util.Collections;
import java.util.List;

/**
 * A <code>ImmutableProperty</code> object represents the smallest granularity of content
 * storage. It has a single parent node and no children. A property consists of
 * a name and a value, or in the case of multi-value properties, a set of values
 * all of the same type. See <code>{@link ImmutableValue}</code>.
 */
public interface ImmutableProperty extends ImmutableItem, ImmutableValue {

    /**
     * If this property is of type <code>REFERENCE</code>,
     * <code>WEAKREFERENCE</code> or <code>PATH</code> (or convertible to one of
     * these types) this method returns the <code>ImmutableNode</code> to which this
     * property refers.
     * <p>
     * If this property is of type <code>PATH</code> and it contains a relative
     * path, it is interpreted relative to the parent node of this property. For
     * example "<code>.</code>" refers to the parent node itself,
     * "<code>..</code>" to the parent of the parent node and "<code>foo</code>"
     * to a sibling node of this property.
     *
     * @return the referenced ImmutableNode
     * @throws ValueFormatException  if this property cannot be converted to a
     *                               referring type (<code>REFERENCE</code>, <code>WEAKREFERENCE</code> or
     *                               <code>PATH</code>), if the property is multi-valued or if this property
     *                               is a referring type but is currently part of the frozen state of a
     *                               version in version storage.
     * @throws ItemNotFoundException If this property is of type
     *                               <code>PATH</code> or <code>WEAKREFERENCE</code> and no target node
     *                               accessible by the current <code>Session</code> exists in this workspace.
     *                               Note that this applies even if the property is a <code>PATHS</code> and a
     *                               <i>property</i> exists at the specified location. To dereference to a
     *                               target property (as opposed to a target node), the method
     *                               <code>ImmutableProperty.getProperty</code> is used.
     * @throws RepositoryException   if another error occurs.
     */
    ImmutableNode getNode() throws ItemNotFoundException, ValueFormatException;

    /**
     * If this property is of type <code>PATH</code> (or convertible to this
     * type) this method returns the <code>ImmutableProperty</code> to which <i>this</i>
     * property refers.
     * <p>
     * If this property contains a relative path, it is interpreted relative to
     * the parent node of this property. Therefore, when resolving such a
     * relative path, the segment "<code>.</code>" refers to
     * the parent node itself, "<code>..</code>" to the parent of the parent
     * node and "<code>foo</code>" to a sibling property of this property or
     * this property itself.
     * <p>
     * For example, if this property is located at
     * <code>/a/b/c</code> and it has a value of "<code>../d</code>" then this
     * method will return the property at <code>/a/d</code> if such exists.
     * <p>
     * If this property is multi-valued, this method throws a
     * <code>ValueFormatException</code>.
     * <p>
     * If this property cannot be converted to a <code>PATH</code> then a
     * <code>ValueFormatException</code> is thrown.
     * <p>
     * If this property is currently part of the frozen state of a version in
     * version storage, this method will throw a <code>ValueFormatException</code>.
     *
     * @return the referenced property
     * @throws ValueFormatException  if this property cannot be converted to a
     *                               <code>PATH</code>, if the property is multi-valued or if this property is
     *                               a referring type but is currently part of the frozen state of a version
     *                               in version storage.
     * @throws ItemNotFoundException If no property accessible by the current
     *                               <code>Session</code> exists in this workspace at the specified path. Note
     *                               that this applies even if a <i>node</i> exists at the specified location.
     *                               To dereference to a target node, the method <code>ImmutableProperty.getNode</code>
     *                               is used.
     * @throws RepositoryException   if another error occurs.
     * @since JCR 2.0
     */
    ImmutableProperty getProperty() throws ItemNotFoundException;

    /**
     * Returns the length of the value of this property.
     * <p>
     * For a <code>BINARY</code> property, <code>getLength</code> returns the
     * number of bytes. For other property types, <code>getLength</code> returns
     * the same value that would be returned by calling {@link
     * String#length()} on the value when it has been converted to a
     * <code>STRING</code> according to standard JCR property type conversion.
     * <p>
     * Returns -1 if the implementation cannot determine the length.
     *
     * @return an <code>long</code>.
     * @throws ValueFormatException if this property is multi-valued.
     * @throws RepositoryException  if another error occurs.
     */
    default long getLength() {
        return getString().length();
    }

    /**
     * Returns an array holding the lengths of the values of this (multi-value)
     * property in bytes where each is individually calculated as described in
     * {@link #getLength()}.
     * <p>
     * Returns a <code>-1</code> in the appropriate position if the
     * implementation cannot determine the length of a value.
     *
     * @return an array of lengths
     * @throws ValueFormatException if this property is single-valued.
     * @throws RepositoryException  if another error occurs.
     */
    default List<Long> getLengths() {
        return Collections.singletonList(getLength());
    }

    /**
     * Returns the property definition that applies to this property. In some
     * cases there may appear to be more than one definition that could apply to
     * this node. However, it is assumed that upon creation or change of this
     * property, a single particular definition is chosen by the implementation.
     * It is <i>that</i> definition that this method returns. How this governing
     * definition is selected upon property creation or change from among others
     * which may have been applicable is an implementation issue and is not
     * covered by this specification.
     *
     * @return a <code>PropertyDefinition</code> object.
     * @throws RepositoryException if an error occurs.
     * @see NodeType#getPropertyDefinitions
     */
    default Object getDefinition() {
        return null;
    }

    /**
     * Returns <code>true</code> if this property is multi-valued and
     * <code>false</code> if this property is single-valued.
     *
     * @return <code>true</code> if this property is multi-valued;
     * <code>false</code> otherwise.
     * @throws RepositoryException if an error occurs.
     */
    default boolean isMultiValued() {
        return false;
    }
}
