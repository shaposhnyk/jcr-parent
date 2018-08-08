package com.ljcr.utils;

import com.ljcr.api.ImmutableArrayNode;
import com.ljcr.api.ImmutableBinaryValue;
import com.ljcr.api.ImmutableObjectNode;
import com.ljcr.api.ImmutableValue;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ImmutableValueWrapper implements ImmutableValue {
    private final ImmutableValue delegate;

    public ImmutableValueWrapper(ImmutableValue delegate) {
        this.delegate = delegate;
    }

    public ImmutableValue getDelegate() {
        return delegate;
    }

    @Override
    @Nullable
    public Object getValue() {
        return delegate.getValue();
    }

    @Override
    @Nullable
    public String asString() {
        return delegate.asString();
    }

    @Override
    @Nullable
    public boolean asBoolean() {
        return delegate.asBoolean();
    }

    @Override
    @Nullable
    public long asLong() throws NumberFormatException {
        return delegate.asLong();
    }

    @Override
    @Nullable
    public double asDouble() throws NumberFormatException {
        return delegate.asDouble();
    }

    @Override
    @Nullable
    public BigDecimal asDecimal() throws NumberFormatException {
        return delegate.asDecimal();
    }

    @Override
    @Nullable
    public LocalDate asDate() {
        return delegate.asDate();
    }

    @Override
    @Nullable
    public LocalDateTime asDateTime() {
        return delegate.asDateTime();
    }

    @Override
    public ImmutableBinaryValue asBinaryValue() {
        return delegate.asBinaryValue();
    }

    @Override
    public ImmutableObjectNode asObjectNode() {
        return delegate.asObjectNode();
    }

    @Override
    public ImmutableArrayNode asArrayNode() {
        return delegate.asArrayNode();
    }
}
