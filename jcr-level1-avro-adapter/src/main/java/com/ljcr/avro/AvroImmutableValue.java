package com.ljcr.avro;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.stream.Stream;

public class AvroImmutableValue implements ImmutableNode {
    private final Object value;
    private final Path path;

    public AvroImmutableValue(Object obj, Path path) {
        this.value = obj;
        this.path = path;
    }

    @Override
    public Path getKey() {
        return path;
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
        throw new PathNotFoundException(path.resolve(fieldName).toString());
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
}
