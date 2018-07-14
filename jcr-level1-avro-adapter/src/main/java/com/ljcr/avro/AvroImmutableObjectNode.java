package com.ljcr.avro;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableObjectNode;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import org.apache.avro.generic.GenericRecord;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.stream.Stream;

class AvroImmutableObjectNode implements ImmutableObjectNode {
    private final GenericRecord rootRecord;
    private final Path path;

    public AvroImmutableObjectNode(GenericRecord rootRecord, Path path) {
        this.rootRecord = rootRecord;
        this.path = path;
    }

    public String getReference() {
        Object reference = rootRecord.get("reference");
        return reference instanceof String ? (String) reference : null;
    }

    public Path getKey() {
        return path;
    }

    @Nullable
    public Object getValue() {
        return null;
    }

    @Nullable
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        Object obj = rootRecord.get(fieldName);
        return AvroAdapter.nodeOf(obj, path, fieldName);
    }

    public Stream<ImmutableNode> getItems() {
        return rootRecord.getSchema().getFields().stream()
                .map(f -> AvroAdapter.nodeOf(rootRecord.get(f.name()), path, f.name()));
    }

    @Nonnull
    public TypeDefinition getTypeDefinition() {
        return new AvroTypeDefinition(rootRecord.getSchema());
    }

    public void accept(@Nonnull ImmutableItemVisitor visitor) {
        visitor.visit(this);
    }
}
