package com.ljcr.srdb.readers;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableScalar;
import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.srdb.PropertyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * Default immutable resource, working with complete type information
 */
public class ImmutableRelations implements ImmutableNode {

    private static final Logger logger = LoggerFactory.getLogger(ImmutableRelations.class);

    private final TypeDefinition type;
    private final PropertyFactory factory;

    public ImmutableRelations(TypeDefinition type, PropertyFactory factory) {
        this.type = type;
        this.factory = factory;
    }

    @Nullable
    @Override
    public Object getValue() {
        return null;
    }

    @Nonnull
    @Override
    public String getName() {
        return null;
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        PropertyDefinition field = getTypeDefinition().getFieldDefByName(fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Unknown field: " + fieldName + " on " + getTypeDefinition());
        }

        ImmutableScalar value = factory.getValue(field);
        return nodeOf(field, value);
    }

    private ImmutableNode nodeOf(PropertyDefinition field, ImmutableScalar value) {
        return new ImmutableScalarNodeWrapper(field.getIdentifier(), value);
    }

    @Override
    public Stream<ImmutableNode> getItems() {
        return type.getPropertyDefinitions().stream()
                .map(p -> nodeOf(p, factory.getValue(p)));
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
