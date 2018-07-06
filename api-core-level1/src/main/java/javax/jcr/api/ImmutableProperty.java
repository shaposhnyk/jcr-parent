/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package javax.jcr.api;

import javax.jcr.api.definitions.NodeType;
import javax.jcr.api.definitions.PropertyDefinition;
import javax.jcr.api.exceptions.RepositoryException;
import javax.jcr.api.exceptions.ValueFormatException;
import java.util.stream.Stream;

/**
 * A <code>ImmutableProperty</code> object represents the smallest granularity of content
 * storage. It has a single parent node and no children. A property consists of
 * a name and a value, or in the case of multi-value properties, a set of values
 * all of the same type. See <code>{@link ImmutableValue}</code>.
 */
public interface ImmutableProperty extends ImmutableItem, ImmutableValue {

    ImmutableValue getValue();

    @Override
    default Stream<ImmutableItem> getItems() {
        return Stream.empty();
    }

    /**
     * Returns a <code>String</code> representation of this value.
     *
     * @return A <code>String</code> representation of the value of this
     * property.
     * @throws ValueFormatException  if conversion to a <code>String</code> is
     *                               not possible.
     * @throws IllegalStateException if <code>getStream</code> has previously
     *                               been called on this <code>ImmutableValue</code> instance. In this case a new
     *                               <code>ImmutableValue</code> instance must be acquired in order to successfully
     *                               call this method.
     * @throws RepositoryException   if another error occurs.
     */
    @Override
    default String getString() {
        return getValue().getString();
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
    default PropertyDefinition getDefinition() {
        return null;
    }
}
