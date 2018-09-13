package com.ljcr.jackson1x;

import com.ljcr.api.ImmutableArrayNode;
import com.ljcr.api.ImmutableBinaryScalar;
import com.ljcr.api.ImmutableObjectNode;
import com.ljcr.api.ImmutableScalar;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import org.codehaus.jackson.JsonNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonImmutableScalar implements ImmutableScalar {
    private final JsonNode json;

    public JsonImmutableScalar(JsonNode json) {
        this.json = json;
    }

    public JsonNode getJsonNode() {
        return json;
    }

    @Nullable
    @Override
    public Object getValue() {
        if (json.isTextual()) {
            return asString();
        } else if (json.isInt() || json.isLong()) {
            return json.getLongValue();
        } else if (json.isBoolean()) {
            return json.asBoolean();
        } else if (json.isFloatingPointNumber() || json.isDouble()) {
            return json.asDouble();
        }
        return json;
    }

    @Nullable
    @Override
    public String asString() {
        return json.asText();
    }

    @Nullable
    @Override
    public long asLong() throws NumberFormatException {
        return json.asLong();
    }

    @Nullable
    @Override
    public double asDouble() throws NumberFormatException {
        return json.asDouble();
    }

    @Nullable
    @Override
    public BigDecimal asDecimal() throws NumberFormatException {
        return json.getDecimalValue();
    }

    @Nullable
    @Override
    public LocalDate asDate() {
        return null;
    }

    @Nullable
    @Override
    public LocalDateTime asDateTime() {
        return null;
    }

    @Nullable
    @Override
    public boolean asBoolean() {
        return json.asBoolean();
    }

    @Override
    public ImmutableBinaryScalar asBinaryValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableObjectNode asObjectNode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ImmutableArrayNode asArrayNode() {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public TypeDefinition getTypeDefinition() {
        return StandardTypes.ANYTYPE;
    }
}
