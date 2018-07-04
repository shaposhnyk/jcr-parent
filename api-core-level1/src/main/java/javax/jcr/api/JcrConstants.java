/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package javax.jcr.api;

import javax.jcr.api.definitions.NodeType;

/**
 * JCR Constants
 */
public final class JcrConstants {

    private JcrConstants() {
        // Private constructor to prevent instantiation.
    }

    /**
     * A constant for the property name <code>jcr:primaryType</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_BASE
     * nt:base}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_PRIMARY_TYPE = "{http://www.jcp.org/jcr/1.0}primaryType";

    /**
     * A constant for the property name <code>jcr:mixinTypes</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_BASE
     * nt:base}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_MIXIN_TYPES = "{http://www.jcp.org/jcr/1.0}mixinTypes";

    /**
     * A constant for the property name <code>jcr:content</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_LINKED_FILE
     * nt:linkedFile}. Note, <code>jcr:content</code> is also the name of a
     * child node declared in {@link javax.jcr.nodetype.NodeType#NT_FILE
     * nt:file}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_CONTENT = "{http://www.jcp.org/jcr/1.0}content";

    /**
     * A constant for the property name <code>jcr:data</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_RESOURCE
     * nt:resource}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_DATA = "{http://www.jcp.org/jcr/1.0}data";

    /**
     * A constant for the property name <code>jcr:protocol</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_ADDRESS
     * nt:address}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_PROTOCOL = "{http://www.jcp.org/jcr/1.0}protocol";

    /**
     * A constant for the property name <code>jcr:host</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_ADDRESS
     * nt:address}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_HOST = "{http://www.jcp.org/jcr/1.0}host";

    /**
     * A constant for the property name <code>jcr:port</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_ADDRESS
     * nt:address}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_PORT = "{http://www.jcp.org/jcr/1.0}port";

    /**
     * A constant for the property name <code>jcr:repository</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_ADDRESS
     * nt:address}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_REPOSITORY = "{http://www.jcp.org/jcr/1.0}repository";

    /**
     * A constant for the property name <code>jcr:workspace</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_ADDRESS
     * nt:address}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_WORKSPACE = "{http://www.jcp.org/jcr/1.0}workspace";

    /**
     * A constant for the property name <code>jcr:path</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_ADDRESS
     * nt:address}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_PATH = "{http://www.jcp.org/jcr/1.0}path";

    /**
     * A constant for the property name <code>jcr:id</code> (in expanded form),
     * declared in node type {@link javax.jcr.nodetype.NodeType#NT_ADDRESS
     * nt:address}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_ID = "{http://www.jcp.org/jcr/1.0}id";

    /**
     * A constant for the property name <code>jcr:uuid</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_REFERENCEABLE
     * mix:referenceable}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_UUID = "{http://www.jcp.org/jcr/1.0}uuid";

    /**
     * A constant for the property name <code>jcr:title</code> (in expanded
     * form), declared in node types {@link javax.jcr.nodetype.NodeType#MIX_TITLE
     * mix:title} and {@link javax.jcr.nodetype.NodeType#NT_ACTIVITY
     * nt:activity}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_TITLE = "{http://www.jcp.org/jcr/1.0}title";

    /**
     * A constant for the property name <code>jcr:description</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_TITLE
     * mix:title}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_DESCRIPTION = "{http://www.jcp.org/jcr/1.0}description";

    /**
     * A constant for the property name <code>jcr:created</code> (in expanded
     * form), declared in node types {@link javax.jcr.nodetype.NodeType#MIX_CREATED
     * mix:created} and {@link javax.jcr.nodetype.NodeType#NT_VERSION
     * nt:version}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_CREATED = "{http://www.jcp.org/jcr/1.0}created";

    /**
     * A constant for the property name <code>jcr:createdBy</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_CREATED
     * mix:created}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_CREATED_BY = "{http://www.jcp.org/jcr/1.0}createdBy";

    /**
     * A constant for the property name <code>jcr:lastModified</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_LAST_MODIFIED
     * mix:lastModified}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_LAST_MODIFIED = "{http://www.jcp.org/jcr/1.0}lastModified";

    /**
     * A constant for the property name <code>jcr:lastModifiedBy</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_LAST_MODIFIED
     * mix:lastModified}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_LAST_MODIFIED_BY = "{http://www.jcp.org/jcr/1.0}lastModifiedBy";

    /**
     * A constant for the property name <code>jcr:language</code> (in expanded
     * form), declared in node types {@link javax.jcr.nodetype.NodeType#MIX_LANGUAGE
     * mix:language} and {@link javax.jcr.nodetype.NodeType#NT_QUERY nt:query}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_LANGUAGE = "{http://www.jcp.org/jcr/1.0}language";

    /**
     * A constant for the property name <code>jcr:mimeType</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_MIMETYPE
     * mix:mimeType}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_MIMETYPE = "{http://www.jcp.org/jcr/1.0}mimeType";

    /**
     * A constant for the property name <code>jcr:encoding</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_MIMETYPE
     * mix:mimeType}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_ENCODING = "{http://www.jcp.org/jcr/1.0}encoding";

    /**
     * A constant for the property name <code>jcr:nodeTypeName</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_NODE_TYPE
     * nt:nodeType}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_NODE_TYPE_NAME = "{http://www.jcp.org/jcr/1.0}nodeTypeName";

    /**
     * A constant for the property name <code>jcr:supertypes</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_NODE_TYPE
     * nt:nodeType}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_SUPERTYPES = "{http://www.jcp.org/jcr/1.0}supertypes";

    /**
     * A constant for the property name <code>jcr:isAbstract</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_NODE_TYPE
     * nt:nodeType}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_IS_ABSTRACT = "{http://www.jcp.org/jcr/1.0}isAbstract";

    /**
     * A constant for the property name <code>jcr:isMixin</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_NODE_TYPE
     * nt:nodeType}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_IS_MIXIN = "{http://www.jcp.org/jcr/1.0}isMixin";

    /**
     * A constant for the property name <code>jcr:hasOrderableChildNodes</code>
     * (in expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_NODE_TYPE
     * nt:nodeType}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_HAS_ORDERABLE_CHILD_NODES = "{http://www.jcp.org/jcr/1.0}hasOrderableChildNodes";

    /**
     * A constant for the property name <code>jcr:primaryItemName</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_NODE_TYPE
     * nt:nodeType}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_PRIMARY_ITEM_NAME = "{http://www.jcp.org/jcr/1.0}primaryItemName";

    /**
     * A constant for the property name <code>jcr:name</code> (in expanded
     * form), declared in node types {@link javax.jcr.nodetype.NodeType#NT_PROPERTY_DEFINITION
     * nt:propertyDefinition} and {@link javax.jcr.nodetype.NodeType#NT_CHILD_NODE_DEFINITION
     * nt:childNodeDefinition}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_NAME = "{http://www.jcp.org/jcr/1.0}name";

    /**
     * A constant for the property name <code>jcr:autoCreated</code> (in
     * expanded form), declared in node types {@link javax.jcr.nodetype.NodeType#NT_PROPERTY_DEFINITION
     * nt:propertyDefinition} and {@link javax.jcr.nodetype.NodeType#NT_CHILD_NODE_DEFINITION
     * nt:childNodeDefinition}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_AUTOCREATED = "{http://www.jcp.org/jcr/1.0}autoCreated";

    /**
     * A constant for the property name <code>jcr:mandatory</code> (in expanded
     * form), declared in node types {@link javax.jcr.nodetype.NodeType#NT_PROPERTY_DEFINITION
     * nt:propertyDefinition} and {@link javax.jcr.nodetype.NodeType#NT_CHILD_NODE_DEFINITION
     * nt:childNodeDefinition}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_MANDATORY = "{http://www.jcp.org/jcr/1.0}mandatory";

    /**
     * A constant for the property name <code>jcr:protected</code> (in expanded
     * form), declared in node types {@link javax.jcr.nodetype.NodeType#NT_PROPERTY_DEFINITION
     * nt:propertyDefinition} and {@link javax.jcr.nodetype.NodeType#NT_CHILD_NODE_DEFINITION
     * nt:childNodeDefinition}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_PROTECTED = "{http://www.jcp.org/jcr/1.0}protected";

    /**
     * A constant for the property name <code>jcr:onParentVersion</code> (in
     * expanded form), declared in node types {@link javax.jcr.nodetype.NodeType#NT_PROPERTY_DEFINITION
     * nt:propertyDefinition} and {@link javax.jcr.nodetype.NodeType#NT_CHILD_NODE_DEFINITION
     * nt:childNodeDefinition}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_ON_PARENT_VERSION = "{http://www.jcp.org/jcr/1.0}onParentVersion";

    /**
     * A constant for the property name <code>jcr:requiredType</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_PROPERTY_DEFINITION
     * nt:propertyDefinition}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_REQUIRED_TYPE = "{http://www.jcp.org/jcr/1.0}requiredType";

    /**
     * A constant for the property name <code>jcr:valueConstraints</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_PROPERTY_DEFINITION
     * nt:propertyDefinition}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_VALUE_CONSTRAINTS = "{http://www.jcp.org/jcr/1.0}valueConstraints";

    /**
     * A constant for the property name <code>jcr:defaultValues</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_PROPERTY_DEFINITION
     * nt:propertyDefinition}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_DEFAULT_VALUES = "{http://www.jcp.org/jcr/1.0}defaultValues";

    /**
     * A constant for the property name <code>jcr:multiple</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_PROPERTY_DEFINITION
     * nt:propertyDefinition}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_MULTIPLE = "{http://www.jcp.org/jcr/1.0}multiple";

    /**
     * A constant for the property name <code>jcr:requiredPrimaryTypes</code>
     * (in expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_CHILD_NODE_DEFINITION
     * nt:childNodeDefinition}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_REQUIRED_PRIMARY_TYPES = "{http://www.jcp.org/jcr/1.0}requiredPrimaryTypes";

    /**
     * A constant for the property name <code>jcr:defaultPrimaryType</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_CHILD_NODE_DEFINITION
     * nt:childNodeDefinition}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_DEFAULT_PRIMARY_TYPE = "{http://www.jcp.org/jcr/1.0}defaultPrimaryType";

    /**
     * A constant for the property name <code>jcr:sameNameSiblings</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_CHILD_NODE_DEFINITION
     * nt:childNodeDefinition}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_SAME_NAME_SIBLINGS = "{http://www.jcp.org/jcr/1.0}sameNameSiblings";

    /**
     * A constant for the property name <code>jcr:lockOwner</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_LOCKABLE
     * mix:lockable}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_LOCK_OWNER = "{http://www.jcp.org/jcr/1.0}lockOwner";

    /**
     * A constant for the property name <code>jcr:lockIsDeep</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_LOCKABLE
     * mix:lockable}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_LOCK_IS_DEEP = "{http://www.jcp.org/jcr/1.0}lockIsDeep";

    /**
     * A constant for the property name <code>jcr:lifecyclePolicy</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_LIFECYCLE
     * mix:lifecycle}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_LIFECYCLE_POLICY = "{http://www.jcp.org/jcr/1.0}lifecyclePolicy";

    /**
     * A constant for the property name <code>jcr:currentLifecycleState</code>
     * (in expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_LIFECYCLE
     * mix:lifecycle}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_CURRENT_LIFECYCLE_STATE = "{http://www.jcp.org/jcr/1.0}currentLifecycleState";

    /**
     * A constant for the property name <code>jcr:isCheckedOut</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_SIMPLE_VERSIONABLE
     * mix:simpleVersionable}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_IS_CHECKED_OUT = "{http://www.jcp.org/jcr/1.0}isCheckedOut";

    /**
     * A constant for the property name <code>jcr:frozenPrimaryType</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_FROZEN_NODE
     * nt:frozenNode}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_FROZEN_PRIMARY_TYPE = "{http://www.jcp.org/jcr/1.0}frozenPrimaryType";

    /**
     * A constant for the property name <code>jcr:frozenMixinTypes</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_FROZEN_NODE
     * nt:frozenNode}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_FROZEN_MIXIN_TYPES = "{http://www.jcp.org/jcr/1.0}frozenMixinTypes";

    /**
     * A constant for the property name <code>jcr:frozenUuid</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_FROZEN_NODE
     * nt:frozenNode}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_FROZEN_UUID = "{http://www.jcp.org/jcr/1.0}frozenUuid";

    /**
     * A constant for the property name <code>jcr:versionHistory</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_VERSIONABLE
     * mix:versionable}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_VERSION_HISTORY = "{http://www.jcp.org/jcr/1.0}versionHistory";

    /**
     * A constant for the property name <code>jcr:baseVersion</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_VERSIONABLE
     * mix:versionable}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_BASE_VERSION = "{http://www.jcp.org/jcr/1.0}baseVersion";

    /**
     * A constant for the property name <code>jcr:predecessors</code> (in
     * expanded form), declared in node types {@link javax.jcr.nodetype.NodeType#MIX_VERSIONABLE
     * mix:versionable} and {@link javax.jcr.nodetype.NodeType#NT_VERSION
     * nt:version}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_PREDECESSORS = "{http://www.jcp.org/jcr/1.0}predecessors";

    /**
     * A constant for the property name <code>jcr:mergeFailed</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_VERSIONABLE
     * mix:versionable}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_MERGE_FAILED = "{http://www.jcp.org/jcr/1.0}mergeFailed";

    /**
     * A constant for the property name <code>jcr:activity</code> (in expanded
     * form), declared in node types {@link javax.jcr.nodetype.NodeType#MIX_VERSIONABLE
     * mix:versionable} and {@link javax.jcr.nodetype.NodeType#NT_VERSION
     * nt:version}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_ACTIVITY = "{http://www.jcp.org/jcr/1.0}activity";

    /**
     * A constant for the property name <code>jcr:configuration</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#MIX_VERSIONABLE
     * mix:versionable}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_CONFIGURATION = "{http://www.jcp.org/jcr/1.0}configuration";

    /**
     * A constant for the property name <code>jcr:versionableUuid</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_VERSION
     * nt:version}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_VERSIONABLE_UUID = "{http://www.jcp.org/jcr/1.0}versionableUuid";

    /**
     * A constant for the property name <code>jcr:copiedFrom</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_VERSION
     * nt:version}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_COPIED_FROM = "{http://www.jcp.org/jcr/1.0}copiedFrom";

    /**
     * A constant for the property name <code>jcr:successors</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_VERSION
     * nt:versione}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_SUCCESSORS = "{http://www.jcp.org/jcr/1.0}successors";

    /**
     * A constant for the property name <code>jcr:childVersionHistory</code> (in
     * expanded form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_VERSIONED_CHILD
     * nt:versionedChild}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_CHILD_VERSION_HISTORY = "{http://www.jcp.org/jcr/1.0}childVersionHistory";

    /**
     * A constant for the property name <code>jcr:root</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_CONFIGURATION
     * nt:configuration}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_ROOT = "{http://www.jcp.org/jcr/1.0}root";

    /**
     * A constant for the property name <code>jcr:statement</code> (in expanded
     * form), declared in node type {@link javax.jcr.nodetype.NodeType#NT_QUERY
     * nt:query}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_STATEMENT = "{http://www.jcp.org/jcr/1.0}statement";

    /**
     * A constant representing the <code>read</code> action string, used to
     * determine if this <code>Session</code> has permission to retrieve an item
     * (and read the value, in the case of a property).
     *
     * @see #hasPermission(String, String)
     * @see #checkPermission(String, String)
     * @since JCR 2.0
     */
    public static final String ACTION_READ = "read";

