/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package javax.jcr.api;

import javax.jcr.api.definitions.PropertyType;
import javax.jcr.api.definitions.StandardPropertyTypes;
import javax.jcr.api.exceptions.RepositoryException;
import javax.jcr.api.exceptions.UnsupportedRepositoryOperationException;
import javax.jcr.api.exceptions.ValueFormatException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A generic holder for the value of a property. A <code>ImmutableValue</code> object can
 * be used without knowing the actual property type (<code>STRING</code>,
 * <code>DOUBLE</code>, <code>BINARY</code> etc.).
 * <p>
 * The <code>ImmutableBinaryValue</code> interface and its related methods in
 * <code>ImmutableProperty</code>, <code>ImmutableValue</code> and <code>ValueFactory</code>
 * replace the deprecated <code>ImmutableValue.getStream</code> and
 * <code>ImmutableProperty.getStream</code> methods from JCR 1.0. However, though
 * <code>getStream</code> has been deprecated, for reasons of backward
 * compatibility its behavior must still conform to the following rules: <ul>
 * <li> A <code>ImmutableValue</code> object can be read using type-specific
 * <code>get</code> methods. These methods are divided into two groups: <ul>
 * <li> The non-stream <code>get</code> methods <code>getString()</code>,
 * <code>getBinary()</code>, <code>getDate()</code>, <code>getDecimal()</code>,
 * <code>getLong()</code>, <code>getDouble()</code> and
 * <code>getBoolean()</code>. </li> <li> <code>getStream()</code>. </li> </ul>
 * </li> <li> Once a <code>ImmutableValue</code> object has been read once using
 * <code>getStream()</code>, all subsequent calls to <code>getStream()</code>
 * will return the same <code>Stream</code> object. This may mean, for example,
 * that the stream returned is fully or partially consumed. In order to get a
 * fresh stream the <code>ImmutableValue</code> object must be reacquired via {@link
 * ImmutableProperty#getValue()} or {@link ImmutableProperty#getValues()}. </li> <li> Once a
 * <code>ImmutableValue</code> object has been read once using <code>getStream()</code>,
 * any subsequent call to any of the non-stream <code>get</code> methods will
 * throw an <code>IllegalStateException</code>. In order to successfully invoke
 * a non-stream <code>get</code> method, the <code>ImmutableValue</code> must be
 * reacquired via {@link ImmutableProperty#getValue()} or {@link ImmutableProperty#getValues()}.
 * </li> <li> Once a <code>ImmutableValue</code> object has been read once using a
 * non-stream get method, any subsequent call to <code>getStream()</code> will
 * throw an <code>IllegalStateException</code>. In order to successfully invoke
 * <code>getStream()</code>, the <code>ImmutableValue</code> must be reacquired via
 * {@link ImmutableProperty#getValue()} or {@link ImmutableProperty#getValues()}. </ul>
 * <p>
 * Two <code>ImmutableValue</code> instances, <code>v1</code> and <code>v2</code>, are
 * considered equal if and only if: <ul> <li><code>v1.getType() ==
 * v2.getType()</code>, and,</li> <li><code>v1.getString().equals(v2.getString())</code></li>
 * </ul> Actually comparing two <code>ImmutableValue</code> instances by converting them
 * to string form may not be practical in some cases (for example, if the values
 * are very large binaries). Consequently, the above is intended as a normative
 * definition of <code>ImmutableValue</code> equality but not as a procedural test of
 * equality. It is assumed that implementations will have efficient means of
 * determining equality that conform with the above definition.
 * <p>
 * An implementation is only required to support equality comparisons on
 * <code>ImmutableValue</code> instances that were acquired from the same
 * <code>Session</code> and whose contents have not been read. The equality
 * comparison must not change the state of the <code>ImmutableValue</code> instances even
 * though the <code>getString()</code> method in the above definition implies a
 * state change.
 */
public interface ImmutableValue {

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
    String getString() throws IllegalStateException;

    /**
     * Returns a <code>long</code> representation of this value.
     *
     * @return A <code>long</code> representation of this value.
     * @throws NumberFormatException if conversion to an <code>long</code> is not
     *                               possible.
     * @throws RepositoryException   if another error occurs.
     */
    default long getLong() throws NumberFormatException {
        return Long.valueOf(getString());
    }

    /**
     * Returns a <code>double</code> representation of this value.
     *
     * @return A <code>double</code> representation of this value.
     * @throws NumberFormatException if conversion to a <code>double</code> is
     *                               not possible.
     * @throws RepositoryException   if another error occurs.
     */
    default double getDouble() throws NumberFormatException {
        return Double.valueOf(getString());
    }

    /**
     * Returns a <code>BigDecimal</code> representation of this value.
     *
     * @return A <code>BigDecimal</code> representation of this value.
     * @throws NumberFormatException if conversion to a <code>BigDecimal</code>
     *                               is not possible.
     * @throws RepositoryException   if another error occurs.
     * @since JCR 2.0
     */
    default BigDecimal getDecimal() throws NumberFormatException {
        throw new UnsupportedRepositoryOperationException();
    }

    /**
     * Returns a <code>Calendar</code> representation of this value.
     * <p>
     * The object returned is a copy of the stored value, so changes to it are
     * not reflected in internal storage.
     *
     * @return A <code>Calendar</code> representation of this value.
     * @throws DateTimeParseException if conversion to a <code>Calendar</code> is
     *                                not possible.
     * @throws RepositoryException    if another error occurs.
     */
    default LocalDate getDate() {
        return LocalDate.parse(getString(), DateTimeFormatter.ISO_DATE);
    }

    /**
     * Returns a <code>Calendar</code> representation of this value.
     * <p>
     * The object returned is a copy of the stored value, so changes to it are
     * not reflected in internal storage.
     *
     * @return A <code>Calendar</code> representation of this value.
     * @throws DateTimeParseException if conversion to a <code>Calendar</code> is
     *                                not possible.
     * @throws RepositoryException    if another error occurs.
     */
    default LocalDateTime getDateTime() {
        return LocalDateTime.parse(getString(), DateTimeFormatter.ISO_DATE_TIME);
    }


    /**
     * Returns a <code>Boolean</code> representation of this value.
     *
     * @return A <code>Boolean</code> representation of this value.
     * @throws RepositoryException if another error occurs.
     */
    default boolean getBoolean() {
        return Boolean.valueOf(getString());
    }

    default ImmutableBinaryValue getBinaryValue() {
        throw new UnsupportedRepositoryOperationException();
    }

    /**
     * Returns the <code>type</code> of this <code>ImmutableValue</code>. One of: <ul>
     * <li><code>PropertyType.STRING</code></li> <li><code>PropertyType.DATE</code></li>
     * <li><code>PropertyType.BINARY</code></li> <li><code>PropertyType.DOUBLE</code></li>
     * <li><code>PropertyType.DECIMAL</code></li>
     * <li><code>PropertyType.LONG</code></li> <li><code>PropertyType.BOOLEAN</code></li>
     * <li><code>PropertyType.NAME</code></li> <li><code>PropertyType.PATH</code></li>
     * <li><code>PropertyType.REFERENCE</code></li> <li><code>PropertyType.WEAKREFERENCE</code></li>
     * <li><code>PropertyType.URI</code></li></ul> See <code>{@link
     * PropertyType}</code>.
     * <p>
     * The type returned is that which was set at property creation.
     *
     * @return an int
     */
    default PropertyType getType() {
        return StandardPropertyTypes.STRING;
    }
}