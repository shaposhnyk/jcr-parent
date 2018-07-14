package com.ljcr.avro;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableObjectNode;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import org.apache.avro.generic.GenericRecord;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

class AvroImmutableObjectNode implements ImmutableObjectNode {
    private final GenericRecord rootRecord;
    private final String name;

    public AvroImmutableObjectNode(GenericRecord rootRecord, String path) {
        this.rootRecord = rootRecord;
        this.name = path;
    }

    public String getReference() {
        Object reference = rootRecord.get("reference");
        return reference instanceof String ? (String) reference : null;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    public Object getValue() {
        return null;
    }

    @Nullable
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        Object obj = rootRecord.get(fieldName);
        return AvroAdapter.nodeOf(obj, fieldName);
    }

    public Stream<ImmutableNode> getItems() {
        return rootRecord.getSchema().getFields().stream()
                .map(f -> AvroAdapter.nodeOf(rootRecord.get(f.name()), f.name()));
    }

    @Nonnull
    public TypeDefinition getTypeDefinition() {
        return new AvroTypeDefinition(rootRecord.getSchema());
    }

    public void accept(@Nonnull ImmutableItemVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", getClass(), rootRecord);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof AvroImmutableObjectNode) {
            return rootRecord.equals(((AvroImmutableObjectNode) obj).rootRecord);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return rootRecord.hashCode();
    }
}
