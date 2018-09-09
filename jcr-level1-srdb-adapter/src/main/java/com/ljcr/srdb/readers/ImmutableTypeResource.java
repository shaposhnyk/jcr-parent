package com.ljcr.srdb.readers;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.srdb.Resource;
import com.ljcr.srdb.ResourceRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Immutable type resource
 */
public class ImmutableTypeResource implements ImmutableNode {

    private static final Logger logger = LoggerFactory.getLogger(ImmutableTypeResource.class);

    private final TypeDefinition type;
    private final Resource res;
    private final Map<PropertyDefinition, List<ResourceRelation>> fields;
    private final StandardTypeVisitor<ImmutableNode> relationConverter;

    ImmutableTypeResource(
            TypeDefinition type,
            Resource res,
            Map<PropertyDefinition, List<ResourceRelation>> fields,
            StandardTypeVisitor<ImmutableNode> relationConverter
    ) {
        this.type = type;
        this.res = res;
        this.fields = fields;
        this.relationConverter = relationConverter;
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
        List<ResourceRelation> relValue = fields.get(field);

        ImmutableNode finalValue = field.getType()
                .accept(relationConverter, relValue);

        logger.debug("value of {}: {}", field, finalValue);

        return finalValue;
    }

    @Override
    public Stream<ImmutableNode> getItems() {
        return fields.entrySet().stream()
                .map(e -> e.getKey()
                        .getType()
                        .accept(relationConverter, e.getValue()));
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
