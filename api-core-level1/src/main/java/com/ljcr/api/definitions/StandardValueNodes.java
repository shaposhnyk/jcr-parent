package com.ljcr.api.definitions;

import com.ljcr.api.ImmutableNodeScalar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public final class StandardValueNodes {

    public static class StandardNode<T> implements ImmutableNodeScalar {
        private final TypeDefinition type;

        private final T value;

        public StandardNode(TypeDefinition type, T value) {
            this.type = Objects.requireNonNull(type);
            this.value = value;
        }

        @Nullable
        @Override
        public T getValue() {
            return value;
        }

        @Nonnull
        @Override
        public TypeDefinition getTypeDefinition() {
            return type;
        }
    }

    public static final class StringNodeScalarNode extends StandardNode<String> {
        public StringNodeScalarNode(String value) {
            super(StandardTypes.STRING, value);
        }
    }

    public static final class LongNodeScalarNode extends StandardNode<Long> {
        public LongNodeScalarNode(Long value) {
            super(StandardTypes.LONG, value);
        }
    }

    public static final class BooleanNodeScalarNode extends StandardNode<Boolean> {
        public BooleanNodeScalarNode(Boolean value) {
            super(StandardTypes.BOOLEAN, value);
        }
    }

    public static StringNodeScalarNode of(String value) {
        return new StringNodeScalarNode(value);
    }

    public static LongNodeScalarNode of(Long value) {
        return new LongNodeScalarNode(value);
    }

    public static BooleanNodeScalarNode of(Boolean value) {
        return new BooleanNodeScalarNode(value);
    }
}
