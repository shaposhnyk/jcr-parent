package com.ljcr.dynamics;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableObjectNode;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.PathNotFoundException;
import org.apache.avro.util.Utf8;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.Stream;

public abstract class AvroImmutableMapNode<T extends CharSequence> implements ImmutableObjectNode {
    private final String name;
    private final Map<T, Object> map;

    AvroImmutableMapNode(Map<T, Object> map, String path) {
        this.map = map;
        this.name = path;
    }

    public static AvroImmutableMapNode<String> of(Map<String, Object> map, String name) {
        return new AvroImmutableMapNode<String>(map, name) {
            @Override
            public String getReference() {
                return "";
            }

            @Nullable
            @Override
            public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
                Object value = map.get(fieldName);
                return AvroAdapter.referencableNodeOf(value, fieldName);
            }

            @Nullable
            @Override
            public Object getValue() {
                return null;
            }
        };
    }

    public static AvroImmutableMapNode<Utf8> ofUtf8(Map<Utf8, Object> map, String name) {
        return new AvroImmutableMapNode<Utf8>(map, name) {
            @Override
            public String getReference() {
                return "";
            }

            @Nullable
            @Override
            public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
                Utf8 key = new Utf8(fieldName);
                Object value = map.get(key);
                return AvroAdapter.referencableNodeOf(value, fieldName);
            }

            @Nullable
            @Override
            public Object getValue() {
                return null;
            }
        };
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Stream<ImmutableNode> getItems() {
        return map.entrySet().stream()
                .map(e -> AvroAdapter.referencableNodeOf(e.getValue(), e.getKey().toString()));
    }

    @Nonnull
    @Override
    public TypeDefinition getTypeDefinition() {
        return StandardTypes.ANYTYPE;
    }

    @Override
    @Nullable
    public <T> T accept(@Nonnull ImmutableItemVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return String.format("%s[%s]", getClass(), map);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof AvroImmutableMapNode) {
            return map.equals(((AvroImmutableMapNode) obj).map);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}
