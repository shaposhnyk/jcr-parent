/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import javax.annotation.Nonnull;

/**
 * The <code>ImmutableCoreReferencable</code> interface represents a read-only referencable node in a repository.
 */
@Nonnull
public interface ImmutableCoreReferencable<T extends ImmutableCoreNode<T>> extends ImmutableCoreObject<T> {
    @Nonnull
    String getName();
}