/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package javax.jcr.api;

import javax.jcr.api.exceptions.RepositoryException;

/**
 * A <code>Workspace</code> object represents a view onto a persitent workspace
 * within a repository. This view is defined by the authorization settings of
 * the <code>Session</code> object associated with the <code>Workspace</code>
 * object. Each <code>Workspace</code> object is associated one-to-one with a
 * <code>Session</code> object. The <code>Workspace</code> object can be
 * acquired by calling <code>{@link Session#getWorkspace()}</code> on the
 * associated <code>Session</code> object.
 */
public interface Workspace {

    /**
     * Returns the name of the actual persistent workspace represented by this
     * <code>Workspace</code> object. This the name used in
     * <code>Repository.login</code>.
     *
     * @return the name of this workspace.
     */
    public String getName();

    /**
     * Returns the root node of the workspace, "/". This node is the main access
     * point to the content of the workspace.
     *
     * @return The root node of the workspace: a <code>{@link ImmutableItem}</code>
     * object.
     * @throws RepositoryException if an error occurs.
     */
    ImmutableItem getRootNode();
}