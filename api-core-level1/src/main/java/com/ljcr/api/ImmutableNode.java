package com.ljcr.api;


import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.api.exceptions.RepositoryException;
import com.ljcr.api.exceptions.ValueFormatException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

/**
 * The <code>ImmutableNode</code> is the base read-only item of hierarchical key-value repository.
 */
@Nonnull
public interface ImmutableNode extends ImmutableCoreNode<ImmutableNode> {
    /**
     * Accepts an <code>ImmutableItemVisitor</code>. Calls the appropriate
     * <code>ImmutableItemVisitor</code> <code>visit</code> method of the according to
     * whether <i>this</i> <code>ImmutableNode</code> is a <code>ImmutableNodeObject</code> or a
     * <code>ImmutableProperty</code>.
     *
     * @param visitor The ImmutableItemVisitor to be accepted.
     * @throws RepositoryException if an error occurs.
     */
    @Nullable
    <U> U accept(@Nonnull ImmutableItemVisitor<U> visitor);

    default ImmutableNodeObject asObjectNode() {
        return (ImmutableNodeObject) this;
    }

    default ImmutableNodeCollection asArrayNode() {
        return (ImmutableNodeCollection) this;
    }

    @Nullable
    @Override
    ImmutableNode getItem(@Nonnull PropertyDefinition fieldName);

    @Nullable
    @Override
    default ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        return ImmutableCoreNode.super.getItem(fieldName);
    }

    @Nonnull
    @Override
    Stream<ImmutableNode> getElements();

    /**
     * Returns a <code>String</code> representation of this value.
     *
     * @return A <code>String</code> representation of the value of this
     * property.
     * @throws ValueFormatException  if conversion to a <code>String</code> is
     *                               not possible.
     * @throws IllegalStateException if <code>getStream</code> has previously
     *                               been called on this <code>ImmutableNodeScalar</code> instance. In this case a new
     *                               <code>ImmutableNodeScalar</code> instance must be acquired in order to successfully
     *                               call this method.
     * @throws RepositoryException   if another error occurs.
     */
    @Nullable
    default String asString() {
        Object objValue = getValue();
        if (objValue instanceof String) {
            return (String) objValue;
        }
        return objValue == null ? null : objValue.toString();
    }

    /**
     * Returns a <code>Boolean</code> representation of this value.
     *
     * @return A <code>Boolean</code> representation of this value.
     * @throws RepositoryException if another error occurs.
     */
    @Nullable
    default boolean asBoolean() {
        return Boolean.valueOf(asString());
    }

    /**
     * Returns a <code>long</code> representation of this value.
     *
     * @return A <code>long</code> representation of this value.
     * @throws NumberFormatException if conversion to an <code>long</code> is not
     *                               possible.
     * @throws RepositoryException   if another error occurs.
     */
    @Nullable
    default long asLong() throws NumberFormatException {
        return Long.parseLong(asString());
    }

    /**
     * Returns a <code>double</code> representation of this value.
     *
     * @return A <code>double</code> representation of this value.
     * @throws NumberFormatException if conversion to a <code>double</code> is
     *                               not possible.
     * @throws RepositoryException   if another error occurs.
     */
    @Nullable
    default double asDouble() throws NumberFormatException {
        return Double.parseDouble(asString());
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
    @Nullable
    default BigDecimal asDecimal() throws NumberFormatException {
        return new BigDecimal(asString());
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
    @Nullable
    default LocalDate asDate() {
        return LocalDate.parse(asString(), DateTimeFormatter.ISO_DATE);
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
    @Nullable
    default LocalDateTime asDateTime() {
        return LocalDateTime.parse(asString(), DateTimeFormatter.ISO_DATE_TIME);
    }
}