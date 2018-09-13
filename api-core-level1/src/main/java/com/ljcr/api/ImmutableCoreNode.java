package com.ljcr.api;


import com.ljcr.api.exceptions.RepositoryException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The <code>ImmutableNode</code> is the base read-only value item of hierarchical key-value repository.
 * It can any of these: Scalar, Object, or Array (Container)
 */
@Nonnull
public interface ImmutableCoreNode<T extends ImmutableCoreNode>
        extends
        ImmutableCoreScalar,
        ImmutableCoreContainer,
        ImmutableCoreObject {
    /**
     * Indicates whether this <code>ImmutableCoreNode</code> is a <code>ImmutableCoreObject</code> or not.
     *
     * @return <code>true</code> if this <code>ImmutableCoreNode</code> is a <code>ImmutableCoreObject</code>
     */
    boolean isObjectNode();

    /**
     * Indicates whether this <code>ImmutableCoreNode</code> is a <code>ImmutableCoreContainer</code> or not.
     *
     * @return <code>true</code> if this <code>ImmutableCoreNode</code> is a <code>ImmutableObjectNode</code>
     */
    boolean isArrayNode();

    /**
     * Indicates whether this <code>ImmutableCoreNode</code> is a <code>ImmutableCoreScalar</code> or not.
     *
     * @return <code>true</code> if this <code>ImmutableCoreNode</code> is a <code>ImmutableCoreScalar</code>
     */
    boolean isScalarNode();

    /**
     * Accepts an <code>ImmutableItemVisitor</code>. Calls the appropriate
     * <code>ImmutableItemVisitor</code> <code>visit</code> method of the according to
     * whether <i>this</i> <code>ImmutableNode</code> is a <code>ImmutableObjectNode</code> or a
     * <code>ImmutableProperty</code>.
     *
     * @param visitor The ImmutableItemVisitor to be accepted.
     * @throws RepositoryException if an error occurs.
     */
    @Nullable
    <T> T accept(@Nonnull ImmutableItemVisitor<T> visitor);
}