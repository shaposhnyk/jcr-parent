package com.ljcr.srdb.mods;

import com.ljcr.api.definitions.*;
import com.ljcr.srdb.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class TypeDefinitionBuilder {

    private static final class ContainerDescriptor {
        final String fieldName;
        final RelationalTypeDefinition valueType;
        final String description;

        public ContainerDescriptor(String fieldName, RelationalTypeDefinition valueType, String description) {
            this.fieldName = fieldName;
            this.valueType = valueType;
            this.description = description;
        }
    }

    private static final class FieldDescriptor {
        final String fieldName;
        final TypeDefinition type;
        String description = null;
        boolean isMandatory = false;
        private int limit = -1;

        public FieldDescriptor(String fieldName, TypeDefinition valueType) {
            this.fieldName = fieldName;
            this.type = valueType;
        }

        public FieldDescriptor withDescription(String description) {
            this.description = description;
            return this;
        }

        public FieldDescriptor isMandatory() {
            isMandatory = true;
            return this;
        }

        public FieldDescriptor limitedTo(int limit) {
            this.limit = limit;
            return this;
        }
    }

    private final Resource initialResource;
    private final List<FieldDescriptor> fields = new ArrayList<>();
    private final List<ContainerDescriptor> containers = new ArrayList<>(1);
    private final List<ContainerDescriptor> arrays = new ArrayList<>(1);

    private RelationalTypeDefinition valueType = null;

    private boolean referencable = false;

    public TypeDefinitionBuilder(String name) {
        this(Resource.newObjectType(name));
    }

    public TypeDefinitionBuilder(Resource res) {
        this.initialResource = res;
    }

    public TypeDefinitionBuilder field(String fieldName, StandardType type) {
        return field(fieldName, type, -1);
    }

    public TypeDefinitionBuilder field(String fieldName, StandardType type, int limit) {
        this.fields.add(new FieldDescriptor(fieldName, type)
                .limitedTo(limit)
        );
        return this;
    }

    public TypeDefinitionBuilder field(String fieldName, RelationalTypeDefinition type) {
        this.fields.add(new FieldDescriptor(fieldName, type));
        return this;
    }

    private ResourceRelation newScalarField(Resource fieldType, String fieldName) {
        ResourceRelation rel = new ResourceRelation();

        // rel.setParent(withParent); it is unknown at this stage
        rel.setChild(fieldType);
        rel.setStringValue(fieldName);

        return rel;
    }

    public static Resource aliasOf(StandardType type, String name) {
        return new Resource(null, type.getNumericCode() * 1L, name);
    }

    public static Resource resourceOf(StandardType type) {
        return new Resource(1L * type.getNumericCode(), StandardTypes.TYPEDEF.getNumericCode() * 1L, type.getIdentifier());
    }

    public TypeDefinitionBuilder isReferencable() {
        this.referencable = true;
        return this;
    }

    public TypeDefinitionBuilder referenceMap(String fieldName, RelationalTypeDefinition type) {
        return referenceMap(fieldName, type, null);
    }

    public TypeDefinitionBuilder referenceMap(String fieldName, RelationalTypeDefinition type, String description) {
        this.containers.add(new ContainerDescriptor(fieldName, type, description));
        return this;
    }

    public TypeDefinitionBuilder reference(String refFieldName, RelationalTypeDefinition referencableType) {
        return field(refFieldName, referencableType.getReferenceType());
    }

    public TypeDefinitionBuilder repeatable(String arrayField, RelationalTypeDefinition type) {
        this.arrays.add(new ContainerDescriptor(arrayField, type, null));
        return this;
    }

    // builder

    public RelationalTypeDefinition build(ResourceRepository res, RelationRepository rels) {
        final Resource typeRes = res.save(initialResource);
        final String typeName = initialResource.getReference();

        final Map<String, PropertyDefinition> fieldsMap = new LinkedHashMap<>();

        if (referencable) {
            PropertyDefinition p = createField(res, rels, typeRes, new FieldDescriptor("reference", StandardTypes.NAME));
            fieldsMap.put("reference", p);
        }

        for (FieldDescriptor fd : fields) {
            PropertyDefinition field = createField(res, rels, typeRes, fd);
            fieldsMap.put(fd.fieldName, field);
        }
        for (ContainerDescriptor cd : arrays) {
            PropertyDefinition arrayField = createArrayField(res, rels, typeRes, cd);
            fieldsMap.put(cd.fieldName, arrayField);
        }
        for (ContainerDescriptor cd : containers) {
            PropertyDefinition mapField = createMapField(res, rels, typeRes, cd);
            fieldsMap.put(cd.fieldName, mapField);
        }

        CompletableFuture<RelationalTypeDefinition> fRefType = new CompletableFuture<>();

        RelationalTypeDefinition typeDef = new RelationalTypeDefinitionImpl(typeName, typeRes, referencable, fRefType, fieldsMap, valueType);
        RelationalTypeDefinition refType = buildRefType(res, rels, typeDef);
        fRefType.complete(refType);

        return typeDef;
    }

    private RelationalTypeDefinition buildRefType(ResourceRepository res, RelationRepository rels, RelationalTypeDefinition valueType) {
        if (referencable) {
            return new TypeDefinitionBuilder(valueType.getIdentifier() + "Ref")
                    .valueType(valueType)
                    .field("reference", StandardTypes.REFERENCE)
                    .build(res, rels);
        }
        return null;
    }

    private TypeDefinitionBuilder valueType(RelationalTypeDefinition valueType) {
        this.valueType = valueType;
        return this;
    }

    private PropertyDefinition createField(ResourceRepository res, RelationRepository rels, Resource parentType, FieldDescriptor fd) {
        TypeDefinition type = fd.type;
        final Resource fieldTypeRes;
        if (type instanceof RelationalTypeDefinition) {
            fieldTypeRes = ((RelationalTypeDefinition) type).getTypeResource();
        } else {
            fieldTypeRes = res.findById(((StandardType) type).getNumericCode() * 1L)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown type: " + type));
        }
        String fullName = initialResource.getReference() + "." + fd.fieldName;

        Resource fieldRes = res.save(new Resource(fieldTypeRes.getId(), fullName));
        ResourceRelation rel = newScalarField(fieldTypeRes, fd.fieldName);
        rel.setParent(parentType);
        rel.setChild(fieldRes);
        rels.save(rel);

        if (rel.getStringValue().equals("reference")) {
            return new PropertyDefinition() {
                @Override
                public String getIdentifier() {
                    return rel.getStringValue();
                }

                @Override
                public TypeDefinition getType() {
                    return type;
                }

                @Override
                public boolean isIdentifier() {
                    return true;
                }
            };
        }
        return StandardTypes.propertyOf(rel.getStringValue(), type);
    }

    private PropertyDefinition createArrayField(ResourceRepository res, RelationRepository rels, Resource parentTypeRes, ContainerDescriptor cd) {
        StandardTypes.ArrayType type = StandardTypes.arrayOf(cd.valueType);
        Resource tArr = res.getOrCreateValueType(type);
        createArrayField(tArr, res, rels, parentTypeRes, cd);
        return StandardTypes.propertyOf(cd.fieldName, type);
    }

    private PropertyDefinition createMapField(ResourceRepository res, RelationRepository rels, Resource parentTypeRes, ContainerDescriptor cd) {
        StandardTypes.MapType type = StandardTypes.mapOf(cd.valueType);
        Resource tArr = res.getOrCreateValueType(type);
        createArrayField(tArr, res, rels, parentTypeRes, cd);
        return StandardTypes.propertyOf(cd.fieldName, type);
    }

    private ResourceRelation createArrayField(Resource tArr, ResourceRepository res, RelationRepository rels, Resource parentTypeRes, ContainerDescriptor cd) {
        Resource fieldRes = res.save(new Resource(tArr.getId(), parentTypeRes.getReference() + "." + cd.fieldName));

        ResourceRelation rel = new ResourceRelation();
        rel.setParent(parentTypeRes);
        rel.setChild(fieldRes);
        rel.setStringValue(cd.valueType.getIdentifier());

        ResourceRelation savedRel = rels.save(rel);

        return savedRel;
    }

    @Override
    public String toString() {
        return String.format("TypeBuilder[%s]", initialResource);
    }

}
