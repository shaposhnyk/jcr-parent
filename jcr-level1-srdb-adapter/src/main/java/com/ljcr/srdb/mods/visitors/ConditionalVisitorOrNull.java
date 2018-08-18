package com.ljcr.srdb.mods.visitors;

import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;

import java.util.Objects;

public class ConditionalVisitorOrNull<T> implements StandardTypeVisitor<T> {

    private final StandardTypeVisitor<Boolean> predicate;

    private final StandardTypeVisitor<T> delegate;

    public ConditionalVisitorOrNull(StandardTypeVisitor<Boolean> predicate, StandardTypeVisitor<T> delegate) {
        this.predicate = Objects.requireNonNull(predicate);
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public T visit(StandardTypes.StringType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.BinaryType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.LongType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.DoubleType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.DateTimeType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.BooleanType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.IdentifierType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.PathType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.ReferenceType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.WeakReferenceType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.UriType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.DateType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.DecimalType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.TypeDefinitionType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.ArrayType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.MapType type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

    @Override
    public T visit(TypeDefinition type, Object context) {
        if (predicate.visit(type, context)) {
            return delegate.visit(type, context);
        }
        return null;
    }

}
