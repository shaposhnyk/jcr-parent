/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import com.ljcr.api.exceptions.RepositoryException;

import java.io.InputStream;
import java.io.IOException;

/**
 * A <code>ImmutableBinaryValue</code> object holds a JCR property value of type
 * <code>BINARY</code>. The <code>ImmutableBinaryValue</code> interface and the related
 * methods in {@link ImmutableProperty}, {@link ImmutableValue} and {@link ValueFactory} replace
 * the deprecated {@link ImmutableValue#getStream} and {@link ImmutableProperty#getStream}
 * methods.
 *
 * @since JCR 2.0
 */
public interface ImmutableBinaryValue {

    /**
     * Returns an {@link InputStream} representation of this value. Each call to
     * <code>getStream()</code> returns a new stream. The API consumer is
     * responsible for calling <code>close()</code> on the returned stream.
     * <p>
     * If {@link #dispose()} has been called on this <code>ImmutableBinaryValue</code>
     * object, then this method will throw the runtime exception
     * {@link IllegalStateException}.
     *
     * @return A stream representation of this value.
     * @throws RepositoryException if an error occurs.
     */
    InputStream getStream();

    /**
     * Returns the size of this <code>ImmutableBinaryValue</code> value in bytes.
     * <p>
     * If {@link #dispose()} has been called on this <code>ImmutableBinaryValue</code>
     * object, then this method will throw the runtime exception
     * {@link IllegalStateException}.
     *
     * @return the size of this value in bytes.
     * @throws RepositoryException if an error occurs.
     */
    long size();

    /**
     * Releases all resources associated with this <code>ImmutableBinaryValue</code> object
     * and informs the repository that these resources may now be reclaimed.
     * An application should call this method when it is finished with the
     * <code>ImmutableBinaryValue</code> object.
     */
    void dispose();
}