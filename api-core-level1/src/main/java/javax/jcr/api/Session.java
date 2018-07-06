/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package javax.jcr.api;

import javax.jcr.api.exceptions.*;
import javax.security.auth.login.LoginException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

/**
 * The <code>Session</code> object provides read and (in level 2) write access
 * to the content of a particular workspace in the repository.
 * <p>
 * The <code>Session</code> object is returned by {@link
 * Repository#login(Credentials, String) Repository.login()}. It encapsulates
 * both the authorization settings of a particular user (as specified by the
 * passed <code>Credentials</code>) and a binding to the workspace specified by
 * the <code>workspaceName</code> passed on <code>login</code>.
 * <p>
 * Each <code>Session</code> object is associated one-to-one with a
 * <code>Workspace</code> object. The <code>Workspace</code> object represents a
 * "view" of an actual repository workspace entity as seen through the
 * authorization settings of its associated <code>Session</code>.
 */
interface Session {

    /**
     * Gets the user ID associated with this <code>Session</code>. How the user
     * ID is set is up to the implementation, it may be a string passed in as
     * part of the credentials or it may be a string acquired in some other way.
     * This method is free to return an "anonymous user ID" or
     * <code>null</code>.
     *
     * @return the user ID associated with this <code>Session</code>.
     */
    String getUserID();

    /**
     * Returns the names of the attributes set in this session as a result of
     * the <code>Credentials</code> that were used to acquire it. Not all
     * <code>Credentials</code> implementations will contain attributes (though,
     * for example, <code>SimpleCredentials</code> does allow for them). This
     * method returns an empty array if the <code>Credentials</code> instance
     * did not provide attributes.
     *
     * @return A string array containing the names of all attributes passed in
     * the credentials used to acquire this session.
     */
    default Collection<String> getAttributeNames() {
        return Collections.emptyList();
    }

    /**
     * Returns the value of the named attribute as an <code>Object</code>, or
     * <code>null</code> if no attribute of the given name exists. See {@link
     * Session#getAttributeNames}.
     *
     * @param name the name of an attribute passed in the credentials used to
     *             acquire this session.
     * @return the value of the attribute or <code>null</code> if no attribute
     * of the given name exists.
     */
    default Object getAttribute(String name) {
        return null;
    }

    /**
     * Returns the <code>Workspace</code> attached to this
     * <code>Session</code>.
     *
     * @return a <code>{@link Workspace}</code> object.
     */
    Workspace getWorkspace();

    /**
     * Returns the root node of the workspace, "/". This node is the main access
     * point to the content of the workspace.
     *
     * @return The root node of the workspace: a <code>{@link ImmutableObjectNode}</code>
     * object.
     * @throws RepositoryException if an error occurs.
     */
    default ImmutableItem getRootNode() {
        return getWorkspace().getRootNode();
    }

    /**
     * Returns a new session in accordance with the specified (new) Credentials.
     * Allows the current user to "impersonate" another using incomplete or
     * relaxed credentials requirements (perhaps including a user name but no
     * password, for example), assuming that this <code>Session</code> gives
     * them that permission.
     * <p>
     * The new <code>Session</code> is tied to a new <code>Workspace</code>
     * instance. In other words, <code>Workspace</code> instances are not
     * re-used. However, the <code>Workspace</code> instance returned represents
     * the same actual persistent workspace entity in the repository as is
     * represented by the <code>Workspace</code> object tied to this
     * <code>Session</code>.
     *
     * @param credentials A <code>Credentials</code> object
     * @return a <code>Session</code> object
     * @throws LoginException      if the current session does not have sufficient
     *                             access to perform the operation.
     * @throws RepositoryException if another error occurs.
     */
    Session impersonate(Credentials credentials) throws LoginException;

    /**
     * Returns the node specified by the given identifier. Applies to both
     * referenceable and non-referenceable nodes.
     *
     * @param id An identifier.
     * @return A <code>ImmutableObjectNode</code>.
     * @throws ItemNotFoundException if no node with the specified identifier
     *                               exists or if this <code>Session<code> does not have read access to the
     *                               node with the specified identifier.
     * @throws RepositoryException   if another error occurs.
     * @since JCR 2.0
     */
    ImmutableObjectNode getNodeByIdentifier(String id) throws ItemNotFoundException;

    /**
     * Returns the node at the specified absolute path in the workspace. If no
     * such node exists, then it returns the property at the specified path.
     * <p>
     * This method should only be used if the application does not know whether
     * the item at the indicated path is property or node. In cases where the
     * application has this information, either {@link #getNode} or {@link
     * #getProperty} should be used, as appropriate. In many repository
     * implementations the node and property-specific methods are likely to be
     * more efficient than <code>getItem</code>.
     *
     * @param absPath An absolute path.
     * @return the specified <code>ImmutableItem</code>.
     * @throws PathNotFoundException if no accessible item is found at the
     *                               specified path.
     * @throws RepositoryException   if another error occurs.
     */
    ImmutableItem getItem(Path absPath) throws PathNotFoundException;

