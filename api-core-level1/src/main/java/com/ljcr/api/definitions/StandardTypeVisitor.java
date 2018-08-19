package com.ljcr.api.definitions;

/**
 * Contextual Visitor interface, which can be used for object convertions depending on their type
 *
 * @param <T> - visit result type
 */
public interface StandardTypeVisitor<T> {
    T visit(StandardTypes.StringType type, Object context);

    T visit(StandardTypes.BinaryType type, Object context);

    T visit(StandardTypes.LongType type, Object context);

    T visit(StandardTypes.DoubleType type, Object context);

    T visit(StandardTypes.DateTimeType type, Object context);

    T visit(StandardTypes.BooleanType type, Object context);

    T visit(StandardTypes.IdentifierType type, Object context);

    T visit(StandardTypes.PathType type, Object context);

    T visit(StandardTypes.ReferenceType type, Object context);

    T visit(StandardTypes.WeakReferenceType type, Object context);

    T visit(StandardTypes.UriType type, Object context);

    T visit(StandardTypes.DateType type, Object context);

    T visit(StandardTypes.DecimalType type, Object context);

    T visit(StandardTypes.TypeDefinitionType type, Object context);

    T visit(StandardTypes.ArrayType type, Object context);

    T visit(StandardTypes.MapType type, Object context);

    /**
     * All non standard types will pass by this method
     * So you can plug-in your custom visitor here
     *
     * @return visit result
     */
    T visit(TypeDefinition type, Object context);
}
