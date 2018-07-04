package javax.jcr.api;


import javax.jcr.api.exceptions.AccessDeniedException;
import javax.jcr.api.exceptions.InvalidItemStateException;
import javax.jcr.api.exceptions.ItemNotFoundException;
import javax.jcr.api.exceptions.RepositoryException;

/**
 * The <code>ImmutableItem</code> is the base read-only part of <code>{@link ImmutableNode}</code> and
 * <code>{@link ImmutableProperty}</code>.
 */
interface ImmutableItem {

    /**
     * Returns the normalized absolute path to this item.
     *
     * @return the normalized absolute path of this <code>ImmutableItem</code>.
     * @throws RepositoryException if an error occurs.
     */
    String getPath();

    /**
     * Returns the name of this <code>ImmutableItem</code> in qualified form. If this
     * <code>ImmutableItem</code> is the root node of the workspace, an empty string is
     * returned.
     *
     * @return the name of this <code>ImmutableItem</code> in qualified form or an empty
     * string if this <code>ImmutableItem</code> is the root node of a workspace.
     * @throws RepositoryException if an error occurs.
     */
    String getName();

    /**
     * Returns the ancestor of this <code>ImmutableItem</code> at the specified depth. An
     * ancestor of depth <i>x</i> is the <code>ImmutableItem</code> that is <i>x</i>
     * levels down along the path from the root node to <i>this</i>
     * <code>ImmutableItem</code>. <ul> <li><i>depth</i> = 0 returns the root node of a
     * workspace. <li><i>depth</i> = 1 returns the child of the root node along
     * the path to <i>this</i> <code>ImmutableItem</code>. <li><i>depth</i> = 2 returns
     * the grandchild of the root node along the path to <i>this</i>
     * <code>ImmutableItem</code>. <li>And so on to <i>depth</i> = <i>n</i>, where
     * <i>n</i> is the depth of <i>this</i> <code>ImmutableItem</code>, which returns
     * <i>this</i> <code>ImmutableItem</code> itself. </ul>
     * <p>
     * If this node has more than one path (i.e., if it is a descendant of a
     * shared node) then the path used to define the ancestor is
     * implementaion-dependent.
     *
     * @param depth An integer, 0 &lt;= <i>depth</i> &lt;= <i>n</i> where
     *              <i>n</i> is the depth of <i>this</i> <code>ImmutableItem</code>.
     * @return The ancestor of this <code>ImmutableItem</code> at the specified
     * <code>depth</code>.
     * @throws ItemNotFoundException if <i>depth</i> &lt; 0 or <i>depth</i> &gt;
     *                               <i>n</i> where <i>n</i> is the is the depth of this item.
     * @throws AccessDeniedException if the current session does not have
     *                               sufficent access to retrieve the specified node.
     * @throws RepositoryException if an error occurs.
     */
    ImmutableItem getAncestor(int depth) throws ItemNotFoundException, AccessDeniedException;

    /**
     * Returns the parent of this <code>ImmutableItem</code>.
     *
     * @return The parent of this <code>ImmutableItem</code>.
     * @throws ItemNotFoundException if this <code>ImmutableItem</code> is the root node
     *                               of a workspace.
     * @throws AccessDeniedException if the current session does not have
     *                               sufficent access to retrieve the parent of this item.
     * @throws RepositoryException   if another error occurs.
     */
    ImmutableNode getParent() throws ItemNotFoundException, AccessDeniedException;

    /**
     * Returns the depth of this <code>ImmutableItem</code> in the workspace item graph.
     * <ul> <li>The root node returns 0. <li>A property or child node of the
     * root node returns 1. <li>A property or child node of a child node of the
     * root returns 2. <li>And so on to <i>this</i> <code>ImmutableItem</code>. </ul>
     *
     * @return The depth of this <code>ImmutableItem</code> in the workspace item graph.
     * @throws RepositoryException if an error occurs.
     */
    int getDepth();

