/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import com.ljcr.api.exceptions.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
 * <code>Repository</code> object. The <code>Repository</code> object represents a
 * "view" of an actual repository workspace entity as seen through the
 * authorization settings of its associated <code>Session</code>.
 */
@Nonnull
interface Session {

    /**
     * Returns the <code>Repository</code> attached to this
     * <code>Session</code>.
     *
     * @return a <code>{@link Repository}</code> object.
     */
    Repository getWorkspace();

    /**
     * Gets the user ID associated with this <code>Session</code>. How the user
     * ID is set is up to the implementation, it may be a string passed in as
     * part of the credentials or it may be a string acquired in some other way.
     * This method is free to return an "anonymous user ID" or
     * <code>null</code>.
     *
     * @return the user ID associated with this <code>Session</code>.
     */
    @Nullable
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
    @Nullable
    default Object getAttribute(@Nonnull String name) {
        return null;
    }

    /**
     * Returns the root node of the workspace, "/". This node is the main access
     * point to the content of the workspace.
     *
     * @return The root node of the workspace: a <code>{@link ImmutableObjectNode}</code>
     * object.
     * @throws RepositoryException if an error occurs.
     */
    default ImmutableNode getRootNode() {
        return getWorkspace().getRootNode();
    }

    /**
     * Returns a new session in accordance with the specified (new) Credentials.
     * Allows the current user to "impersonate" another using incomplete or
     * relaxed credentials requirements (perhaps including a user name but no
     * password, for example), assuming that this <code>Session</code> gives
     * them that permission.
     * <p>
     * The new <code>Session</code> is tied to a new <code>Repository</code>
     * instance. In other words, <code>Repository</code> instances are not
     * re-used. However, the <code>Repository</code> instance returned represents
     * the same actual persistent workspace entity in the repository as is
     * represented by the <code>Repository</code> object tied to this
     * <code>Session</code>.
     *
     * @param credentials A <code>Credentials</code> object
     * @return a <code>Session</code> object
     * @throws LoginException      if the current session does not have sufficient
     *                             access to perform the operation.
     * @throws RepositoryException if another error occurs.
     */
    default Session impersonate(Credentials credentials) throws LoginException {
        return this;
    }

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
     * @return the specified <code>ImmutableNode</code>.
     * @throws PathNotFoundException if no accessible item is found at the
     *                               specified path.
     * @throws RepositoryException   if another error occurs.
     */
    default ImmutableNode getItem(@Nonnull Path absPath) throws PathNotFoundException {
        return getWorkspace().getItem(absPath);
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
    default boolean itemExists(@Nonnull Path absPath) {
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
    default boolean nodeExists(@Nonnull Path absPath) {
        ImmutableNode item = getItem(absPath);
        return item != null && item.isObjectNode();
    }

    /**
     * Returns all prefixes currently mapped to URIs in this
     * <code>Session</code>.
     *
     * @return a string array
     * @throws RepositoryException if an error occurs
     */
    default Collection<String> getNamespacePrefixes() {
        return Collections.emptyList();
    }

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
    @Nullable
    default String getNamespaceURI(@Nonnull String prefix) throws NamespaceException {
        return null;
    }

    /**
     * Returns the prefix to which the given <code>uri</code> is mapped as
     * currently set in this <code>Session</code>.
     *
     * @param uri a string
     * @return a string
     * @throws NamespaceException  if the specified <code>uri</code> is unknown.
     * @throws RepositoryException if another error occurs
     */
    @Nullable
    default String getNamespacePrefix(@Nonnull String uri) throws NamespaceException {
        return null;
    }

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