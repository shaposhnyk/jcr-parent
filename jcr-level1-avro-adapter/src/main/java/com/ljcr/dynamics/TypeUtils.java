package com.ljcr.dynamics;

import org.apache.avro.Schema;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class TypeUtils {
    private final SchemaVisitor visitor;
    private final HashSet<String> visited = new LinkedHashSet<>();

    interface SchemaVisitor {
        void accept(Path p, Schema type);
    }

    public static Map<Schema, Collection<Path>> collectTypes(Schema schema, Schema.Type target) {
        Map<Schema, Collection<Path>> m = new LinkedHashMap<>();
        SchemaVisitor v = (p, t) -> {
            if (target.equals(t.getType())) {
                m.merge(t, new ArrayList(Collections.singletonList(p)),
                        (v1, v2) -> {
                            v1.add(v2.iterator().next());
                            return v1;
                        });
            }
        };
        new TypeUtils(v).visit(schema);
        return m;
    }

    public TypeUtils(SchemaVisitor visitor) {
        this.visitor = visitor;
    }

    public void visit(Schema schema) {
        visit(Paths.get("/"), schema);
    }

    private void visit(Path path, Schema schema) {
        if (visited.contains(schema.getName())) {
            return;
        }

        visited.add(schema.getName());
        visitor.accept(path, schema);

        if (schema.getType().equals(Schema.Type.RECORD)) {
            for (Schema.Field f : schema.getFields()) {
                visit(path.resolve(f.name()), f.schema());
            }
        } else if (schema.getType().equals(Schema.Type.MAP)) {
            visit(path.resolve("*"), schema.getValueType());
        } else if (schema.getType().equals(Schema.Type.ARRAY)) {
            visit(path.resolve("*"), schema.getElementType());
        } else if (schema.getType().equals(Schema.Type.UNION)) {
            for (Schema s : schema.getTypes()) {
                visit(path, s);
            }
        }
    }
}
