package com.ljcr.jackson1x;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.Repository;
import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class JacksonAdapter {
    private static final JsonNode jsonNull = new ObjectMapper().getNodeFactory().nullNode();
    private static final JsonImmutableValue JSON_IMMUTABLE_NULL = new JsonImmutableValue(jsonNull) {
        @Nullable
        @Override
        public Object getValue() {
            return null;
        }

        @Nullable
        @Override
        public String asString() {
            return null;
        }
    };

    private static final List<PropertyDefinition> anyTypes = Arrays.asList(StandardTypes.UNKNOWN_PROPERTY);

    private static final TypeDefinition objectType = new TypeDefinition() {
        @Override
        public String getIdentifier() {
            return "object";
        }

        @Override
        public Collection<PropertyDefinition> getDeclaredPropertyDefinitions() {
            return anyTypes;
        }
    };
    private static final TypeDefinition arrayType = new TypeDefinition() {
        @Override
        public String getIdentifier() {
            return "array";
        }

        @Override
        public Collection<PropertyDefinition> getDeclaredPropertyDefinitions() {
            return anyTypes;
        }
    };

    public static JsonImmutableNode of(String name, JsonNode json) {
        if (json == null || json.isNull()) {
            return new JsonImmutableNode(name, JSON_IMMUTABLE_NULL, StandardTypes.ANYTYPE);
        } else if (json.isObject()) {
            return new JsonImmutableObjectNode(name, new JsonImmutableValue(json), objectType);
        } else if (json.isArray()) {
            return new JsonImmutableArrayNode(name, new JsonImmutableValue(json), arrayType);
        }
        return new JsonImmutableNode(name, new JsonImmutableValue(json), () -> typeOf(json));
    }

    public static Repository createWs(String name, JsonNode json) {
        JsonImmutableNode jsonItem = of("", json);

        return new Repository() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public ImmutableNode getRootNode() {
                return jsonItem;
            }
        };
    }

    static TypeDefinition typeOf(JsonNode json) {
        if (json.isTextual()) {
            return StandardTypes.STRING;
        } else if (json.isBoolean()) {
            return StandardTypes.BOOLEAN;
        } else if (json.isLong() || json.isIntegralNumber()) {
            return StandardTypes.LONG;
        } else if (json.isBigDecimal()) {
            return StandardTypes.DECIMAL;
        } else if (json.isDouble() || json.isFloatingPointNumber()) {
            return StandardTypes.DOUBLE;
        } else if (json.isBinary()) {
            return StandardTypes.BINARY;
        }

        return StandardTypes.ANYTYPE;
    }
}