    /**
     * Returns the node at the specified absolute path in the workspace.
     *
     * @param absPath An absolute path.
     * @return the specified <code>ImmutableObjectNode</code>.
     * @throws PathNotFoundException If no accessible node is found at the
     *                               specifed path.
     * @throws RepositoryException   If another error occurs.
     * @since JCR 2.0
     */
    default ImmutableObjectNode getNode(Path absPath) throws PathNotFoundException {
        ImmutableItem item = getItem(absPath);
        if (item instanceof ImmutableObjectNode) {
            return (ImmutableObjectNode) item;
        }
        throw new RepositoryException();
    }

    /**
     * Returns the property at the specified absolute path in the workspace.
     *
     * @param absPath An absolute path.
     * @return the specified <code>ImmutableProperty</code>.
     * @throws PathNotFoundException If no accessible property is found at the
     *                               specified path.
     * @throws RepositoryException   if another error occurs.
     * @since JCR 2.0
     */
    default ImmutableProperty getProperty(Path absPath) throws PathNotFoundException {
        ImmutableItem item = getItem(absPath);
        if (item instanceof ImmutableProperty) {
            return (ImmutableProperty) item;
        }
        throw new RepositoryException();
    }

    /**
     * Returns <code>true</code> if an item exists at <code>absPath</code> and
     * this <code>Session</code> has read access to it; otherwise returns
     * <code>false</code>.
     *
     * @param absPath An absolute path.
     * @return a <code>boolean</code>
     * @throws RepositoryException if <code>absPath</code> is not a well-formed
     *                             absolute path.
     */
    default boolean itemExists(Path absPath) {
        return getItem(absPath) != null;
    }

    /**
     * Returns <code>true</code> if a node exists at <code>absPath</code> and
     * this <code>Session</code> has read access to it; otherwise returns
     * <code>false</code>.
     *
     * @param absPath An absolute path.
     * @return a <code>boolean</code>
     * @throws RepositoryException if <code>absPath</code> is not a well-formed
     *                             absolute path.
     * @since JCR 2.0
     */
    default boolean nodeExists(Path absPath) {
        return getNode(absPath) != null;
    }

    /**
     * Returns <code>true</code> if a property exists at <code>absPath</code>
     * and this <code>Session</code> has read access to it; otherwise returns
     * <code>false</code>.
     *
     * @param absPath An absolute path.
     * @return a <code>boolean</code>
     * @throws RepositoryException if <code>absPath</code> is not a well-formed
     *                             absolute path.
     * @since JCR 2.0
     */
    default boolean propertyExists(Path absPath) {
        return getProperty(absPath) != null;
    }

    /**
     * If <code>keepChanges</code> is <code>false</code>, this method discards
     * all pending changes currently recorded in this <code>Session</code> and
     * returns all items to reflect the current saved state. Outside a
     * transaction this state is simply the current state of persistent storage.
     * Within a transaction, this state will reflect persistent storage as
     * modified by changes that have been saved but not yet committed.
     * <p>
     * If <code>keepChanges</code> is true then pending change are not discarded
     * but items that do not have changes pending have their state refreshed to
     * reflect the current saved state, thus revealing changes made by other
     * sessions.
     *
     * @param keepChanges a boolean
     * @throws RepositoryException if an error occurs.
     */
    default void refresh(boolean keepChanges) {
        throw new UnsupportedRepositoryOperationException();
    }

    /**
     * Returns all prefixes currently mapped to URIs in this
     * <code>Session</code>.
     *
     * @return a string array
     * @throws RepositoryException if an error occurs
     */
    Collection<String> getNamespacePrefixes();

    /**
     * Returns the URI to which the given <code>prefix</code> is mapped as
     * currently set in this <code>Session</code>.
     *
     * @param prefix a string
     * @return a string
     * @throws NamespaceException  if the specified <code>prefix</code> is
     *                             unknown.
     * @throws RepositoryException if another error occurs
     */
    String getNamespaceURI(String prefix) throws NamespaceException;

    /**
     * Returns the prefix to which the given <code>uri</code> is mapped as
     * currently set in this <code>Session</code>.
     *
     * @param uri a string
     * @return a string
     * @throws NamespaceException  if the specified <code>uri</code> is unknown.
     * @throws RepositoryException if another error occurs
     */
    String getNamespacePrefix(String uri) throws NamespaceException;

    /**
     * Releases all resources associated with this <code>Session</code>. This
     * method should be called when a <code>Session</code> is no longer needed.
     */
    void logout();

    /**
     * Returns <code>true</code> if this <code>Session</code> object is usable
     * by the client. Otherwise, returns <code>false</code>. A usable
     * <code>Session</code> is one that is neither logged-out, timed-out nor in
     * any other way disconnected from the repository.
     *
     * @return <code>true</code> if this <code>Session</code> is usable,
     * <code>false</code> otherwise.
     */
    default boolean isLive() {
        return true;
    }

    /**
     * Returns the access control manager for this <code>Session</code>.
     *
     * @return the access control manager for this <code>Session</code>
     * @throws UnsupportedRepositoryOperationException if access control is not
     *                                                 supported.
     * @throws RepositoryException                     if another error occurs.
     * @since JCR 2.0
     */
    default AccessControlManager getAccessControlManager() throws UnsupportedRepositoryOperationException {
        throw new UnsupportedRepositoryOperationException();
    }
}