package com.ljcr.api;


import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.api.exceptions.RepositoryException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

/**
 * The <code>ImmutableNode</code> is the base read-only item of hierarchical key-value repository.
 */
@Nonnull
public interface ImmutableNode extends ImmutableValue {
    /**
     * Returns the normalized absolute path of this item in hierarchy.
     *
     * @return the normalized absolute path of this <code>ImmutableNode</code>.
     * @throws RepositoryException if an error occurs.
     */
    Path getKey();

    /**
     * Returns the value associated with this key
     *
     * @return the value associated with this key
     */
    @Override
    @Nullable
    Object getValue();

    /**
     * Returns the name of this <code>ImmutableNode</code> in qualified form. If this
     * <code>ImmutableNode</code> is the root node of the workspace, an empty string is
     * returned.
     *
     * @return the name of this <code>ImmutableNode</code> in qualified form or an empty
     * string if this <code>ImmutableNode</code> is the root node of a workspace.
     * @throws RepositoryException if an error occurs.
     */
    default String getName() {
        return getKey().getNameCount() == 0 ? "" : getKey().getFileName().toString();
    }

    /**
     * Returns the normalized absolute path to this item.
     *
     * @return the normalized absolute path of this <code>ImmutableNode</code>.
     * @throws RepositoryException if an error occurs.
     */
    @Nonnull
    default Path getPath() {
        return getKey().getParent();
    }

    /**
     * Returns the node at <code>fieldName</code> relative to this node.
     * <p>
     * If <code>fieldName</code> contains a path element that refers to a node
     * with same-name sibling nodes without explicitly including an index using
     * the array-style notation (<code>[x]</code>), then the index [1] is
     * assumed (indexing of same name siblings begins at 1, not 0, in order to
     * preserve compatibility with XPath).
     * <p>
     * Within the scope of a single <code>Session</code> object, if a
     * <code>ImmutableObjectNode</code> object has been acquired, any subsequent call of
     * <code>getNode</code> reacquiring the same node must return a
     * <code>ImmutableObjectNode</code> object reflecting the same state as the earlier
     * <code>ImmutableObjectNode</code> object. Whether this object is actually the same
     * <code>ImmutableObjectNode</code> instance, or simply one wrapping the same state, is up
     * to the implementation.
     *
     * @param fieldName The relative path of the node to retrieve.
     * @return The node at <code>fieldName</code>.
     * @throws PathNotFoundException if no node exists at the specified path or
     *                               the current <code>Session</code> does not read access to the node at the
     *                               specified path.
     * @throws RepositoryException   If another error occurs.
     */
    @Nullable
    ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException;

    /**
     * All direct children of this node, i.e.
     * - empty stream for a property
     * - all fields for an object
     * - all items for array
     *
     * @return all direct children items of this node
     */
    Stream<ImmutableNode> getItems();

    /**
     * Indicates whether this <code>ImmutableNode</code> is a <code>ImmutableObjectNode</code> or not.
     * Returns <code>true</code> if this <code>ImmutableNode</code> is a <code>ImmutableObjectNode</code>;
     * Returns <code>false</code> otherwise
     *
     * @return <code>true</code> if this <code>ImmutableNode</code> is a <code>ImmutableObjectNode</code>
     */
    default boolean isObjectNode() {
        return false;
    }

    /**
     * Indicates whether this <code>ImmutableNode</code> is a <code>ImmutableObjectNode</code> or not.
     * Returns <code>true</code> if this <code>ImmutableNode</code> is a <code>ImmutableObjectNode</code>;
     * Returns <code>false</code> otherwise
     *
     * @return <code>true</code> if this <code>ImmutableNode</code> is a <code>ImmutableObjectNode</code>
     */
    default boolean isArrayNode() {
        return false;
    }

    /**
     * Returns the primary node type in effect for this node. Which
     * <code>NodeType</code> is returned when this method is called on the root
     * node of a workspace is up to the implementation.
     *
     * @return a <code>NodeType</code> object.
     * @throws RepositoryException if an error occurs
     */
    @Nonnull
    TypeDefinition getTypeDefinition();

    /**
     * Accepts an <code>ImmutableItemVisitor</code>. Calls the appropriate
     * <code>ImmutableItemVisitor</code> <code>visit</code> method of the according to
     * whether <i>this</i> <code>ImmutableNode</code> is a <code>ImmutableObjectNode</code> or a
     * <code>ImmutableProperty</code>.
     *
     * @param visitor The ImmutableItemVisitor to be accepted.
     * @throws RepositoryException if an error occurs.
     */
    void accept(@Nonnull ImmutableItemVisitor visitor);

    @Override
    @Nullable
    default String asString() {
        Object objValue = getValue();
        return objValue instanceof String ? (String) objValue : null;
    }

    @Override
    @Nullable
    default long asLong() throws NumberFormatException {
        return Long.parseLong(asString());
    }

    @Override
    @Nullable
    default double asDouble() throws NumberFormatException {
        return Double.parseDouble(asString());
    }

    @Override
    @Nullable
    default BigDecimal asDecimal() throws NumberFormatException {
        return BigDecimal.valueOf(asDouble());
    }

    @Override
    @Nullable
    default LocalDate asDate() {
        return LocalDate.parse(asString(), DateTimeFormatter.ISO_DATE);
    }

    @Override
    @Nullable
    default LocalDateTime asDateTime() {
        return LocalDateTime.parse(asString(), DateTimeFormatter.ISO_DATE_TIME);
    }


    @Override
    @Nullable
    default boolean asBoolean() {
        return Boolean.valueOf(asString());
    }

    @Override
    default ImmutableBinaryValue asBinaryValue() {
        return (ImmutableBinaryValue) this;
    }

    @Override
    default ImmutableObjectNode asObjectNode() {
        return (ImmutableObjectNode) this;
    }

    @Override
    default ImmutableArrayNode asArrayNode() {
        return (ImmutableArrayNode) this;
    }
}