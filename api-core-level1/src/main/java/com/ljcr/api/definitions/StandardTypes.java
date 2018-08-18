/*
 * Copyright 2009 Day Management AG, Switzerland. All rights reserved.
 */
package com.ljcr.api.definitions;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The property types supported by the JCR standard.
 * <p>
 * This interface defines following property types: <ul> <li><code>STRING</code>
 * <li><code>BINARY</code> <li><code>LONG</code> <li><code>DOUBLE</code>
 * <li><code>DECIMAL</code> <li><code>DATE</code> <li><code>BOOLEAN</code>
 * <li><code>NAME</code> <li><code>PATH</code> <li><code>REFERENCE</code>
 * <li><code>WEAKREFERENCE</code> <li><code>URI</code> </ul>.
 */
public final class StandardTypes {

    public static class StandardScalar implements TypeDefinition {
        private final String code;
        private final int id;

        StandardScalar(int id, String code) {
            this.id = id;
            this.code = code;
        }

        @Override
        public String getIdentifier() {
            return code;
        }

        public int getNumericCode() {
            return id;
        }

        @Override
        public boolean isQueryable() {
            return false;
        }

        @Override
        public String toString() {
            return code;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj instanceof TypeDefinition) {
                return getIdentifier().equals(((TypeDefinition) obj).getIdentifier());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return getIdentifier().hashCode();
        }
    }

    /*
     * The supported property types.
     */

    /**
     * This constant can be used within a property definition (see <i>4.7.5
     * ImmutableProperty Definitions</i>) to specify that the property in question may be
     * of any type. However, it cannot be the actual type of any property
     * instance. For example, it will never be returned by and
     * it cannot be assigned as the type when creating a new property.
     */
    public static final StandardScalar ANYTYPE = new StandardScalar(0, "*");

    /**
     * The <code>STRING</code> property type is used to store strings. It has
     * the same characteristics as the Java <code>String</code> class.
     */
    public static final StandardScalar STRING = new StandardScalar(1, "String");

    /**
     * <code>BINARY</code> properties are used to store binary data.
     */
    public static final StandardScalar BINARY = new StandardScalar(2, "Binary");

    /**
     * The <code>LONG</code> property type is used to store integers. It has the
     * same characteristics as the Java primitive type <code>long</code>.
     */
    public static final StandardScalar LONG = new StandardScalar(3, "Long");

    /**
     * The <code>DOUBLE</code> property type is used to store floating point
     * numbers. It has the same characteristics as the Java primitive type
     * <code>double</code>.
     */
    public static final StandardScalar DOUBLE = new StandardScalar(4, "Double");

    /**
     * The <code>DATE</code> property type is used to store time and date
     * information.
     */
    public static final StandardScalar DATETIME = new StandardScalar(5, "DateTime");

    /**
     * The <code>BOOLEAN</code> property type is used to store boolean values.
     * It has the same characteristics as the Java primitive type
     * <code>boolean</code>.
     */
    public static final StandardScalar BOOLEAN = new StandardScalar(6, "Boolean");

    /**
     * A <code>NAME</code> is a pairing of a namespace and a local name. When
     * read, the namespace is mapped to the current prefix.
     */
    public static final StandardScalar NAME = new StandardScalar(7, "Name");

    /**
     * A <code>PATH</code> property is an ordered list of path elements. A path
     * element is a <code>NAME</code> with an optional index. When read, the
     * <code>NAME</code>s within the path are mapped to their current prefix. A
     * path may be absolute or relative.
     */
    public static final StandardScalar PATH = new StandardScalar(8, "Path");

    /**
     * A <code>REFERENCE</code> property stores the identifier of a
     * referenceable node (one having type <code>mix:referenceable</code>),
     * which must exist within the same workspace or session as the
     * <code>REFERENCE</code> property. A <code>REFERENCE</code> property
     * enforces this referential integrity by preventing the removal of its
     * target node.
     */
    public static StandardScalar referenceOf(TypeDefinition valueType) {
        return valueTypeOf(9, "Reference", valueType);
    }

    public static final StandardScalar REFERENCE = referenceOf(ANYTYPE);

    /**
     * A <code>WEAKREFERENCE</code> property stores the identifier of a
     * referenceable node (one having type <code>mix:referenceable</code>). A
     * <code>WEAKREFERENCE</code> property does not enforce referential
     * integrity.
     *
     * @since JCR 2.0
     */
    public static StandardScalar weakReferenceOf(TypeDefinition valueType) {
        return valueTypeOf(10, "WeakReference", valueType);
    }

    public static final StandardScalar WEAKREFERENCE = weakReferenceOf(ANYTYPE);


