package com.ljcr.srdb.readers;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableScalar;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public class ImmutableScalarNodeWrapper implements ImmutableNode {
    private final String name;
    private final ImmutableScalar value;

    public ImmutableScalarNodeWrapper(String name, ImmutableScalar value) {
        this.name = name;
        this.value = value;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Override
    @Nullable
    public Object getValue() {
        return value == null ? null : value.getValue();
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        throw new PathNotFoundException();
    }

    @Override
    public Stream<ImmutableNode> getItems() {
        return Stream.empty();
    }

    @Override
    @Nonnull
    public TypeDefinition getTypeDefinition() {
        return value.getTypeDefinition();
    }

    @Nullable
    @Override
    public <T> T accept(@Nonnull ImmutableItemVisitor<T> visitor) {
        return null;
    }
}
