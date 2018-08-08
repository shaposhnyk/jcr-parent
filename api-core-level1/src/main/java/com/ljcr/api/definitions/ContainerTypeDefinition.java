/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api.definitions;

import com.ljcr.api.ImmutableObjectNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * The <code>TypeDefinition</code> interface provides methods for
 * discovering the static definition of a node type. These are accessible both
 * before and after the node type is registered. Its subclass
 * <code>NodeType</code> adds methods that are relevant only when the node type
 * is "live"; that is, after it has been registered. Note that the separate
 * <code>NodeDefinition</code> interface only plays a significant role in
 * implementations that support node type registration. In those cases it serves
 * as the superclass of both <code>NodeType</code> and <code>NodeTypeTemplate</code>.
 * In implementations that do not support node type registration, only objects
 * implementing the subinterface <code>NodeType</code> will be encountered.
 *
 * @since JCR 2.0
 */
@Nonnull
public interface ContainerTypeDefinition extends TypeDefinition {
    @Nullable
    ImmutableObjectNode findByReference(String id);

    Stream<ImmutableObjectNode> getItems();
}