package com.ljcr.srdb;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class TypeSchema extends StandardTypes.TypeDefinitionType {

    private final Map<String, PropertyDefinition> fields;

    protected TypeSchema(String name, Map<String, PropertyDefinition> fields) {
        super(name);
        this.fields = Collections.unmodifiableMap(fields);
    }

    public static TypeSchema metaSchemaOf(TypeDefinition fieldSchema) {
        return new TypeSchema(StandardTypes.TYPEDEF.getIdentifier(), initFields(fieldSchema));
    }

    private static Map<String, PropertyDefinition> initFields(TypeDefinition fieldSchema) {
        Map<String, TypeDefinition> fields = new LinkedHashMap<>();
        fields.put("name", StandardTypes.NAME);
        fields.put("namespace", StandardTypes.STRING);
        fields.put("doc", StandardTypes.STRING);
        fields.put("aliases", StandardTypes.arrayOf(StandardTypes.STRING));
        fields.put("referencable", StandardTypes.BOOLEAN);
        fields.put("fields", StandardTypes.arrayOf(fieldSchema));

        Map<String, PropertyDefinition> defs = new LinkedHashMap<>();
        for (Map.Entry<String, TypeDefinition> e : fields.entrySet()) {
            if ("name".equals(e.getKey()) || "type".equals(e.getKey())) {
                defs.put(e.getKey(), StandardTypes.mandatoryPropertyOf(e.getKey(), e.getValue()));
            } else {
                defs.put(e.getKey(), StandardTypes.propertyOf(e.getKey(), e.getValue()));
            }
        }

        return Collections.unmodifiableMap(defs);
    }

    protected Map<String, PropertyDefinition> getInternalMap() {
        return fields;
    }

    @Override
    public boolean isReferencable() {
        return true;
    }

    @Override
    public Collection<PropertyDefinition> getDeclaredPropertyDefinitions() {
        return getInternalMap().values();
    }

    @Override
    public PropertyDefinition getFieldDefByName(String name) {
        return getInternalMap().get(name);
    }
}
