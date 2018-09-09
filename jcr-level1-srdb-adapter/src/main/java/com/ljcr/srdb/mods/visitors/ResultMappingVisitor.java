package com.ljcr.srdb.mods.visitors;

import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;

import java.util.function.Function;

public class ResultMappingVisitor<T, R> implements StandardTypeVisitor<R> {

    private final StandardTypeVisitor<T> source;

    private final Function<T, R> function;

    public ResultMappingVisitor(StandardTypeVisitor<T> source, Function<T, R> function) {
        this.source = source;
        this.function = function;
    }

    @Override
    public R visit(StandardTypes.StringType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.BinaryType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.LongType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.DoubleType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.DateTimeType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.BooleanType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.IdentifierType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.PathType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.ReferenceType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.WeakReferenceType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.UriType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.DateType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.DecimalType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.TypeDefinitionType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.ArrayType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(StandardTypes.MapType type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }

    @Override
    public R visit(TypeDefinition type, Object context) {
        final T result = source.visit(type, context);
        return function.apply(result);
    }
}
