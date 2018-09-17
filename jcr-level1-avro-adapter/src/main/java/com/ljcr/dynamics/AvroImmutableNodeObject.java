package com.ljcr.dynamics;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableNodeObject;
import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

class AvroImmutableNodeObject implements ImmutableNodeObject {
    private final GenericRecord rootRecord;
    private final String name;

    AvroImmutableNodeObject(GenericRecord rootRecord, String path) {
        this.rootRecord = rootRecord;
        this.name = path;
    }

    public static AvroImmutableNodeObject of(GenericRecord record, String fieldName) {
        return new AvroImmutableNodeObject(record, fieldName);
    }

    public static AvroImmutableNodeObject referencableOf(GenericRecord record, String reference) {
        return new AvroImmutableNodeObject(record, reference) {
            @Override
            public String getName() {
                return getName();
            }
        };
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
    public ImmutableNode getItem(@Nonnull PropertyDefinition field) {
        String fieldName = field.getIdentifier();
        Schema.Field sField = rootRecord.getSchema().getField(fieldName);
        if (field == null) {
            throw new PathNotFoundException("./" + fieldName);
        }

        return nodeOf(sField, rootRecord);
    }

    private ImmutableNode nodeOf(Schema.Field field, GenericRecord rootRecord) {
        if (Schema.Type.STRING.equals(field.schema().getType())
                && field.doc() != null && field.doc().startsWith("ref:")) {

            Object obj = rootRecord.get(field.name());
            AvroTypeDefinition type = new AvroTypeDefinition(field.schema(), field.doc().substring("ref:".length()) + "Ref");
            return new AvroImmutableScalar(obj, field.name(), () -> type);
        }
        return AvroAdapter.nodeOf(field.schema(), rootRecord.get(field.name()), field.name());
    }

    @Override
    public Stream<ImmutableNode> getElements() {
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
        } else if (obj instanceof AvroImmutableNodeObject) {
            return rootRecord.equals(((AvroImmutableNodeObject) obj).rootRecord);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return rootRecord.hashCode();
    }
}
