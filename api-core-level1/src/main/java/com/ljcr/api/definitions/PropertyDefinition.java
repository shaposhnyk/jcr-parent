/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api.definitions;

/**
 * Superclass of {@link TypeDefinition}
 */
public interface PropertyDefinition extends TypeDefinition {
    /**
     * Reports whether the item is to be automatically created when its parent
     * node is created. If <code>true</code>, then this <code>PropertyDefinition</code>
     * will necessarily not be a residual set definition but will specify an
     * actual item name (in other words getReference() will not return "*").
     * <p>
     * An autocreated non-protected item must be created immediately when its
     * parent node is created in the transient session space. Creation of
     * autocreated non-protected items is never delayed until
     * <code>save</code>.
     * <p>
     * <p>
     * An autocreated protected item should be created immediately when its
     * parent node is created in the transient session space. Creation of
     * autocreated protected items should not be delayed until
     * <code>save</code>, though doing so does not violate JCR compliance.
     * <p>
     * In implementations that support node type registration, if this
     * <code>PropertyDefinition</code> object is actually a newly-created empty
     * <code>PropertyDefinitionTemplate</code> or <code>NodeDefinitionTemplate</code>,
     * then this method will return <code>false</code>.
     *
     * @return a <code>boolean</code>.
     */
    default boolean isAutoCreated() {
        return false;
    }

    /**
     * Reports whether the item is mandatory. A mandatory item is one that, if
     * its parent node exists, must also exist.
     * <p>
     * This means that a mandatory single-value property must have a value
     * (since there is no such thing a <code>null</code> value). In the case of
     * multi-value properties this means that the property must exist, though it
     * can have zero or more values.
     * <p>
     * An attempt to save a node that has a mandatory child item without first
     * creating that child item  will throw a <code>ConstraintViolationException</code>
     * on <code>save</code>.
     * <p>
     * In implementations that support node type registration, if this
     * <code>PropertyDefinition</code> object is actually a newly-created empty
     * <code>PropertyDefinitionTemplate</code> or <code>NodeDefinitionTemplate</code>,
     * then this method will return <code>false</code>.
     * <p>
     * An item definition cannot be both residual and mandatory.
     *
     * @return a <code>boolean</code>
     */
    default boolean isMandatory() {
        return false;
    }

    default boolean isFullTextSearchable() {
        return false;
    }

    /**
     * Gets the <code>OnParentVersion </code> status of the child item. This
     * governs what occurs (in implementations that support versioning) when the
     * parent node of this item is checked-in. One of: <ul>
     * <li><code>OnParentVersionActions.COPY</code></li>
     * <li><code>OnParentVersionActions.VERSION</code></li>
     * <li><code>OnParentVersionActions.IGNORE</code></li>
     * <li><code>OnParentVersionActions.INITIALIZE</code></li>
     * <li><code>OnParentVersionActions.COMPUTE</code></li>
     * <li><code>OnParentVersionActions.ABORT</code></li> </ul>
     * <p>
     * In implementations that support node type registration, if this
     * <code>PropertyDefinition</code> object is actually a newly-created empty
     * <code>PropertyDefinitionTemplate</code> or <code>NodeDefinitionTemplate</code>,
     * then this method will return <code>OnParentVersionAction.COPY</code>.
     *
     * @return a <code>int</code> constant member of {@link
     * OnParentVersionAction}.
     */
    default OnParentVersionAction getOnParentVersion() {
        return OnParentVersionActions.COPY;
    }
}
