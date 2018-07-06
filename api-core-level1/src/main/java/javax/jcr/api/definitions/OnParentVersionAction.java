package javax.jcr.api.definitions;

public interface OnParentVersionAction {

    /**
     * Returns the name of the node type.
     * <p>
     * In implementations that support node type registration, if this
     * <code>TypeDefinition</code> object is actually a newly-created empty
     * <code>NodeTypeTemplate</code>, then this method will return
     * <code>null</code>.
     *
     * @return a <code>String</code>
     * @since JCR 2.0 moved here from JCR 1.0 <code>NodeType</code>.
     */
    String getIdentifier();
}
