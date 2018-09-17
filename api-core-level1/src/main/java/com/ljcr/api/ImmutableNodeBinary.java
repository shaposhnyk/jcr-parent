/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import com.ljcr.api.exceptions.RepositoryException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.InputStream;

/**
 * A <code>ImmutableNodeBinary</code> object holds a JCR property value of type
 * <code>BINARY</code>. The <code>ImmutableNodeBinary</code> interface and the related
 * methods in {@link ImmutableProperty}, {@link ImmutableNodeScalar} and {@link ValueFactory} replace
 * the deprecated {@link ImmutableNodeScalar#getStream} and {@link ImmutableProperty#getStream}
 * methods.
 *
 * @since JCR 2.0
 */
@Nonnull
public interface ImmutableNodeBinary extends ImmutableNodeScalar {

    /**
     * Implementation specific
     *
     * @return a handler corresponding to this binary value
     */
    @Override
    @Nullable
    Object getValue();

    /**
     * @return object's size if known or -1 otherwise
     */
    long size();

    /**
     * Returns an {@link InputStream} representation of this value. Each call to
     * <code>getStream()</code> returns a new stream. The API consumer is
     * responsible for calling <code>close()</code> on the returned stream.
     * <p>
     *
     * @return New InputStream of this value.
     * @throws RepositoryException if an error occurs.
     */
    InputStream newInputStream();
}