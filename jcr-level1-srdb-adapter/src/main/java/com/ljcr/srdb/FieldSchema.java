package com.ljcr.srdb;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class FieldSchema extends StandardTypes.TypeDefinitionType {

    private static final Map<String, PropertyDefinition> FIELDS = initFields();

    private static Map<String, PropertyDefinition> initFields() {
        Map<String, TypeDefinition> fields = new LinkedHashMap<>();
        fields.put("name", StandardTypes.NAME);
        fields.put("doc", StandardTypes.STRING);
        fields.put("type", StandardTypes.TYPEDEF);
        fields.put("default", StandardTypes.ANYTYPE);
        fields.put("order", StandardTypes.STRING);
        fields.put("aliases", StandardTypes.ARRAY);
        fields.put("mandatory", StandardTypes.BOOLEAN);

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

    public FieldSchema() {
        super(StandardTypes.FIELDDEF.getIdentifier());
    }

    protected Map<String, PropertyDefinition> getInternalMap() {
        return FIELDS;
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
