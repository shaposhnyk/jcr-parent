package com.ljcr.srdb;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.srdb.mods.DatabaseOperationOnResources;
import com.ljcr.srdb.mods.ResourceModifier;
import com.ljcr.srdb.mods.ResourceModifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Resource builder
 */
public class RelationalResourceBuilder implements ObjectBuilder<RelationalResourceBuilder, Resource> {
    private static final Logger logger = LoggerFactory.getLogger(RelationalResourceBuilder.class);

    private final ResourceRepository resRepo;
    private final RelationRepository relsRepo;
    private final TypeDefinition type;

    private ResourceModifier mainResourceModifier = null;
    private final Collection<ResourceModifier> modifiers;

    public RelationalResourceBuilder(ResourceRepository res, RelationRepository rels, TypeDefinition type) {
        this.relsRepo = rels;
        this.resRepo = res;
        this.type = type;
        this.modifiers = new ArrayList<>();
    }

    @Override
    public TypeDefinition getTypeDef() {
        return type;
    }

    @Override
    public RelationalResourceBuilder newBuilderFor(TypeDefinition type) {
        return new RelationalResourceBuilder(resRepo, relsRepo, type);
    }

    @Override
    public RelationalResourceBuilder set(PropertyDefinition field, Object value) {
        if (field.isIdentifier()) {
            Resource resource = new Resource(findTypeId(type), (String) value);
            mainResourceModifier = ResourceModifiers.newResource(resource);
            return this;
        } else if (value instanceof String && field.getType().getValueType() != null && field.getType().getValueType().isReferencable()) {
            TypeDefinition valueType = field.getType().getValueType();
            return addLazyRelationModifier(field, null, () ->
                    resRepo.findByReference((String) value, valueType)
                            .orElseThrow(() -> new IllegalArgumentException("Cannot find object '" + value + "' of type: " + valueType))
            );
        }

        return addRelationModifier(field, null, value);
    }

    @Override
    public RelationalResourceBuilder setLocalized(String fieldName, String locale, String value) {
        Objects.requireNonNull(fieldName);
        Objects.requireNonNull(locale);
        PropertyDefinition field = fieldDefOrThrow(fieldName);
        return addRelationModifier(field, locale, value);
    }

    @Override
    public RelationalResourceBuilder add(PropertyDefinition field, ObjectBuilder builder) {
        Resource fieldRes = findFieldResource(field);
        return addRelationModifier(ResourceModifiers.subType(fieldRes, field, builder));
    }

    @Override
    public RelationalResourceBuilder add(PropertyDefinition field, Object value) {
        return addRelationModifier(field, null, value);
    }

    public Resource buildResource() {
        logger.info("Building   {}: {}", type, mainResourceModifier);
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
        logger.info("RES saved  {}: {}", type, mainResource);

        for (ResourceModifier m : modifiers) {
            m.getDbOperation()
                    .completeRelation(mainResource)
                    .accept(dbRes);
        }
        logger.info("RELS saved {}: {}", type, mainResource);

        return mainResource;
    }

    @Override
    public Resource build() {
        return buildResource();
    }

    //
    private RelationalResourceBuilder addLazyRelationModifier(PropertyDefinition field, String locale, Supplier<Resource> value) {
        Resource fieldRes = findFieldResource(field);
        return addRelationModifier(ResourceModifiers.newLazyRelation(fieldRes, field, locale, value));
    }

    private RelationalResourceBuilder addRelationModifier(PropertyDefinition field, String locale, Object value) {
        Resource fieldRes = findFieldResource(field);
        return addRelationModifier(ResourceModifiers.newPrimitiveRelation(fieldRes, field, locale, value));
    }

    public RelationalResourceBuilder addRelationModifier(ResourceModifier mod) {
        modifiers.add(mod);
        return this;
    }

    //

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

    @Override
    public RelationalResourceBuilder set(PropertyDefinition field, ObjectBuilder builder) {
        return set(field, builder.build());
    }

    @Override
    public String toString() {
        return String.format("%s[%s]", getClass().getSimpleName(), type);
    }
}
