package com.ljcr.dynamics;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import org.apache.avro.Schema;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class AvroTypeDefinition implements TypeDefinition {
    private final Schema schema;
    private final String name;

    public AvroTypeDefinition(Schema schema) {
        this(schema, schema.getName());
    }

    public AvroTypeDefinition(Schema schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    @Override
    public String getIdentifier() {
        return name;
    }

    @Override
    public Collection<PropertyDefinition> getDeclaredPropertyDefinitions() {
        return schema.getFields().stream()
                .map(f -> propertyOf(f))
                .collect(toList());
    }

    private PropertyDefinition propertyOf(Schema.Field f) {
        if (f.doc() != null && f.doc().startsWith("ref:")) {
            return StandardTypes.propertyOf(f.doc().substring("ref:".length()), new AvroTypeDefinition(f.schema()));
        }
        return StandardTypes.propertyOf(f.name(), new AvroTypeDefinition(f.schema()));
    }
}