    /**
     * A <code>URI</code> property is identical to <code>STRING</code> property
     * except that it only accepts values that conform to the syntax of a
     * URI-reference as defined in RFC 3986.
     *
     * @since JCR 2.0
     */
    public static final StandardScalar URI = new StandardScalar(11, "URI");

    /**
     * The <code>DECIMAL</code> property type is used to store precise decimal
     * numbers. It has the same characteristics as the Java class
     * <code>java.math.BigDecimal</code>.
     *
     * @since JCR 2.0
     */
    public static final StandardScalar DECIMAL = new StandardScalar(12, "Decimal");

    // end of standard JCR types
    public static final StandardScalar TYPEDEF = new StandardScalar(16, "TypeDef");

    /**
     * The <code>DATE</code> property type is used to store date only
     * information.
     */
    public static final StandardScalar DATE = new StandardScalar(17, "Date");

    public static StandardScalar arrayOf(TypeDefinition valueType) {
        return valueTypeOf(18, "Array", valueType);
    }

    public static final StandardScalar ARRAY = arrayOf(ANYTYPE);

    public static StandardScalar mapOf(TypeDefinition valueType) {
        return valueTypeOf(19, "Map", valueType);
    }

    public static final StandardScalar MAP = mapOf(ANYTYPE);

    public static final StandardScalar NULL = new StandardScalar(20, "null");

    public static final StandardScalar REPOSITORY = new StandardScalar(65, "RepositoryRoot");

    private static final List<TypeDefinition> STANDARD_TYPES = Arrays.asList(
            STRING,
            BINARY,
            LONG,
            DECIMAL,
            DATE,
            DATETIME,
            BOOLEAN,
            URI,
            PATH,
            NAME,
            // special types
            REFERENCE, // reference to another object
            WEAKREFERENCE,
            ARRAY,
            MAP,
            TYPEDEF,
            ANYTYPE,
            NULL
    );


    /**
     * Returns the name of the specified <code>type</code>, as used in
     * serialization.
     *
     * @param type the property type
     * @return the name of the specified <code>type</code>
     * @throws IllegalArgumentException if <code>type</code> is not a valid
     *                                  property type.
     */
    public static String nameFromValue(int type) {
        return STANDARD_TYPES.stream()
                .filter(t -> ((StandardScalar) t).getNumericCode() == type)
                .findFirst()
                .map(t -> t.getIdentifier())
                .orElseThrow(() -> new IllegalArgumentException("unknown type: " + type));
    }

    /**
     * Returns the numeric constant value of the type with the specified name.
     *
     * @param name the name of the property type.
     * @return the numeric constant value.
     * @throws IllegalArgumentException if <code>name</code> is not a valid
     *                                  property type name.
     */
    public static int valueFromName(String name) {
        Objects.requireNonNull(name);
        return STANDARD_TYPES.stream()
                .filter(t -> t.getIdentifier().equals(name))
                .findFirst()
                .map(t -> ((StandardScalar) t).getNumericCode())
                .orElseThrow(() -> new IllegalArgumentException("unknown type: " + name));
    }

    /**
     * Private constructor to prevent instantiation.
     */
    StandardTypes() {
    }

    public static StandardScalar valueTypeOf(int id, String name, TypeDefinition valueType) {
        return new StandardScalar(id, name) {
            @Nullable
            @Override
            public TypeDefinition getValueType() {
                return valueType;
            }
        };
    }

    /**
     * Scalar type
     *
     * @param numericId - numericId for jcr compatibility
     * @param namespace - namespace
     * @param typeName  - typeName
     */
    public static TypeDefinition scalarOf(int numericId, String namespace, String typeName) {
        return new StandardScalar(numericId, namespace + "/" + typeName);
    }

    public static final PropertyDefinition UNKNOWN_PROPERTY = propertyOf("*", StandardTypes.ANYTYPE);

    /**
     * Default non-mandatory property of given type
     */
    public static PropertyDefinition propertyOf(String fieldName, TypeDefinition type) {
        return new PropertyDefinition() {
            @Override
            public String getIdentifier() {
                return fieldName;
            }

            @Override
            public TypeDefinition getType() {
                return type;
            }
        };
    }

    /**
     * Default mandatory property of given type
     */
    public static PropertyDefinition mandatoryPropertyOf(String fieldName, TypeDefinition type) {
        return new PropertyDefinition() {
            @Override
            public String getIdentifier() {
                return fieldName;
            }

            @Override
            public TypeDefinition getType() {
                return type;
            }

            @Override
            public boolean isMandatory() {
                return true;
            }
        };
    }
}