/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api.definitions;

import javax.annotation.Nonnull;

/**
 * Superclass of {@link TypeDefinition}
 */
@Nonnull
public interface PropertyDefinition {

    String getIdentifier();

    TypeDefinition getType();

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
     * @return true if field is used as resource identifier
     */
    default boolean isIdentifier() {
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