    /**
     * A constant representing the <code>add_node</code> action string, used to
     * determine if this <code>Session</code> has permission to add a new node.
     *
     * @see #hasPermission(String, String)
     * @see #checkPermission(String, String)
     * @since JCR 2.0
     */
    public static final String ACTION_ADD_NODE = "add_node";

    /**
     * A constant representing the <code>set_property</code> action string, used
     * to determine if this <code>Session</code> has permission to set (add or
     * modify) a property.
     *
     * @see #hasPermission(String, String)
     * @see #checkPermission(String, String)
     * @since JCR 2.0
     */
    public static final String ACTION_SET_PROPERTY = "set_property";

    /**
     * A constant representing the <code>remove</code> action string, used to
     * determine if this <code>Session</code> has permission to remove an item.
     *
     * @see #hasPermission(String, String)
     * @see #checkPermission(String, String)
     * @since JCR 2.0
     */
    public static final String ACTION_REMOVE = "remove";

    /**
     * A constant for the JCR name <code>jcr:content</code>. This is the name of
     * a child node  declared in {@link NodeType#NT_FILE nt:file} and a property
     * declared in {@link javax.jcr.nodetype.NodeType#NT_LINKED_FILE
     * nt:linkedFile}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_CONTENT = "{http://www.jcp.org/jcr/1.0}content";

    /**
     * A constant for the node name <code>jcr:propertyDefinition</code> declared
     * in {@link NodeType#NT_FILE nt:nodeType}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_PROPERTY_DEFINITION = "{http://www.jcp.org/jcr/1.0}propertyDefinition";

    /**
     * A constant for the node name <code>jcr:childNodeDefinition</code>
     * declared in {@link NodeType#NT_FILE nt:nodeType}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_CHILD_NODE_DEFINITION = "{http://www.jcp.org/jcr/1.0}childNodeDefinition";

    /**
     * A constant for the node name <code>jcr:rootVersion</code> declared in
     * {@link NodeType#NT_VERSION_HISTORY nt:versionHistory}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_ROOT_VERSION = "{http://www.jcp.org/jcr/1.0}rootVersion";

    /**
     * A constant for the node name <code>jcr:versionLabels</code> declared in
     * {@link NodeType#NT_VERSION_HISTORY nt:versionHistory}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_VERSION_LABELS = "{http://www.jcp.org/jcr/1.0}versionLabels";

    /**
     * A constant for the node name <code>jcr:frozenNode</code> declared in
     * {@link NodeType#NT_VERSION nt:version}.
     *
     * @since JCR 2.0
     */
    public static final String JCR_FROZEN_NODE = "{http://www.jcp.org/jcr/1.0}frozenNode";
}
