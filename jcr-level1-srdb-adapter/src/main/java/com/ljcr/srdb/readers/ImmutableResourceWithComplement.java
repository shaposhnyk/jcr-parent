package com.ljcr.srdb.readers;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * Resource of no relations. Only reference is set
 */
public class ImmutableResourceWithComplement implements ImmutableNode {
    private final ImmutableNode principal;

    private final ImmutableNode fallback;

    public ImmutableResourceWithComplement(ImmutableNode parent, ImmutableNode fallback) {
        this.principal = parent;
        this.fallback = fallback;
    }

    @Override
    @Nonnull
    public String getName() {
        return fallback.getName();
    }

    @Override
    @Nullable
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        PropertyDefinition field = principal.getTypeDefinition().getFieldDefByName(fieldName);
        if (field != null) {
            return principal.getItem(fieldName);
        }

        return fallback.getItem(fieldName);
    }

    @Override
    public Stream<ImmutableNode> getItems() {
        return Stream.concat(principal.getItems(), fallback.getItems());
    }

    @Override
    public boolean isObjectNode() {
        return fallback.isObjectNode();
    }

    @Override
    public boolean isArrayNode() {
        return fallback.isArrayNode();
    }

    @Override
    @Nonnull
    public TypeDefinition getTypeDefinition() {
        return fallback.getTypeDefinition();
    }

    @Override
    @Nullable
    public <T> T accept(@Nonnull ImmutableItemVisitor<T> visitor) {
        return fallback.accept(visitor);
    }

    @Override
    @Nullable
    public Object getValue() {
        return fallback.getValue();
    }
}
