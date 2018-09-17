package com.ljcr.jackson1x;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableNodeCollection;
import com.ljcr.api.exceptions.PathNotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonImmutableNodeCollection extends JsonImmutableNode implements ImmutableNodeCollection {
    public JsonImmutableNodeCollection(String p, JsonImmutableNodeScalar jsonImmutableValue) {
        super(p, jsonImmutableValue);
    }

    @Override
    public Collection<ImmutableNode> getValue() {
        return getElements().collect(Collectors.toList());
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        return JacksonAdapter.of(fieldName, getJsonNode().get(Integer.valueOf(fieldName) - 1));
    }

    @Override
    public Stream<ImmutableNode> getElements() {
        final AtomicInteger counter = new AtomicInteger(1);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(getJsonNode().getElements(), Spliterator.ORDERED), false)
                .map(f -> JacksonAdapter.of(String.valueOf(counter.getAndIncrement()), f));
    }

    @Nullable
    @Override
    public <T> T accept(@Nonnull ImmutableItemVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public ImmutableNodeCollection asArrayNode() {
        return this;
    }
}



