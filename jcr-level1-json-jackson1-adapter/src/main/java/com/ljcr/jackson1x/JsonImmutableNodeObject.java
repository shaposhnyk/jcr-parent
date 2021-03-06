package com.ljcr.jackson1x;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableNodeObject;
import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import org.codehaus.jackson.JsonNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JsonImmutableNodeObject extends JsonImmutableNode implements ImmutableNodeObject {
    public JsonImmutableNodeObject(String p, JsonImmutableNodeScalar jsonImmutableValue) {
        super(p, jsonImmutableValue);
    }

    @Override
    public String getName() {
        ImmutableNode reference = getItem("reference");
        return reference == null ? null : reference.asString();
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        return JacksonAdapter.of(fieldName, getJsonNode().get(fieldName));
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull PropertyDefinition field) throws PathNotFoundException {
        return JacksonAdapter.of(field.getIdentifier(), getJsonNode().get(field.getIdentifier()));
    }

    @Override
    public Stream<ImmutableNode> getElements() {
        JsonNode jsonNode = getJsonNode();
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(getJsonNode().getFieldNames(), Spliterator.ORDERED), false)
                .map(f -> JacksonAdapter.of(f, jsonNode.get(f)));
    }

    @Nullable
    @Override
    public <T> T accept(@Nonnull ImmutableItemVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public ImmutableNodeObject asObjectNode() {
        return this;
    }
}
