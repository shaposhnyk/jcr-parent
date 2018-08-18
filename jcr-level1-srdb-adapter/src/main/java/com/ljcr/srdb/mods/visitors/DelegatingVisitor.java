package com.ljcr.srdb.mods.visitors;

import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;

import java.util.Objects;

public class DelegatingVisitor<T> implements StandardTypeVisitor<T> {

    private final StandardTypeVisitor<T> delegate;

    public DelegatingVisitor(StandardTypeVisitor<T> delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public T visit(StandardTypes.StringType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.BinaryType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.LongType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.DoubleType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.DateTimeType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.BooleanType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.IdentifierType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.PathType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.ReferenceType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.WeakReferenceType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.UriType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.DateType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.DecimalType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.TypeDefinitionType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.ArrayType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(StandardTypes.MapType type, Object context) {
        return delegate.visit(type, context);
    }

    @Override
    public T visit(TypeDefinition type, Object context) {
        return delegate.visit(type, context);
    }
}
