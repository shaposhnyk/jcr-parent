package com.ljcr.api.definitions;

import com.ljcr.api.ImmutableScalar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public final class StandardValueNodes {

    public static class StandardNode<T> implements ImmutableScalar {
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

    public static final class StringScalarNode extends StandardNode<String> {
        public StringScalarNode(String value) {
            super(StandardTypes.STRING, value);
        }
    }

    public static final class LongScalarNode extends StandardNode<Long> {
        public LongScalarNode(Long value) {
            super(StandardTypes.LONG, value);
        }
    }

    public static final class BooleanScalarNode extends StandardNode<Boolean> {
        public BooleanScalarNode(Boolean value) {
            super(StandardTypes.BOOLEAN, value);
        }
    }

    public static StringScalarNode of(String value) {
        return new StringScalarNode(value);
    }

    public static LongScalarNode of(Long value) {
        return new LongScalarNode(value);
    }

    public static BooleanScalarNode of(Boolean value) {
        return new BooleanScalarNode(value);
    }
}
