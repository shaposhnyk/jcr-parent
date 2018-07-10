/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api;

import com.ljcr.api.exceptions.RepositoryException;

import javax.annotation.Nullable;

/**
 * This interface defines two signatures of the <code>visit</code> method; one
 * taking a <code>ImmutableObjectNode</code>, the other a <code>ImmutableProperty</code>. When an object
 * implementing this interface is passed to <code>{@link ImmutableNode#accept(ImmutableItemVisitor
 * visitor)}</code> the appropriate <code>visit</code> method is automatically
 * called, depending on whether the <code>ImmutableNode</code> in question is a
 * <code>ImmutableObjectNode</code> or a <code>ImmutableProperty</code>. Different implementations of
 * this interface can be written for different purposes. It is, for example,
 * possible for the <code>{@link #visit(ImmutableObjectNode node)}</code> method to call
 * <code>accept</code> on the children of the passed node and thus recurse
 * through the tree performing some operation on each <code>ImmutableNode</code>.
 */
public interface ImmutableItemVisitor {

    /**
     * This method is called when the <code>ImmutableItemVisitor</code> is passed to the
     * <code>accept</code> method of a <code>ImmutableProperty</code>. If this method
     * throws an exception the visiting process is aborted.
     *
     * @param property The <code>ImmutableProperty</code> that is accepting this
     *                 visitor.
     * @throws RepositoryException if an error occurs
     */
    void visit(@Nullable ImmutableNode property);

    /**
     * This method is called when the <code>ImmutableItemVisitor</code> is passed to the
     * <code>accept</code> method of a <code>ImmutableObjectNode</code>. If this method throws
     * an exception the visiting process is aborted.
     *
     * @param node The <code>ImmutableObjectNode</code that is accepting this visitor.
     * @throws RepositoryException if an error occurs
     */
    void visit(@Nullable ImmutableObjectNode node);

    /**
     * This method is called when the <code>ImmutableItemVisitor</code> is passed to the
     * <code>accept</code> method of a <code>ImmutableObjectNode</code>. If this method throws
     * an exception the visiting process is aborted.
     *
     * @param node The <code>ImmutableObjectNode</code that is accepting this visitor.
     * @throws RepositoryException if an error occurs
     */
    void visit(@Nullable ImmutableArrayNode node);
}