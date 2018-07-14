package com.ljcr.avro;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.Workspace;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.FileReader;
import org.apache.avro.generic.GenericArray;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.util.Utf8;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AvroAdapter {

    private static final Map<Class<?>, TypeDefinition> typeMapping;

    static {
        Map<Class<?>, TypeDefinition> mapping = new HashMap<>();
        mapping.put(Integer.class, StandardTypes.LONG);
        mapping.put(Long.class, StandardTypes.LONG);
        mapping.put(Boolean.class, StandardTypes.BOOLEAN);
        mapping.put(String.class, StandardTypes.STRING);
        typeMapping = mapping;
    }

    public static Workspace createWs(final File fileName) throws IOException {
        GenericDatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>();
        FileReader<GenericRecord> streamReader = DataFileReader.openReader(fileName, reader);

        GenericRecord rootRecord = streamReader.next(null); // I expect only one record
        AvroImmutableObjectNode rootNode = new AvroImmutableObjectNode(rootRecord, "");

        return new Workspace() {
            public String getName() {
                return rootRecord.getSchema().getName();
            }

            public ImmutableNode getRootNode() {
                return rootNode;
            }
        };
    }

    static TypeDefinition avroObjectType(Object obj) {
        return obj == null ? StandardTypes.UNDEFINED : typeMapping.getOrDefault(obj.getClass(), StandardTypes.UNDEFINED);
    }

    static ImmutableNode nodeOf(Object obj, String fieldName) {
        if (obj == null) {
            return new AvroImmutableValue(null, fieldName);
        }

        if (obj instanceof GenericRecord) {
            return new AvroImmutableObjectNode((GenericRecord) obj, fieldName);
        } else if (obj instanceof GenericArray<?>) {
            return new AvroImmutableArrayNode((GenericArray) obj, fieldName);
        } else if (obj instanceof Utf8) {
            return new AvroImmutableValue(((Utf8) obj).toString(), fieldName);
        }
        return new AvroImmutableValue(obj, fieldName);
    }
}


