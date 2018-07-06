/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package javax.jcr.api.version;

import javax.jcr.api.ImmutableObjectNode;
import javax.jcr.api.exceptions.RepositoryException;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * A <code>Version</code> object wraps an <code>nt:version</code> node. It
 * provides convenient access to version information.
 */
public interface Version extends ImmutableObjectNode {

    /**
     * Returns the <code>VersionHistory</code> that contains this
     * <code>Version</code>.
     *
     * @return the <code>VersionHistory</code> that contains this
     * <code>Version</code>.
     * @throws RepositoryException if an error occurs.
     */
    VersionHistory getContainingHistory();

    /**
     * Returns the date this version was created. This corresponds to the value
     * of the <code>jcr:created</code> property in the <code>nt:version</code>
     * node that represents this version.
     *
     * @return a <code>Calendar</code> object
     * @throws RepositoryException if an error occurs.
     */
    LocalDateTime getCreated();

    /**
     * Assuming that this <code>Version</code> object was acquired through a
     * <code>Workspace</code> <code>W</code> and is within the
     * <code>VersionHistory</code> <code>H</code>, this method returns the
     * successor of this version along the same line of descent as is returned
     * by <code>H.getAllLinearVersions()</code> where <code>H</code> was also
     * acquired through <code>W</code>.
     * <p>
     * Note that under simple versioning the behavior of this method is
     * equivalent to getting the unique successor (if any) of this version.
     *
     * @return a <code>Version</code> or <code>null</code> if no linear
     * successor exists.
     * @throws RepositoryException if an error occurs.
     * @see VersionHistory#getAllLinearVersions
     */
    Version getLinearSuccessor();

    /**
     * Returns the successor versions of this version. This corresponds to
     * returning all the <code>nt:version</code> nodes referenced by the
     * <code>jcr:successors</code> multi-value property in the
     * <code>nt:version</code> node that represents this version.
     * <p>
     * In a simple versioning repository this method
     *
     * @return a <code>Version</code> array.
     * @throws RepositoryException if an error occurs.
     */
    Collection<Version> getSuccessors();

    /**
     * Assuming that this <code>Version</code> object was acquired through a
     * <code>Workspace</code> <code>W</code> and is within the
     * <code>VersionHistory</code> <code>H</code>, this method returns the
     * predecessor of this version along the same line of descent as is returned
     * by <code>H.getAllLinearVersions()</code> where <code>H</code> was also
     * acquired through <code>W</code>.
     * <p>
     * Note that under simple versioning the behavior of this method is
     * equivalent to getting the unique predecessor (if any) of this version.
     *
     * @return a <code>Version</code> or <code>null</code> if no linear
     * predecessor exists.
     * @throws RepositoryException if an error occurs.
     * @see VersionHistory#getAllLinearVersions
     */
    Version getLinearPredecessor();

    /**
     * In both simple and full versioning repositories, this method returns the
     * predecessor versions of this version. This corresponds to returning all
     * the <code>nt:version</code> nodes whose <code>jcr:successors</code>
     * property includes a reference to the <code>nt:version</code> node that
     * represents this version.
     *
     * @return a <code>Version</code> array.
     * @throws RepositoryException if an error occurs.
     */
    Collection<Version> getPredecessors();
}