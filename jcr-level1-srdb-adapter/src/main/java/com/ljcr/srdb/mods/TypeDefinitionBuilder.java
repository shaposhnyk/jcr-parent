package com.ljcr.srdb.mods;

import com.ljcr.api.definitions.*;
import com.ljcr.srdb.*;

import javax.annotation.Nullable;
import java.util.*;

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

    private List<FieldDescriptor> fields = new ArrayList<>();
    private boolean referencable = false;

    private List<ContainerDescriptor> containers = new ArrayList<>(1);
    private List<ContainerDescriptor> arrays = new ArrayList<>(1);

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
        ResourceRelation field = newScalarField(resourceOf(type), fieldName);
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
            PropertyDefinition p = createField(res, rels, new FieldDescriptor("reference", StandardTypes.NAME));
            fieldsMap.put("reference", p);
        }

        for (FieldDescriptor fd : fields) {
            PropertyDefinition field = createField(res, rels, fd);
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

        final RelationalTypeDefinition refType = buildRefType(res, rels, typeRes);

        return new RelationalTypeDefinition() {
            @Override
            public String getIdentifier() {
                return typeName;
            }

            @Override
            public <T> T accept(StandardTypeVisitor<T> visitor, Object context) {
                return visitor.visit(this, context);
            }

            @Override
            public Resource getTypeResource() {
                return typeRes;
            }

            @Nullable
            @Override
            public RelationalTypeDefinition getReferenceType() {
                return refType;
            }

            @Override
            public Collection<PropertyDefinition> getDeclaredPropertyDefinitions() {
                return fieldsMap.values();
            }

            @Override
            public PropertyDefinition getFieldDefByName(String name) {
                return fieldsMap.get(name);
            }

            @Override
            public boolean isReferencable() {
                return referencable;
            }
        };
    }

    private RelationalTypeDefinition buildRefType(ResourceRepository res, RelationRepository rels, Resource typeName) {
        if (referencable) {
            return new TypeDefinitionBuilder(typeName.getReference() + "Ref")
                    .field("reference", StandardTypes.REFERENCE)
                    .build(res, rels);
        }
        return null;
    }

    private PropertyDefinition createField(ResourceRepository res, RelationRepository rels, FieldDescriptor fd) {
        TypeDefinition type = fd.type;
        final Resource typeRes;
        if (type instanceof RelationalTypeDefinition) {
            typeRes = ((RelationalTypeDefinition) type).getTypeResource();
        } else {
            typeRes = res.findById(((StandardType) type).getNumericCode() * 1L)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown type: " + type));
        }
        String fullName = initialResource.getReference() + "." + fd.fieldName;

        Resource fieldRes = res.save(new Resource(typeRes.getId(), fullName));
        ResourceRelation rel = newScalarField(typeRes, fd.fieldName);
        rel.setParent(typeRes);
        rel.setChild(fieldRes);
        rels.save(rel);
        return StandardTypes.propertyOf(rel.getStringValue(), type);
    }

    private PropertyDefinition createArrayField(ResourceRepository res, RelationRepository rels, Resource parentTypeRes, ContainerDescriptor cd) {
        Resource tArr = res.findType(StandardTypes.ARRAY).get();
        createArrayField(tArr, res, rels, parentTypeRes, cd);
        return StandardTypes.propertyOf(cd.fieldName, StandardTypes.arrayOf(cd.valueType));
    }

    private PropertyDefinition createMapField(ResourceRepository res, RelationRepository rels, Resource parentTypeRes, ContainerDescriptor cd) {
        Resource tArr = res.findType(StandardTypes.MAP).get();
        createArrayField(tArr, res, rels, parentTypeRes, cd);
        return StandardTypes.propertyOf(cd.fieldName, StandardTypes.mapOf(cd.valueType));
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
}
