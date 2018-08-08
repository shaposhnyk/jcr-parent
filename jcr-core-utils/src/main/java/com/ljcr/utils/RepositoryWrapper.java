package com.ljcr.utils;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableObjectNode;
import com.ljcr.api.Repository;
import com.ljcr.api.definitions.ContainerTypeDefinition;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.ItemNotFoundException;
import com.ljcr.api.exceptions.PathNotFoundException;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Collection;

public class RepositoryWrapper implements Repository {
    private final Repository delegate;

    public RepositoryWrapper(Repository delegate) {
        this.delegate = delegate;
    }

    public Repository getDelegate() {
        return delegate;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public ImmutableNode getRootNode() {
        return delegate.getRootNode();
    }

    @Override
    @Nullable
    public ImmutableObjectNode getNodeByReference(TypeDefinition type, String id) throws ItemNotFoundException {
        return delegate.getNodeByReference(type, id);
    }

    @Override
    @Nullable
    public ImmutableObjectNode getNodeByReference(String typeName, Object id) {
        return delegate.getNodeByReference(typeName, id);
    }

    @Override
    @Nullable
    public ImmutableNode getItem(Path absPath) throws PathNotFoundException {
        return delegate.getItem(absPath);
    }

    @Override
    public Collection<TypeDefinition> getKnownTypes() {
        return delegate.getKnownTypes();
    }

    @Override
    @Nullable
    public ContainerTypeDefinition findContainerType(String typeName) {
        return delegate.findContainerType(typeName);
    }
}
