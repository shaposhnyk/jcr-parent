package com.ljcr.utils;

import com.ljcr.api.*;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class ImmutableNodeWrapper implements ImmutableNode {
    private final ImmutableNode delegate;

    public ImmutableNodeWrapper(ImmutableNode delegate) {
        this.delegate = delegate;
    }

    public ImmutableNode getDelegate() {
        return delegate;
    }

    @Override
    @Nonnull
    public String getName() {
        return delegate.getName();
    }

    @Override
    @Nullable
    public Object getValue() {
        return delegate.getValue();
    }

    @Override
    @Nullable
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        return delegate.getItem(fieldName);
    }

    @Override
    public Stream<ImmutableNode> getItems() {
        return delegate.getItems();
    }

    @Override
    public boolean isObjectNode() {
        return delegate.isObjectNode();
    }

    @Override
    public boolean isArrayNode() {
        return delegate.isArrayNode();
    }

    @Override
    @Nonnull
    public TypeDefinition getTypeDefinition() {
        return delegate.getTypeDefinition();
    }

    @Override
    @Nullable
    public <T> T accept(@Nonnull ImmutableItemVisitor<T> visitor) {
        return delegate.accept(visitor);
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
        return (ImmutableBinaryScalar) this;
    }

    @Override
    public ImmutableObjectNode asObjectNode() {
        return (ImmutableObjectNode) this;
    }

    @Override
    public ImmutableArrayNode asArrayNode() {
        return (ImmutableArrayNode) this;
    }
}
