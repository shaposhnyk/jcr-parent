package com.ljcr.srdb;

import com.ljcr.api.definitions.TypeDefinition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface RelationalTypeDefinition extends TypeDefinition {
    /**
     * @return resource corresponding to the given type
     */
    @Nonnull
    Resource getTypeResource();

    /**
     * @return reference type for the given type if type is referencable or null otherwise
     */
    @Nullable
    RelationalTypeDefinition getReferenceType();
}
