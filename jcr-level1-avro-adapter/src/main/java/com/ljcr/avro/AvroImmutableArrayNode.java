package com.ljcr.avro;

import com.ljcr.api.ImmutableArrayNode;
import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import org.apache.avro.generic.GenericArray;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class AvroImmutableArrayNode implements ImmutableArrayNode {
    private final String name;
    private final GenericArray<?> array;

    public AvroImmutableArrayNode(GenericArray<?> array, String path) {
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
        return AvroAdapter.nodeOf(array.get(Integer.valueOf(fieldName).intValue() - 1), fieldName);
    }

    @Override
    public Stream<ImmutableNode> getItems() {
        return IntStream.range(0, array.size())
                .mapToObj(i -> AvroAdapter.nodeOf(array.get(i), String.valueOf(i)));
    }

    @Nonnull
    @Override
    public TypeDefinition getTypeDefinition() {
        return StandardTypes.BINARY;
    }

    @Override
    public void accept(@Nonnull ImmutableItemVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return String.format("%s[%s]", getClass(), array);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof AvroImmutableArrayNode) {
            return array.equals(((AvroImmutableArrayNode) obj).array);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return array.hashCode();
    }
}
