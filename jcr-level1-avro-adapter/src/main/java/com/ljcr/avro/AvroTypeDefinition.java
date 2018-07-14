package com.ljcr.avro;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.TypeDefinition;
import org.apache.avro.Schema;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public class AvroTypeDefinition implements TypeDefinition {
    private final Schema schema;

    private static class AvroPropertyDefinition extends AvroTypeDefinition implements PropertyDefinition {

        private final Schema.Field field;

        public AvroPropertyDefinition(Schema.Field field) {
            super(field.schema());
            this.field = field;
        }

        @Override
        public String getIdentifier() {
            return field.name();
        }
    }

    public AvroTypeDefinition(Schema schema) {
        this.schema = schema;
    }

    @Override
    public String getIdentifier() {
        return schema.getType().getName();
    }

    @Override
    public Collection<PropertyDefinition> getDeclaredPropertyDefinitions() {
        return schema.getFields().stream()
                .map(f -> propertyOf(f))
                .collect(toList());
    }

    private PropertyDefinition propertyOf(Schema.Field f) {
        return new AvroPropertyDefinition(f);
    }
}
