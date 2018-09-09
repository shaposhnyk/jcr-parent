package com.ljcr.srdb.readers;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableValue;
import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.srdb.RelationalPropertyFactory;
import com.ljcr.srdb.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * Default immutable resource, working with complete type information
 */
public class ImmutableResource implements ImmutableNode {

    private static final Logger logger = LoggerFactory.getLogger(ImmutableResource.class);

    private final TypeDefinition type;
    private final Resource res;
    private final RelationalPropertyFactory factory;

    public ImmutableResource(
            TypeDefinition type,
            Resource res,
            RelationalPropertyFactory factory
    ) {
        this.type = type;
        this.res = res;
        this.factory = factory;
    }

    @Nonnull
    @Override
    public String getName() {
        return res.getReference();
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
        }

        ImmutableValue value = factory.getValue(field);
        return nodeOf(field, value);
    }

    private ImmutableNode nodeOf(PropertyDefinition field, ImmutableValue value) {
        return new ImmutableValueNodeWrapper(field.getIdentifier(), value);
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
