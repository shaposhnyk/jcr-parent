package com.ljcr.srdb.readers;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.definitions.*;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.srdb.Resource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * Resource of no relations. Only reference is set
 */
public class ImmutableEmptyResource implements ImmutableNode {
    private final Resource resource;
    private final TypeDefinition type;

    public ImmutableEmptyResource(TypeDefinition type, Resource objResource) {
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
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        PropertyDefinition field = getTypeDefinition().getFieldDefByName(fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Unknown field: " + fieldName + " on " + getTypeDefinition());
        } else if (StandardTypes.NAME.equals(field.getType())) {
            return nameNodeOf(field);
        }
        return null;
    }

    private ImmutableNode nameNodeOf(PropertyDefinition field) {
        return new ImmutableValueNodeWrapper(field.getIdentifier(), StandardValueNodes.of(getName()));
    }

    @Override
    public Stream<ImmutableNode> getItems() {
        return getTypeDefinition().getPropertyDefinitions().stream()
                .filter(field -> StandardTypes.NAME.equals(field.getType()))
                .map(this::nameNodeOf);
    }

    @Nonnull
    @Override
    public TypeDefinition getTypeDefinition() {
        return type;
    }

    @Nullable
    @Override
    public <T> T accept(@Nonnull ImmutableItemVisitor<T> visitor) {
        return null;
    }
}
