/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package javax.jcr.api;

import javax.jcr.api.definitions.StandardTypes;
import javax.jcr.api.definitions.TypeDefinition;
import javax.jcr.api.exceptions.RepositoryException;
import javax.jcr.api.exceptions.ValueFormatException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
     * Returns the <code>type</code> of this <code>ImmutableValue</code>. One of: <ul>
     * <li><code>PropertyType.STRING</code></li> <li><code>PropertyType.DATE</code></li>
     * <li><code>PropertyType.BINARY</code></li> <li><code>PropertyType.DOUBLE</code></li>
     * <li><code>PropertyType.DECIMAL</code></li>
     * <li><code>PropertyType.LONG</code></li> <li><code>PropertyType.BOOLEAN</code></li>
     * <li><code>PropertyType.NAME</code></li> <li><code>PropertyType.PATH</code></li>
     * <li><code>PropertyType.REFERENCE</code></li> <li><code>PropertyType.WEAKREFERENCE</code></li>
     * <li><code>PropertyType.URI</code></li></ul> See <code>{@link TypeDefinition}</code>.
     * <p>
     * The type returned is that which was set at property creation.
     *
     * @return an int
     */
    default TypeDefinition getTypeDefinition() {
        return StandardTypes.UNDEFINED;
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

    @Override
    default boolean getBoolean() {
        return getValue().getBoolean();
    }

    @Override
    default BigDecimal getDecimal() {
        return getValue().getDecimal();
    }

    @Override
    default double getDouble() {
        return getValue().getDouble();
    }

    @Override
    default long getLong() throws NumberFormatException {
        return getValue().getLong();
    }

    @Override
    default ImmutableBinaryValue getBinaryValue() {
        return getValue().getBinaryValue();
    }

    @Override
    default LocalDate getDate() {
        return getValue().getDate();
    }

    @Override
    default LocalDateTime getDateTime() {
        return getValue().getDateTime();
    }
}
