package com.ljcr.srdb.readers;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableNodeScalar;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ImmutableNodeScalarNodeWrapper implements ImmutableNodeScalar {
    private final ImmutableNodeScalar value;

    public ImmutableNodeScalarNodeWrapper(String name, ImmutableNodeScalar value) {
        this.value = value;
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
    @Nonnull
    public TypeDefinition getTypeDefinition() {
        return value.getTypeDefinition();
    }
}
