package com.ljcr.jackson1x;

import com.ljcr.api.*;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import org.codehaus.jackson.JsonNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class JsonImmutableNode implements ImmutableNode {
    private final String name;
    private final JsonImmutableValue delegate;
    private final Supplier<TypeDefinition> typeSupplier;

    public JsonImmutableNode(String p, JsonImmutableValue jsonImmutableValue, TypeDefinition type) {
        this(p, jsonImmutableValue, () -> type);
    }

    public JsonImmutableNode(String p, JsonImmutableValue jsonImmutableValue, Supplier<TypeDefinition> typeSupplier) {
        this.name = p;
        this.typeSupplier = typeSupplier;
        this.delegate = jsonImmutableValue;
    }

    protected JsonNode getJsonNode() {
        return delegate.getJsonNode();
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Override
    @Nullable
    public Object getValue() {
        return delegate.getValue();
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        throw new PathNotFoundException();
    }

    @Override
    public Stream<ImmutableNode> getItems() {
        return Stream.empty();
    }

    @Nonnull
    @Override
    public TypeDefinition getTypeDefinition() {
        return typeSupplier.get();
    }

    @Nullable
    @Override
    public <T> T accept(@Nonnull ImmutableItemVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    @Nullable
    public String asString() {
        return delegate.asString();
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
    @Nullable
    public boolean asBoolean() {
        return delegate.asBoolean();
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
