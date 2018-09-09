package com.ljcr.srdb.readers;

import com.ljcr.api.ImmutableValue;
import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.StandardValueNodes;
import com.ljcr.srdb.mods.visitors.*;

import java.util.function.Function;

public class SimpleValueNodeFactory extends DelegatingVisitor<ImmutableValue>
        implements StandardTypeVisitor<ImmutableValue> {

    private static final StandardTypeVisitor<ImmutableValue> FALLBACK = newThrowingVisitor();

    public SimpleValueNodeFactory() {
        super(FALLBACK);
    }

    public static <T> StandardTypeVisitor<T> newThrowingVisitor() {
        return newThrowingVisitor(new UnsupportedOperationException());
    }

    public static <T> StandardTypeVisitor<T> newThrowingVisitor(RuntimeException exception) {
        StandardTypeVisitor<RuntimeException> ex = ConstantVisitor.of(exception);
        Function<RuntimeException, T> fx = e -> {
            throw e;
        };

        ContextMappingVisitor throwing = ContextMappingVisitor.of(fx);
        return new ChainingVisitor<>(ex, throwing);
    }

    @Override
    public ImmutableValue visit(StandardTypes.StringType type, Object context) {
        return StandardValueNodes.of((String) context);
    }

    @Override
    public ImmutableValue visit(StandardTypes.LongType type, Object context) {
        return StandardValueNodes.of((Long) context);
    }

    @Override
    public ImmutableValue visit(StandardTypes.DoubleType type, Object context) {
        return StandardValueNodes.of((Long) context);
    }

    @Override
    public ImmutableValue visit(StandardTypes.BooleanType type, Object context) {
        return StandardValueNodes.of((Long) context);
    }

    @Override
    public ImmutableValue visit(StandardTypes.IdentifierType type, Object context) {
        return StandardValueNodes.of((String) context);
    }
}
