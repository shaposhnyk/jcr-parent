package com.ljcr.api;


import javax.annotation.Nonnull;

/**
 * The <code>ImmutableNode</code> is the base read-only value item of hierarchical key-value repository.
 * It can any of these: Scalar, Object, or Array (Container)
 */
@Nonnull
public interface ImmutableCoreNode<T extends ImmutableCoreNode<T>>
        extends
        ImmutableCoreScalar,
        ImmutableCoreObject<T>,
        ImmutableCoreContainer<T>,
        ImmutableCoreReferencable<T> {
    /**
     * Indicates whether this <code>ImmutableCoreNode</code> is a <code>ImmutableCoreObject</code> or not.
     *
     * @return <code>true</code> if this <code>ImmutableCoreNode</code> is a <code>ImmutableCoreObject</code>
     */
    boolean isObject();

    /**
     * Indicates whether this <code>ImmutableCoreNode</code> is a <code>ImmutableCoreContainer</code> or not.
     *
     * @return <code>true</code> if this <code>ImmutableCoreNode</code> is a <code>ImmutableNodeObject</code>
     */
    boolean isCollection();

    /**
     * Indicates whether this <code>ImmutableCoreNode</code> is a <code>ImmutableCoreScalar</code> or not.
     *
     * @return <code>true</code> if this <code>ImmutableCoreNode</code> is a <code>ImmutableCoreScalar</code>
     */
    boolean isScalarValue();
}