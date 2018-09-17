package com.ljcr.srdb.readers;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableNodeObject;
import com.ljcr.api.ImmutableNodeScalar;
import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.srdb.PropertyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Default immutable resource, working with complete type information
 */
public class ImmutableRelationsObject implements ImmutableNodeObject {

    private static final Logger logger = LoggerFactory.getLogger(ImmutableRelationsObject.class);

    private final TypeDefinition type;
    private final PropertyFactory factory;

    public ImmutableRelationsObject(TypeDefinition type, PropertyFactory factory) {
        this.type = type;
        this.factory = factory;
    }

    @Nonnull
    @Override
    public String getName() {
        return null;
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull PropertyDefinition field) {
        ImmutableNodeScalar value = factory.getValue(field);
        return nodeOf(field, value);
    }

    private ImmutableNode nodeOf(PropertyDefinition field, ImmutableNodeScalar value) {
        return new ImmutableNodeScalarNodeWrapper(field.getIdentifier(), value);
    }

    @Nonnull
    @Override
    public TypeDefinition getTypeDefinition() {
        return type;
    }
}
