package com.ljcr.srdb;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.srdb.mods.DatabaseOperationOnResources;
import com.ljcr.srdb.mods.ResourceModifier;
import com.ljcr.srdb.mods.ResourceModifiers;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class RelationalTypeBuilder implements TypeBuilder {
    private final ResourceRepository resRepo;
    private final RelationRepository relsRepo;
    private final TypeDefinition type;

    private ResourceModifier mainResourceModifier = null;
    private final Collection<ResourceModifier> modifiers;

    public RelationalTypeBuilder(ResourceRepository res, RelationRepository rels, TypeDefinition type) {
        this.relsRepo = rels;
        this.resRepo = res;
        this.type = type;
        this.modifiers = new ArrayList<>(1);
    }

    @Override
    public TypeDefinition getTypeDef() {
        return type;
    }

    @Override
    public TypeBuilder set(PropertyDefinition field, TypeBuilder builder) {
        return set(field, builder.build());
    }

    @Override
    public TypeBuilder set(PropertyDefinition field, Object value) {
        if ("reference".equals(field.getIdentifier())) {
            Resource resource = new Resource(findTypeId(type), (String) value);
            mainResourceModifier = ResourceModifiers.newResource(resource);
            return this;
        }
        return addRelationModifier(field, null, value);
    }

    private Resource findTypeId(TypeDefinition type) {
        if (type instanceof RelationalTypeDefinition) {
            return ((RelationalTypeDefinition) type).getTypeResource();
        }
        return null;
    }

    private Resource findFieldResource(PropertyDefinition field) {
        if (field.getType() instanceof RelationalTypeDefinition) {
            return ((RelationalTypeDefinition) field.getType()).getTypeResource();
        }
        return resRepo.findByReference(field.getType().getIdentifier(), StandardTypes.TYPEDEF).get();
    }

    private TypeBuilder addRelationModifier(PropertyDefinition field, String locale, Object value) {
        Resource fieldRes = findFieldResource(field);
        return addRelationModifier(ResourceModifiers.newPrimitiveRelation(fieldRes, field, locale, value));
    }

    public RelationalTypeBuilder addRelationModifier(ResourceModifier mod) {
        modifiers.add(mod);
        return this;
    }

    @Override
    public TypeBuilder add(PropertyDefinition field, TypeBuilder builder) {
        Resource fieldRes = findFieldResource(field);
        return addRelationModifier(ResourceModifiers.subType(fieldRes, field, builder));
    }

    @Override
    public TypeBuilder add(PropertyDefinition field, Object value) {
        return addRelationModifier(field, null, value);
    }

    @Override
    public TypeBuilder newFieldBuilder(String fieldName) {
        PropertyDefinition field = type.getFieldDefByName(fieldName);
        return new RelationalTypeBuilder(resRepo, relsRepo, type);
    }

    @Override
    public TypeBuilder newFieldTypeBuilder(String fieldName) {
        PropertyDefinition field = type.getFieldDefByName(fieldName);
        return new RelationalTypeBuilder(resRepo, relsRepo, field.getType().getValueType());
    }

    @Override
    public TypeBuilder setLocalized(String fieldName, String locale, String value) {
        Objects.requireNonNull(fieldName);
        Objects.requireNonNull(locale);
        PropertyDefinition field = type.getFieldDefByName(fieldName);
        return addRelationModifier(field, locale, value);
    }

    @Override
    public TypeBuilder addItem(TypeBuilder builder) {
        TypeDefinition targetTypeDef = builder.getTypeDef(); // target type must be unique
        List<PropertyDefinition> matchingTypes = getTypeDef().getPropertyDefinitions().stream()
                .filter(propDef -> targetTypeDef.equals(propDef.getType()) || targetTypeDef.equals(propDef.getType().getValueType()))
                .limit(2)
                .collect(toList());

        if (matchingTypes.isEmpty()) {
            throw new IllegalStateException("No type found: " + targetTypeDef);
        } else if (matchingTypes.size() > 1) {
            throw new IllegalStateException("Too many types found: " + matchingTypes);
        }

        // only one type found
        return add(matchingTypes.get(0), builder);
    }

    public Resource buildResource() {
        DatabaseOperationOnResources dbRes = new DatabaseOperationOnResources(resRepo, relsRepo);

        if (mainResourceModifier == null) { // we should generate random reference
            if (type.isReferencable()) {
                throw new IllegalArgumentException("Object must have reference set, but it was not");
            }
            Resource resource = new Resource(findTypeId(type), String.valueOf(new Random().nextLong()));
            mainResourceModifier = ResourceModifiers.newResource(resource);
        }

        Resource mainResource = (Resource) mainResourceModifier.getDbOperation()
                .accept(dbRes);

        for (ResourceModifier m : modifiers) {
            m.getDbOperation()
                    .completeRelation(mainResource)
                    .accept(dbRes);
        }

        return mainResource;
    }

    @Override
    public ImmutableNode build() {
        Resource resource = buildResource();
        return null;
    }
}
