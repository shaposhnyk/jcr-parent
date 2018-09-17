package com.ljcr.jackson1x;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableNodeCollection;
import com.ljcr.api.ImmutableNodeObject;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import org.codehaus.jackson.JsonNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public abstract class JsonImmutableNode implements ImmutableNode {
    private final String name;
    private final JsonImmutableNodeScalar delegate;

    public JsonImmutableNode(String p, JsonImmutableNodeScalar jsonImmutableValue) {
        this.name = p;
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
    public Stream<ImmutableNode> getElements() {
        return Stream.empty();
    }

    @Nonnull
    @Override
    public TypeDefinition getTypeDefinition() {
        return delegate.getTypeDefinition();
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
    public ImmutableNodeObject asObjectNode() {
        return delegate.asObjectNode();
    }

    @Override
    public ImmutableNodeCollection asArrayNode() {
        return delegate.asArrayNode();
    }
}
