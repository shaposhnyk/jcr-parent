package com.ljcr.dynamics;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableObjectNode;
import com.ljcr.api.Repository;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.FileReader;
import org.apache.avro.generic.GenericArray;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static java.util.stream.Collectors.toList;


public class AvroAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AvroAdapter.class);

    private static final Map<Class<?>, TypeDefinition> typeMapping;

    static {
        Map<Class<?>, TypeDefinition> mapping = new HashMap<>();
        mapping.put(Integer.class, StandardTypes.LONG);
        mapping.put(Long.class, StandardTypes.LONG);
        mapping.put(Boolean.class, StandardTypes.BOOLEAN);
        mapping.put(String.class, StandardTypes.STRING);
        typeMapping = mapping;
    }

    public static Repository createWs(final File fileName) throws IOException {
        GenericDatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>();
        FileReader<GenericRecord> streamReader = DataFileReader.openReader(fileName, reader);

        GenericRecord rootRecord = streamReader.next(null); // I expect only one record
        AvroImmutableObjectNode rootNode = new AvroImmutableObjectNode(rootRecord, "");

        Map<Schema, Collection<Path>> containers = TypeUtils.collectTypes(streamReader.getSchema(), Schema.Type.MAP);
        Map<Schema, Collection<Path>> objects = TypeUtils.collectTypes(streamReader.getSchema(), Schema.Type.RECORD);

        return new Repository() {
            public String getName() {
                return rootRecord.getSchema().getName();
            }

            public ImmutableNode getRootNode() {
                return rootNode;
            }

            @Override
            public Collection<TypeDefinition> getKnownTypes() {
                return objects.entrySet().stream()
                        .map(e -> e.getKey())
                        .map(s -> newTypeOf(s, containers, this))
                        .collect(toList());
            }
        };
    }

    private static TypeDefinition newTypeOf(Schema object, Map<Schema, Collection<Path>> containers, Repository rep) {
        for (Map.Entry<Schema, Collection<Path>> e : containers.entrySet()) {
            if (e.getKey().getValueType().equals(object)) {
                List<ImmutableObjectNode> maps = e.getValue().stream()
                        .map(p -> rep.getItem(p))
                        .filter(Objects::nonNull)
                        .map(node -> node.asObjectNode())
                        .collect(toList());

                return new AvroContainerTypeDefinition(object, maps);
            }
        }
        return new AvroTypeDefinition(object);
    }

    static ImmutableNode nodeOf(Schema s, Object obj, String fieldName) {
        if (obj == null) {
            return new AvroImmutableValue(null, fieldName, s);
        }

        if (obj instanceof GenericRecord) {
            return AvroImmutableObjectNode.of((GenericRecord) obj, fieldName);
        } else if (obj instanceof GenericArray<?>) {
            return new AvroImmutableArrayNode((GenericArray) obj, fieldName);
        } else if (obj instanceof Utf8) {
            return new AvroImmutableValue(((Utf8) obj).toString(), fieldName, s);
        } else if (obj instanceof Map<?, ?>) {
            if (((Map) obj).isEmpty() || ((Map<?, ?>) obj).entrySet().iterator().next().getKey() instanceof String) {
                return AvroImmutableMapNode.of((Map<String, Object>) obj, fieldName);
            }

            return AvroImmutableMapNode.ofUtf8((Map<Utf8, Object>) obj, fieldName);
        }
        return new AvroImmutableValue(obj, fieldName, s);
    }

    public static ImmutableNode referencableNodeOf(Object obj, String reference) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof GenericRecord) {
            return AvroImmutableObjectNode.referencableOf((GenericRecord) obj, reference);
        }
        logger.warn("Unexpected object type. Expecting Generic Record, but was: {}", obj);
        return null;
    }
}


