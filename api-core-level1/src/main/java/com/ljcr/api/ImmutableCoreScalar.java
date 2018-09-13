/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import com.ljcr.api.definitions.TypeDefinition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A generic holder for the value of a repository item.
 * A <code>ImmutableScalar</code> object can
 * be used without knowing the actual property type (<code>STRING</code>,
 * <code>DOUBLE</code>, <code>BINARY</code> etc.).
 * <p>
 * The equality comparison must not change the state of the <code>ImmutableScalar</code> instances.
 */
@Nonnull
public interface ImmutableCoreScalar {
    /**
     * @return type definition of the value
     */
    @Nonnull
    TypeDefinition getTypeDefinition();

    /**
     * @return native representaion of the value
     */
    @Nullable
    Object getValue();
}