    /**
     * Returns the <code>Session</code> through which this <code>ImmutableItem</code> was
     * acquired.
     *
     * @return the <code>Session</code> through which this <code>ImmutableItem</code> was
     * acquired.
     * @throws RepositoryException if an error occurs.
     */
    Session getSession();

    /**
     * Indicates whether this <code>ImmutableItem</code> is a <code>ImmutableNode</code> or a
     * <code>ImmutableProperty</code>. Returns <code>true</code> if this
     * <code>ImmutableItem</code> is a <code>ImmutableNode</code>; Returns <code>false</code> if
     * this <code>ImmutableItem</code> is a <code>ImmutableProperty</code>.
     *
     * @return <code>true</code> if this <code>ImmutableItem</code> is a
     * <code>ImmutableNode</code>, <code>false</code> if it is a
     * <code>ImmutableProperty</code>.
     */
    boolean isNode();

    /**
     * Returns <code>true</code> if this <code>ImmutableItem</code> object (the Java
     * object instance) represents the same actual workspace item as the object
     * <code>otherItem</code>.
     * <p>
     * Two <code>ImmutableItem</code> objects represent the same workspace item if and
     * only if all the following are true: <ul> <li>Both objects were acquired
     * through <code>Session</code> objects that were created by the same
     * <code>Repository</code> object.</li> <li>Both objects were acquired
     * through <code>Session</code> objects bound to the same repository
     * workspace.</li> <li>The objects are either both <code>ImmutableNode</code> objects
     * or both <code>ImmutableProperty</code> objects.</li> <li>If they are
     * <code>ImmutableNode</code> objects, they have the same identifier.</li> <li>If
     * they are <code>ImmutableProperty</code> objects they have identical names and
     * <code>isSame</code> is true of their parent nodes.</li> </ul> This method
     * does not compare the <i>states</i> of the two items. For example, if two
     * <code>ImmutableItem</code> objects representing the same actual workspace item
     * have been retrieved through two different sessions and one has been
     * modified, then this method will still return <code>true</code> when
     * comparing these two objects. Note that if two <code>ImmutableItem</code> objects
     * representing the same workspace item are retrieved through the
     * <i>same</i> session they will always reflect the same state.
     *
     * @param otherItem the <code>ImmutableItem</code> object to be tested for identity
     *                  with this <code>ImmutableItem</code>.
     * @return <code>true</code> if this <code>ImmutableItem</code> object and
     * <code>otherItem</code> represent the same actual repository item;
     * <code>false</code> otherwise.
     * @throws RepositoryException if an error occurs.
     */
    boolean isSame(ImmutableItem otherItem);

    /**
     * Accepts an <code>ImmutableItemVisitor</code>. Calls the appropriate
     * <code>ImmutableItemVisitor</code> <code>visit</code> method of the according to
     * whether <i>this</i> <code>ImmutableItem</code> is a <code>ImmutableNode</code> or a
     * <code>ImmutableProperty</code>.
     *
     * @param visitor The ImmutableItemVisitor to be accepted.
     * @throws RepositoryException if an error occurs.
     */
    void accept(ImmutableItemVisitor visitor);

    /**
     * If <code>keepChanges</code> is <code>false</code>, this method discards
     * all pending changes currently recorded in this <code>Session</code> that
     * apply to this ImmutableItem or any of its descendants (that is, the subgraph
     * rooted at this ImmutableItem)and returns all items to reflect the current saved
     * state. Outside a transaction this state is simple the current state of
     * persistent storage. Within a transaction, this state will reflect
     * persistent storage as modified by changes that have been saved but not
     * yet committed.
     * <p>
     * If <code>keepChanges</code> is true then pending change are not discarded
     * but items that do not have changes pending have their state refreshed to
     * reflect the current saved state, thus revealing changes made by other
     * sessions.
     *
     * @param keepChanges a boolean
     * @throws InvalidItemStateException if this <code>ImmutableItem</code> object
     *                                   represents a workspace item that has been removed (either by this session
     *                                   or another).
     * @throws RepositoryException       if another error occurs.
     */
    void refresh(boolean keepChanges) throws InvalidItemStateException;
}