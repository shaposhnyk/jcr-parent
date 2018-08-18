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
    public static final class AnyType extends StandardType {
        AnyType() {
            super(0, "*");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return null;
        }
    }

    public static final StandardType ANYTYPE = new AnyType();

    /**
     * The <code>STRING</code> property type is used to store strings. It has
     * the same characteristics as the Java <code>String</code> class.
     */
    public static final class StringType extends StandardType {
        StringType() {
            super(1, "String");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    public static final StringType STRING = new StringType();

    /**
     * <code>BINARY</code> properties are used to store binary data.
     */
    public static final class BinaryType extends StandardType {
        BinaryType() {
            super(2, "Binary");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    public static final BinaryType BINARY = new BinaryType();

    /**
     * The <code>LONG</code> property type is used to store integers. It has the
     * same characteristics as the Java primitive type <code>long</code>.
     */
    public static final class LongType extends StandardType {
        LongType() {
            super(3, "Long");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    public static final LongType LONG = new LongType();

    /**
     * The <code>DOUBLE</code> property type is used to store floating point
     * numbers. It has the same characteristics as the Java primitive type
     * <code>double</code>.
     */
    public static final class DoubleType extends StandardType {
        DoubleType() {
            super(4, "Double");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    public static final DoubleType DOUBLE = new DoubleType();

    /**
     * The <code>DATE</code> property type is used to store time and date
     * information.
     */
    public static final class DateTimeType extends StandardType {
        DateTimeType() {
            super(5, "DateTime");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    public static final DateTimeType DATETIME = new DateTimeType();

    /**
     * The <code>BOOLEAN</code> property type is used to store boolean values.
     * It has the same characteristics as the Java primitive type
     * <code>boolean</code>.
     */
    public static final class BooleanType extends StandardType {
        BooleanType() {
            super(6, "Boolean");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    public static final BooleanType BOOLEAN = new BooleanType();

    /**
     * A <code>NAME</code> is a pairing of a namespace and a local name. When
     * read, the namespace is mapped to the current prefix.
     */
    public static final class IdentifierType extends StandardType {
        IdentifierType() {
            super(7, "Name");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    public static final IdentifierType NAME = new IdentifierType();

    /**
     * A <code>PATH</code> property is an ordered list of path elements. A path
     * element is a <code>NAME</code> with an optional index. When read, the
     * <code>NAME</code>s within the path are mapped to their current prefix. A
     * path may be absolute or relative.
     */
    public static final class PathType extends StandardType {
        PathType() {
            super(8, "Path");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    public static final PathType PATH = new PathType();

    public static abstract class ValueType extends StandardType {
        private final TypeDefinition valueType;

        ValueType(int id, String code, TypeDefinition valueType) {
            super(id, code);
            this.valueType = valueType;
        }

        @Nullable
        @Override
        public TypeDefinition getValueType() {
            return valueType;
        }
    }

    public static final class ReferenceType extends ValueType {
        ReferenceType(TypeDefinition valueType) {
            super(9, "Reference", valueType);
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    public static ReferenceType referenceOf(TypeDefinition valueType) {
        return new ReferenceType(valueType);
    }

    /**
     * A <code>REFERENCE</code> property stores the identifier of a
     * referenceable node (one having type <code>mix:referenceable</code>),
     * which must exist within the same workspace or session as the
     * <code>REFERENCE</code> property. A <code>REFERENCE</code> property
     * enforces this referential integrity by preventing the removal of its
     * target node.
     */
    public static final ReferenceType REFERENCE = referenceOf(ANYTYPE);

    public static final class WeakReferenceType extends ValueType {
        WeakReferenceType(TypeDefinition valueType) {
            super(10, "WeakReference", valueType);
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    public static WeakReferenceType weakReferenceOf(TypeDefinition valueType) {
        return new WeakReferenceType(valueType);
    }

    /**
     * A <code>WEAKREFERENCE</code> property stores the identifier of a
     * referenceable node (one having type <code>mix:referenceable</code>). A
     * <code>WEAKREFERENCE</code> property does not enforce referential
     * integrity.
     */
    public static final WeakReferenceType WEAKREFERENCE = weakReferenceOf(ANYTYPE);

    public static final class UriType extends StandardType {
        UriType() {
            super(11, "URI");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    /**
     * A <code>URI</code> property is identical to <code>STRING</code> property
     * except that it only accepts values that conform to the syntax of a
     * URI-reference as defined in RFC 3986.
     *
     * @since JCR 2.0
     */
    public static final UriType URI = new UriType();

    public static final class DecimalType extends StandardType {
        DecimalType() {
            super(12, "Decimal");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    /**
     * The <code>DECIMAL</code> property type is used to store precise decimal
     * numbers. It has the same characteristics as the Java class
     * <code>java.math.BigDecimal</code>.
     *
     * @since JCR 2.0
     */
    public static final DecimalType DECIMAL = new DecimalType();

    // end of standard JCR types
    public static final class TypeDefinitionType extends StandardType {
        TypeDefinitionType() {
            super(16, "TypeDef");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    public static final TypeDefinitionType TYPEDEF = new TypeDefinitionType();

    public static final class DateType extends StandardType {
        DateType() {
            super(17, "Date");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    /**
     * The <code>DATE</code> property type is used to store date only
     * information.
     */
    public static final DateType DATE = new DateType();

    public static final class ArrayType extends ValueType {
        ArrayType(TypeDefinition valueType) {
            super(18, "Array", valueType);
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    public static ArrayType arrayOf(TypeDefinition valueType) {
        return new ArrayType(valueType);
    }

    public static final ArrayType ARRAY = arrayOf(ANYTYPE);

    public static final class MapType extends ValueType {
        MapType(TypeDefinition valueType) {
            super(19, "Map", valueType);
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    }

    public static MapType mapOf(TypeDefinition valueType) {
        return new MapType(valueType);
    }

    public static final MapType MAP = mapOf(ANYTYPE);

    public static final class NullType extends StandardType {
        NullType() {
            super(20, "null");
        }

        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return null;
        }
    }

    public static final NullType NULL = new NullType();

    public static final StandardType REPOSITORY = new StandardType(65, "RepositoryType") {
        @Override
        public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
            return visitor.visit(this, context);
        }
    };

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
                .filter(t -> ((StandardType) t).getNumericCode() == type)
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
                .map(t -> ((StandardType) t).getNumericCode())
                .orElseThrow(() -> new IllegalArgumentException("unknown type: " + name));
    }

    /**
     * Private constructor to prevent instantiation.
     */
    StandardTypes() {
    }

    /**
     * Scalar type
     *
     * @param numericId - numericId for jcr compatibility
     * @param namespace - namespace
     * @param typeName  - typeName
     */
    public static TypeDefinition customTypeOf(int numericId, String namespace, String typeName) {
        return new StandardType(numericId, namespace + "/" + typeName) {
            @Override
            public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
                return null;
            }
        };
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