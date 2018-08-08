package com.ljcr.dynamics;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.TypeDefinition;
import org.apache.avro.Schema;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class AvroTypeDefinition implements TypeDefinition {
    private final Schema schema;
    private final String name;

    private static class AvroPropertyDefinition extends AvroTypeDefinition implements PropertyDefinition {
        public AvroPropertyDefinition(Schema.Field field, String name) {
            super(field.schema(), name);
        }
    }

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
            return new AvroPropertyDefinition(f, f.doc().substring("ref:".length()));
        }
        return new AvroPropertyDefinition(f, f.name());
    }
}
