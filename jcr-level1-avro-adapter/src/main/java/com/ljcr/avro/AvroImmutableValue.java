package com.ljcr.avro;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableValue;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public class AvroImmutableValue implements ImmutableNode {
    private final Object value;
    private final String name;

    public AvroImmutableValue(Object obj, String path) {
        this.value = obj;
        this.name = path;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public Object getValue() {
        return value;
    }

    @Nullable
    @Override
    public long asLong() throws NumberFormatException {
        return value instanceof Number ? ((Number) value).longValue() : null;
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        throw new PathNotFoundException(fieldName);
    }

    @Override
    public Stream<ImmutableNode> getItems() {
        return Stream.empty();
    }

    @Nonnull
    @Override
    public TypeDefinition getTypeDefinition() {
        return AvroAdapter.avroObjectType(value);
    }

    @Override
    public void accept(@Nonnull ImmutableItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", getClass(), getValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (value == null || obj == null) {
            return false;
        } else if (obj instanceof ImmutableValue) {
            return getValue().equals(((ImmutableValue) obj).getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }
}
