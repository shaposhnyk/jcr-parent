package com.ljcr.utils;

import com.ljcr.api.ImmutableArrayNode;
import com.ljcr.api.ImmutableBinaryScalar;
import com.ljcr.api.ImmutableObjectNode;
import com.ljcr.api.ImmutableScalar;
import com.ljcr.api.definitions.TypeDefinition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ImmutableScalarWrapper implements ImmutableScalar {
    private final ImmutableScalar delegate;

    public ImmutableScalarWrapper(ImmutableScalar delegate) {
        this.delegate = delegate;
    }

    public ImmutableScalar getDelegate() {
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
    public ImmutableBinaryScalar asBinaryValue() {
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

    @Nonnull
    @Override
    public TypeDefinition getTypeDefinition() {
        return delegate.getTypeDefinition();
    }
}
