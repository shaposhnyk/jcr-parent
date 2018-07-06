/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package javax.jcr.api;

import javax.jcr.api.exceptions.RepositoryException;
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
     * Reads successive bytes from the specified <code>position</code> in this
     * <code>ImmutableBinaryValue</code> into the passed byte array until either the byte
     * array is full or the end of the <code>ImmutableBinaryValue</code> is encountered.
     * <p>
     * If {@link #dispose()} has been called on this <code>ImmutableBinaryValue</code>
     * object, then this method will throw the runtime exception
     * {@link IllegalStateException}.
     *
     * @param b        the buffer into which the data is read.
     * @param position the position in this ImmutableBinaryValue from which to start reading
     *                 bytes.
     * @return the number of bytes read into the buffer, or -1 if there is no
     * more data because the end of the ImmutableBinaryValue has been reached.
     * @throws IOException              if an I/O error occurs.
     * @throws NullPointerException     if b is null.
     * @throws IllegalArgumentException if offset is negative.
     * @throws RepositoryException      if another error occurs.
     */
    int read(byte[] b, long position) throws IOException;

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