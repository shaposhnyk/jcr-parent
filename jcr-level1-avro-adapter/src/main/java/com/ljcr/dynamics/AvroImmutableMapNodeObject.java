package com.ljcr.dynamics;

import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableNodeObject;
import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import org.apache.avro.util.Utf8;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.Stream;

public abstract class AvroImmutableMapNodeObject<T extends CharSequence> implements ImmutableNodeObject {
    private final String name;
    private final Map<T, Object> map;

    AvroImmutableMapNodeObject(Map<T, Object> map, String path) {
        this.map = map;
        this.name = path;
    }

    public static AvroImmutableMapNodeObject<String> of(Map<String, Object> map, String name) {
        return new AvroImmutableMapNodeObject<String>(map, name) {
            @Nullable
            @Override
            public ImmutableNode getItem(@Nonnull PropertyDefinition field) {
                String fieldName = field.getIdentifier();
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

    public static AvroImmutableMapNodeObject<Utf8> ofUtf8(Map<Utf8, Object> map, String name) {
        return new AvroImmutableMapNodeObject<Utf8>(map, name) {
            @Nullable
            @Override
            public ImmutableNode getItem(@Nonnull PropertyDefinition field) {
                String fieldName = field.getIdentifier();
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
    public Stream<ImmutableNode> getElements() {
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
        } else if (obj instanceof AvroImmutableMapNodeObject) {
            return map.equals(((AvroImmutableMapNodeObject) obj).map);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}
