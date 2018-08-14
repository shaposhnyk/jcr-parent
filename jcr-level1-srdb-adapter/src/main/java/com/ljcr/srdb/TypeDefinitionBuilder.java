package com.ljcr.srdb;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class TypeDefinitionBuilder {
    private final Resource initialResource;

    private List<ResourceRelation> fields = new ArrayList<>();
    private boolean referencable = false;

    public TypeDefinitionBuilder(String name) {
        this(Resource.newObjectType(name));
    }

    public TypeDefinitionBuilder(Resource res) {
        this.initialResource = res;
    }

    public TypeDefinitionBuilder field(String fieldName, StandardTypes.StandardScalar type) {
        return field(fieldName, type, -1);
    }

    public TypeDefinitionBuilder field(String fieldName, StandardTypes.StandardScalar type, int limit) {
        ResourceRelation field = newScalarField(typeOf(type), fieldName);
        this.fields.add(field);
        return this;
    }

    public TypeDefinitionBuilder field(String fieldName, RelationalTypeDefinition type) {
        ResourceRelation field = newScalarField(type.getTypeResource(), fieldName);
        this.fields.add(field);
        return this;
    }

    private ResourceRelation newScalarField(Resource fieldType, String fieldName) {
        ResourceRelation rel = new ResourceRelation();

        // rel.setParent(parent); it is unknown at this stage
        rel.setChild(fieldType);
        rel.setStringValue(fieldName);

        return rel;
    }

    public TypeDefinitionBuilder alias(StandardTypes.StandardScalar string) {
        return this;
    }

    public RelationalTypeDefinition build(ResourceRepository res, RelationRepository rels) {
        final Resource typeRes = res.save(initialResource);
        final String typeName = initialResource.getReference();

        if (referencable) {
            createField(res, rels, typeRes, newScalarField(typeOf(StandardTypes.STRING), "reference"));
        }

        for (ResourceRelation rel : fields) {
            createField(res, rels, typeRes, rel);
        }

        if (referencable) {
            new TypeDefinitionBuilder(typeName + "Ref")
                    .field("reference", StandardTypes.REFERENCE)
                    .build(res, rels);
        }

        return new RelationalTypeDefinition() {
            @Override
            public String getIdentifier() {
                return typeName;
            }

            @Override
            public Resource getTypeResource() {
                return typeRes;
            }

            @Override
            public Collection<PropertyDefinition> getDeclaredPropertyDefinitions() {
                return fields.stream()
                        .map(f -> new PropertyDefinition() {
                            @Override
                            public String getIdentifier() {
                                return f.getStringValue();
                            }
                        })
                        .collect(toList());
            }
        };
    }

    private ResourceRelation createField(ResourceRepository res, RelationRepository rels, Resource typeRes, ResourceRelation rel) {
        Resource fieldRes = res.save(new Resource(rel.getChild().getId(), typeRes.getReference() + "." + rel.getStringValue()));
        rel.setParent(typeRes);
        rel.setChild(fieldRes);
        return rels.save(rel);
    }

    public static Resource typeOf(StandardTypes.StandardScalar type, String name) {
        return new Resource(1L * type.getNumericCode(), StandardTypes.TYPEDEF.getNumericCode() * 1L, name);
    }

    public static Resource typeOf(StandardTypes.StandardScalar type) {
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
        return this;
    }

    public TypeDefinitionBuilder reference(String refFieldName, RelationalTypeDefinition refType) {
        return this;
    }

    public TypeDefinitionBuilder repeatable(String arrayField, RelationalTypeDefinition type) {
        return this;
    }
}
