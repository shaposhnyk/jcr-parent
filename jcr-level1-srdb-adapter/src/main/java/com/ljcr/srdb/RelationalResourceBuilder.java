package com.ljcr.srdb;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardType;
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
        final Resource mainResource;
        try {
            mainResource = buildMainResource();
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to save main resource for: " + mainResourceModifier, e);
        }

        DatabaseOperationOnResources dbRes = new DatabaseOperationOnResources(resRepo, relsRepo);
        for (ResourceModifier m : modifiers) {
            try {
                m.getDbOperation()
                        .completeRelation(mainResource)
                        .accept(dbRes);
            } catch (RuntimeException e) {
                throw new RuntimeException("Failed to save relation " + m + " on " + mainResourceModifier, e);
            }
        }
        logger.debug("RELS saved {}: {}", type, mainResource);

        return mainResource;
    }

    private Resource buildMainResource() {
        logger.debug("Building   {}: {}", type, mainResourceModifier);
        DatabaseOperationOnResources dbRes = new DatabaseOperationOnResources(resRepo, relsRepo);

        if (mainResourceModifier == null) { // we should generate random reference
            if (type.isReferencable()) {
                throw new IllegalArgumentException("Object must have reference set, but it was not");
            }
            long randomId = new Random().nextInt();
            Resource resource = new Resource(findTypeId(type), String.valueOf(randomId < 0 ? -randomId : randomId));
            mainResourceModifier = ResourceModifiers.newResource(resource);
        }

        Resource mainResource = (Resource) mainResourceModifier.getDbOperation()
                .accept(dbRes);
        logger.debug("RES saved  {}: {}", type, mainResource);
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
        final Long typeId;
        if (field.getType() instanceof RelationalTypeDefinition) {
            typeId = ((RelationalTypeDefinition) field.getType()).getTypeResource().getId();
        } else if (field.getType().getValueType() == null) {
            typeId = ((StandardType) field.getType()).getNumericCode() * 1L;
        } else { // value types
            typeId = resRepo.findByReference(field.getType().getIdentifier(), StandardTypes.TYPEDEF)
                    .orElseThrow(() ->
                            new IllegalArgumentException("Cannot find field: " + field.getType().getIdentifier() + " of " + StandardTypes.TYPEDEF)
                    ).getId();
        }

        String ref = type.getIdentifier() + "." + field.getIdentifier();
        return resRepo.findByReferenceAndType(ref, typeId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Cannot find field: " + ref + " of " + typeId)
                );
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
