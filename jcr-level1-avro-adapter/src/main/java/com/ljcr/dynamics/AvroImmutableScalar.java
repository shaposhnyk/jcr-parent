package com.ljcr.dynamics;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableScalar;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import org.apache.avro.Schema;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class AvroImmutableScalar implements ImmutableNode {
    private final Object value;
    private final String name;
    private final Supplier<TypeDefinition> tdSupplier;

    public AvroImmutableScalar(Object obj, String path, Supplier<TypeDefinition> supplier) {
        this.value = obj;
        this.name = path;
        this.tdSupplier = supplier;
    }

    public AvroImmutableScalar(Object obj, String path, Schema schema) {
        this(obj, path, () -> new AvroTypeDefinition(schema));
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
        return tdSupplier.get();
    }

    @Override
    @Nullable
    public <T> T accept(@Nonnull ImmutableItemVisitor<T> visitor) {
        return visitor.visit(this);
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
        } else if (obj instanceof ImmutableScalar) {
            return getValue().equals(((ImmutableScalar) obj).getValue());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value == null ? 0 : value.hashCode();
    }
}
