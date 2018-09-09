package com.ljcr.srdb.mods.visitors;

import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;

public class ChainingVisitor<T> implements StandardTypeVisitor<T> {

    private final StandardTypeVisitor<?> source;

    private final StandardTypeVisitor<T> sink;

    public ChainingVisitor(StandardTypeVisitor<?> source, StandardTypeVisitor<T> sink) {
        this.source = source;
        this.sink = sink;
    }


    @Override
    public T visit(StandardTypes.StringType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.BinaryType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.LongType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.DoubleType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.DateTimeType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.BooleanType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.IdentifierType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.PathType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.ReferenceType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.WeakReferenceType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.UriType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.DateType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.DecimalType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.TypeDefinitionType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.ArrayType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.MapType type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }

    @Override
    public T visit(TypeDefinition type, Object context) {
        final Object result = source.visit(type, context);
        return sink.visit(type, context);
    }
}
