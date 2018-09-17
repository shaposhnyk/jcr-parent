package com.ljcr.srdb.readers;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableNodeObject;
import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.StandardValueNodes;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.RepositoryException;
import com.ljcr.srdb.Resource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Resource of no relations. Only reference is set
 */
public class ImmutableResourceNoRelObject implements ImmutableNodeObject {
    private final Resource resource;
    private final TypeDefinition type;

    public ImmutableResourceNoRelObject(TypeDefinition type, Resource objResource) {
        this.type = type;
        this.resource = objResource;
    }

    @Nonnull
    @Override
    public String getName() {
        return resource.getReference();
    }

    @Nullable
    @Override
    public Object getValue() {
        return null;
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull PropertyDefinition field) {
        if (field.getType().equals(StandardTypes.NAME)) {
            return StandardValueNodes.of(resource.getReference());
        }
        throw new RepositoryException();
    }

    private ImmutableNode nameNodeOf(PropertyDefinition field) {
        return new ImmutableNodeScalarNodeWrapper(field.getIdentifier(), StandardValueNodes.of(getName()));
    }

    @Nonnull
    @Override
    public TypeDefinition getTypeDefinition() {
        return type;
    }
}
