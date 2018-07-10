/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import com.ljcr.api.exceptions.RepositoryException;
import com.ljcr.api.exceptions.ValueFormatException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * A generic holder for the value of a repository item.
 * A <code>ImmutableValue</code> object can
 * be used without knowing the actual property type (<code>STRING</code>,
 * <code>DOUBLE</code>, <code>BINARY</code> etc.).
 * <p>
 * An implementation is only required to support equality comparisons on
 * <code>ImmutableValue</code> instances that were acquired from the same
 * <code>Session</code> and whose contents have not been read. The equality
 * comparison must not change the state of the <code>ImmutableValue</code> instances even
 * though the <code>asString()</code> method in the above definition implies a
 * state change.
 */
@Nonnull
public interface ImmutableValue {

    /**
     * @return native value representaion
     */
    @Nullable
    Object getValue();

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
    @Nullable
    String asString();

    /**
     * Returns a <code>long</code> representation of this value.
     *
     * @return A <code>long</code> representation of this value.
     * @throws NumberFormatException if conversion to an <code>long</code> is not
     *                               possible.
     * @throws RepositoryException   if another error occurs.
     */
    @Nullable
    long asLong() throws NumberFormatException;

    /**
     * Returns a <code>double</code> representation of this value.
     *
     * @return A <code>double</code> representation of this value.
     * @throws NumberFormatException if conversion to a <code>double</code> is
     *                               not possible.
     * @throws RepositoryException   if another error occurs.
     */
    @Nullable
    double asDouble() throws NumberFormatException;

    /**
     * Returns a <code>BigDecimal</code> representation of this value.
     *
     * @return A <code>BigDecimal</code> representation of this value.
     * @throws NumberFormatException if conversion to a <code>BigDecimal</code>
     *                               is not possible.
     * @throws RepositoryException   if another error occurs.
     * @since JCR 2.0
     */
    @Nullable
    BigDecimal asDecimal() throws NumberFormatException;

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
    @Nullable
    LocalDate asDate();

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
    @Nullable
    LocalDateTime asDateTime();


    /**
     * Returns a <code>Boolean</code> representation of this value.
     *
     * @return A <code>Boolean</code> representation of this value.
     * @throws RepositoryException if another error occurs.
     */
    @Nullable
    boolean asBoolean();

    ImmutableBinaryValue asBinaryValue();

    ImmutableObjectNode asObjectNode();

    ImmutableArrayNode asArrayNode();
}