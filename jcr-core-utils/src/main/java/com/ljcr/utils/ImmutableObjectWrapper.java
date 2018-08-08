package com.ljcr.utils;

import com.ljcr.api.*;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public class ImmutableObjectWrapper implements ImmutableObjectNode {
    private final ImmutableObjectNode delegate;

    public ImmutableObjectWrapper(ImmutableObjectNode delegate) {
        this.delegate = delegate;
    }

    public ImmutableObjectNode getDelegate() {
        return delegate;
    }

    @Override
    public boolean isObjectNode() {
        return delegate.isObjectNode();
    }

    @Override
    public String getReference() {
        return delegate.getReference();
    }

    @Override
    public Stream<String> getFieldNames() {
        return delegate.getFieldNames();
    }

    @Override
    public boolean hasField(String fieldName) {
        return delegate.hasField(fieldName);
    }

    @Override
    public List<ImmutableNode> getFieldsList() {
        return delegate.getFieldsList();
    }

    @Override
    public Stream<ImmutableNode> getReferences() {
        return delegate.getReferences();
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
