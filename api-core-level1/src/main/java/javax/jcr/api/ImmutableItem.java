package javax.jcr.api;


import javax.jcr.api.definitions.NodeTypeDefinition;
import javax.jcr.api.exceptions.PathNotFoundException;
import javax.jcr.api.exceptions.RepositoryException;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * The <code>ImmutableItem</code> is the base read-only part of <code>{@link ImmutableObjectNode}</code> and
 * <code>{@link ImmutableProperty}</code>.
 */
public interface ImmutableItem {

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
     * Returns the normalized absolute path to this item.
     *
     * @return the normalized absolute path of this <code>ImmutableItem</code>.
     * @throws RepositoryException if an error occurs.
     */
    Path getPath();

    /**
     * Returns the node at <code>fieldName</code> relative to this node.
     * <p>
     * If <code>fieldName</code> contains a path element that refers to a node
     * with same-name sibling nodes without explicitly including an index using
     * the array-style notation (<code>[x]</code>), then the index [1] is
     * assumed (indexing of same name siblings begins at 1, not 0, in order to
     * preserve compatibility with XPath).
     * <p>
     * Within the scope of a single <code>Session</code> object, if a
     * <code>ImmutableObjectNode</code> object has been acquired, any subsequent call of
     * <code>getNode</code> reacquiring the same node must return a
     * <code>ImmutableObjectNode</code> object reflecting the same state as the earlier
     * <code>ImmutableObjectNode</code> object. Whether this object is actually the same
     * <code>ImmutableObjectNode</code> instance, or simply one wrapping the same state, is up
     * to the implementation.
     *
     * @param fieldName The relative path of the node to retrieve.
     * @return The node at <code>fieldName</code>.
     * @throws PathNotFoundException if no node exists at the specified path or
     *                               the current <code>Session</code> does not read access to the node at the
     *                               specified path.
     * @throws RepositoryException   If another error occurs.
     */
    ImmutableItem getItem(String fieldName) throws PathNotFoundException;

    /**
     * All direct children of this node, i.e.
     * - empty stream for a property
     * - all fields for an object
     * - all items for array
     *
     * @return all direct children items of this node
     */
    Stream<ImmutableItem> getItems();

    /**
     * Indicates whether this <code>ImmutableItem</code> is a <code>ImmutableObjectNode</code> or not.
     * Returns <code>true</code> if this <code>ImmutableItem</code> is a <code>ImmutableObjectNode</code>;
     * Returns <code>false</code> otherwise
     *
     * @return <code>true</code> if this <code>ImmutableItem</code> is a <code>ImmutableObjectNode</code>
     */
    default boolean isObjectNode() {
        return false;
    }

    /**
     * Indicates whether this <code>ImmutableItem</code> is a <code>ImmutableObjectNode</code> or not.
     * Returns <code>true</code> if this <code>ImmutableItem</code> is a <code>ImmutableObjectNode</code>;
     * Returns <code>false</code> otherwise
     *
     * @return <code>true</code> if this <code>ImmutableItem</code> is a <code>ImmutableObjectNode</code>
     */
    default boolean isArrayNode() {
        return false;
    }

    /**
     * This method returns the index of this node within the ordered set of its
     * same-name sibling nodes. This index is the one used to address same-name
     * siblings using the square-bracket notation, e.g.,
     * <code>/a[3]/b[4]</code>. Note that the index always starts at 0
     * For nodes that do not have same-name-siblings, this method will always return 0.
     *
     * @return The index of this node within the ordered set of its same-name
     * sibling nodes.
     * @throws RepositoryException if an error occurs.
     */
    default int getIndex() {
        return 0;
    }

    default ImmutableArrayNode asArrayNode() {
        return (ImmutableArrayNode) this;
    }

    default ImmutableObjectNode asNode() {
        return (ImmutableObjectNode) this;
    }

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
     * workspace.</li> <li>The objects are either both <code>ImmutableObjectNode</code> objects
     * or both <code>ImmutableProperty</code> objects.</li> <li>If they are
     * <code>ImmutableObjectNode</code> objects, they have the same identifier.</li> <li>If
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
     * whether <i>this</i> <code>ImmutableItem</code> is a <code>ImmutableObjectNode</code> or a
     * <code>ImmutableProperty</code>.
     *
     * @param visitor The ImmutableItemVisitor to be accepted.
     * @throws RepositoryException if an error occurs.
     */
    void accept(ImmutableItemVisitor visitor);

    // type definition methods

    /**
     * Returns the primary node type in effect for this node. Which
     * <code>NodeType</code> is returned when this method is called on the root
     * node of a workspace is up to the implementation.
     *
     * @return a <code>NodeType</code> object.
     * @throws RepositoryException if an error occurs
     */
    NodeTypeDefinition getDefinition();
}