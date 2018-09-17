package com.ljcr.dynamics;

import com.ljcr.api.ImmutableNodeCollection;
import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericArray;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AvroImmutableNodeCollection implements ImmutableNodeCollection {
    private final String name;
    private final GenericArray<?> array;

    public AvroImmutableNodeCollection(GenericArray<?> array, String path) {
        this.array = array;
        this.name = path;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        Schema elementType = array.getSchema().getElementType();
        return AvroAdapter.nodeOf(elementType, array.get(Integer.valueOf(fieldName).intValue() - 1), fieldName);
    }

    @Override
    public Stream<ImmutableNode> getElements() {
        Schema elementType = array.getSchema().getElementType();
        return IntStream.range(0, array.size())
                .mapToObj(i -> AvroAdapter.nodeOf(elementType, array.get(i), String.valueOf(i)));
    }

    @Nonnull
    @Override
    public TypeDefinition getTypeDefinition() {
        return StandardTypes.BINARY;
    }

    @Override
    @Nullable
    public <T> T accept(@Nonnull ImmutableItemVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return String.format("%s[%s]", getClass(), array);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof AvroImmutableNodeCollection) {
            return array.equals(((AvroImmutableNodeCollection) obj).array);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return array.hashCode();
    }
}
