package com.ljcr.jackson1x;

import com.ljcr.api.ImmutableArrayNode;
import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonImmutableArrayNode extends JsonImmutableNode implements ImmutableArrayNode {
    public JsonImmutableArrayNode(Path p, JsonImmutableValue jsonImmutableValue, TypeDefinition type) {
        super(p, jsonImmutableValue, type);
    }

    @Override
    public Collection<ImmutableNode> getValue() {
        return getItems().collect(Collectors.toList());
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        return JacksonAdapter.of(getKey().resolve(fieldName), getJsonNode().get(Integer.valueOf(fieldName) - 1));
    }

    @Override
    public Stream<ImmutableNode> getItems() {
        final AtomicInteger counter = new AtomicInteger(1);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(getJsonNode().getElements(), Spliterator.ORDERED), false)
                .map(f -> JacksonAdapter.of(getKey().resolve(String.valueOf(counter.getAndIncrement())), f));
    }

    @Override
    public void accept(@Nonnull ImmutableItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ImmutableArrayNode asArrayNode() {
        return this;
    }
}



