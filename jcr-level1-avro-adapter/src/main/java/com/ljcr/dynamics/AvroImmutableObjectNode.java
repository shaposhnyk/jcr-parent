package com.ljcr.dynamics;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableObjectNode;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

class AvroImmutableObjectNode implements ImmutableObjectNode {
    private final GenericRecord rootRecord;
    private final String name;

    AvroImmutableObjectNode(GenericRecord rootRecord, String path) {
        this.rootRecord = rootRecord;
        this.name = path;
    }

    public static AvroImmutableObjectNode of(GenericRecord record, String fieldName) {
        return new AvroImmutableObjectNode(record, fieldName);
    }

    public static AvroImmutableObjectNode referencableOf(GenericRecord record, String reference) {
        return new AvroImmutableObjectNode(record, reference) {
            @Override
            public String getReference() {
                return getName();
            }
        };
    }

    @Override
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
        Schema.Field field = rootRecord.getSchema().getField(fieldName);
        if (field == null) {
            throw new PathNotFoundException("./" + fieldName);
        }

        return nodeOf(field, rootRecord);
    }

    private ImmutableNode nodeOf(Schema.Field field, GenericRecord rootRecord) {
        if (Schema.Type.STRING.equals(field.schema().getType())
                && field.doc() != null && field.doc().startsWith("ref:")) {

            Object obj = rootRecord.get(field.name());
            AvroTypeDefinition type = new AvroTypeDefinition(field.schema(), field.doc().substring("ref:".length()) + "Ref");
            return new AvroImmutableValue(obj, field.name(), () -> type);
        }
        return AvroAdapter.nodeOf(field.schema(), rootRecord.get(field.name()), field.name());
    }

    public Stream<ImmutableNode> getItems() {
        return rootRecord.getSchema().getFields().stream()
                .map(f -> nodeOf(f, rootRecord));
    }

    @Nonnull
    public TypeDefinition getTypeDefinition() {
        return new AvroTypeDefinition(rootRecord.getSchema());
    }

    @Override
    @Nullable
    public <T> T accept(@Nonnull ImmutableItemVisitor<T> visitor) {
        return visitor.visit(this);
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
