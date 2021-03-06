package com.ljcr.srdb.readers;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableNodeScalar;
import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.StandardValueNodes;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.srdb.mods.visitors.ChainingVisitor;
import com.ljcr.srdb.mods.visitors.ConstantVisitor;
import com.ljcr.srdb.mods.visitors.ContextMappingVisitor;
import com.ljcr.srdb.mods.visitors.DelegatingVisitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;

public class SimpleValueNodeFactory extends DelegatingVisitor<ImmutableNodeScalar>
        implements StandardTypeVisitor<ImmutableNodeScalar> {

    private static final StandardTypeVisitor<ImmutableNodeScalar> FALLBACK = newThrowingVisitor();

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
    public ImmutableNodeScalar visit(StandardTypes.StringType type, Object context) {
        return StandardValueNodes.of((String) context);
    }

    @Override
    public ImmutableNodeScalar visit(StandardTypes.LongType type, Object context) {
        return StandardValueNodes.of((Long) context);
    }

    @Override
    public ImmutableNodeScalar visit(StandardTypes.DoubleType type, Object context) {
        return StandardValueNodes.of((Long) context);
    }

    @Override
    public ImmutableNodeScalar visit(StandardTypes.BooleanType type, Object context) {
        return StandardValueNodes.of((Long) context);
    }

    @Override
    public ImmutableNodeScalar visit(StandardTypes.IdentifierType type, Object context) {
        return StandardValueNodes.of((String) context);
    }

    @Override
    public ImmutableNodeScalar visit(StandardTypes.TypeDefinitionType type, Object context) {
        return (ImmutableNodeScalar) context;
    }

    @Override
    public ImmutableNodeScalar visit(StandardTypes.ArrayType type, Object context) {
        return new ImmutableNodeScalar() {
            @Nullable
            @Override
            public Collection<ImmutableNode> getValue() {
                return (Collection<ImmutableNode>) context;
            }

            @Nonnull
            @Override
            public TypeDefinition getTypeDefinition() {
                return type;
            }
        };
    }
}
