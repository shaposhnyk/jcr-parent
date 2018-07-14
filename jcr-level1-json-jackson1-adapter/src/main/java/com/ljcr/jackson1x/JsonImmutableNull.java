package com.ljcr.jackson1x;

import com.ljcr.api.ImmutableArrayNode;
import com.ljcr.api.ImmutableBinaryValue;
import com.ljcr.api.ImmutableObjectNode;
import com.ljcr.api.ImmutableValue;
import com.ljcr.api.exceptions.UnsupportedRepositoryOperationException;
import org.codehaus.jackson.JsonNode;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class JsonImmutableNull implements ImmutableValue {
    public JsonImmutableNull(JsonNode json) {
    }

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

    @Nullable
    @Override
    public long asLong() throws NumberFormatException {
        throw new UnsupportedRepositoryOperationException();
    }

    @Nullable
    @Override
    public double asDouble() throws NumberFormatException {
        throw new UnsupportedRepositoryOperationException();
    }

    @Nullable
    @Override
    public BigDecimal asDecimal() throws NumberFormatException {
        return null;
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
        return false;
    }

    @Override
    public ImmutableBinaryValue asBinaryValue() {
        return null;
    }

    @Override
    public ImmutableObjectNode asObjectNode() {
        return null;
    }

    @Override
    public ImmutableArrayNode asArrayNode() {
        return null;
    }
}
