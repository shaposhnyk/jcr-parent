package com.ljcr.utils;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.definitions.PropertyDefinition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Resource of no relations. Only reference is set
 */
public class ImmutableResourceWithComplement extends ImmutableNodeWrapper implements ImmutableNode {
    private final ImmutableNode principal;

    public ImmutableResourceWithComplement(ImmutableNode principal, ImmutableNode fallback) {
        super(fallback);
        this.principal = principal;
    }

    @Nonnull
    @Override
    public String getName() {
        return super.getName();
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull PropertyDefinition field) {
        PropertyDefinition pField = principal.getTypeDefinition().getFieldDefByName(field.getIdentifier());
        if (pField != null) {
            return principal.getItem(pField);
        }

        return super.getItem(field);
    }
}
