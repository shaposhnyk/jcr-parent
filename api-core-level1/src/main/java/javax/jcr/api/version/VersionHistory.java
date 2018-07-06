/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package javax.jcr.api.version;

import javax.jcr.api.ImmutableObjectNode;
import javax.jcr.api.exceptions.RepositoryException;
import java.util.stream.Stream;

/**
 * A <code>VersionHistory</code> object wraps an <code>nt:versionHistory</code>
 * node. It provides convenient access to version history information.
 */
public interface VersionHistory extends ImmutableObjectNode {

    /**
     * Returns the identifier of the versionable node for which this is the
     * version history.
     *
     * @return the identifier of the versionable node for which this is the
     * version history.
     * @throws RepositoryException if an error occurs.
     * @since JCR 2.0
     */
    public String getVersionableIdentifier();

    /**
     * This method returns an iterator over all the versions in the <i>line of
     * descent</i> from the root version to that base version within this
     * history <i>that is bound to the workspace through which this
     * <code>VersionHistory</code> was accessed</i>.
     * <p>
     * Within a version history <code>H</code>, <code>B</code> is the base
     * version bound to workspace <code>W</code> if and only if there exists a
     * versionable node <code>N</code> in <code>W</code> whose version history
     * is <code>H</code> and <code>B</code> is the base version of
     * <code>N</code>.
     * <p>
     * The <i>line of descent</i> from version <code>V1</code> to
     * <code>V2</code>, where <code>V2</code> is a successor of <code>V1</code>,
     * is the ordered list of versions starting with <code>V1</code> and
     * proceeding through each direct successor to <code>V2</code>.
     * <p>
     * The versions are returned in order of creation date, from oldest to
     * newest.
     * <p>
     * Note that in a simple versioning repository the behavior of this method
     * is equivalent to returning all versions in the version history in order
     * from oldest to newest.
     *
     * @return a <code>VersionIterator</code> object.
     * @throws RepositoryException if an error occurs.
     */
    public Stream<Version> getAllLinearVersions();

    /**
     * Returns an iterator over all the versions within this version history. If
     * the version graph of this history is linear then the versions are
     * returned in order of creation date, from oldest to newest. Otherwise the
     * order of the returned versions is implementation-dependent.
     *
     * @return a <code>VersionIterator</code> object.
     * @throws RepositoryException if an error occurs.
     */
    public Stream<Version> getAllVersions();
}
