package com.ljcr.srdb;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.TypeDefinition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Nonnull
public interface ObjectBuilder<T extends ObjectBuilder, R> {
    /**
     * @return type of object we are working on
     */
    TypeDefinition getTypeDef();

    /**
     * Builds repository node of type R in accordance with builder instructions
     *
     * @return new or modified instance
     */
    R build();

    /**
     * Sets primitive value to a field
     *
     * @param field - field descriptor
     * @param value - primitive value
     * @return builder
     */
    T set(PropertyDefinition field, @Nullable Object value);

    /**
     * Sets localized string value to a field
     *
     * @param fieldName - field name
     * @param value     - text value
     * @return builder
     */
    T setLocalized(String fieldName, String locale, @Nullable String value);

    /**
     * Sets object value from another builder
     *
     * @param field   - field descriptor
     * @param builder - another object builder
     * @return builder
     */
    T set(PropertyDefinition field, ObjectBuilder builder);

    default T setReference(String reference) {
        return set(getTypeDef().getFieldDefByName("reference"), reference);
    }

    /**
     * Sets primitive value to a field
     *
     * @param fieldName - field name
     * @param value     - primitive value
     * @return builder
     */
    default T set(String fieldName, @Nullable Object value) {
        return set(fieldDefOrThrow(fieldName), value);
    }

    /**
     * Sets object value from another builder
     *
     * @param fieldName - field name
     * @param builder   - another object builder
     * @return builder
     */
    default T set(String fieldName, ObjectBuilder builder) {
        return set(fieldDefOrThrow(fieldName), builder);
    }

    /**
     * Adds object value from a builder to container field (array, map, references)
     *
     * @param fieldName - field name
     * @param builder   - another builder
     * @return builder
     */
    default T add(String fieldName, ObjectBuilder builder) {
        return add(fieldDefOrThrow(fieldName), builder);
    }

    /**
     * Adds primitive value to container field (array, map, references)
     *
     * @param field - field definition
     * @param value - value
     * @return builder
     */
    T add(PropertyDefinition field, @Nullable Object value);

    /**
     * Adds object value from a builder to container field (array, map, references)
     *
     * @param field   - field definition
     * @param builder - another builder
     * @return builder
     */
    T add(PropertyDefinition field, ObjectBuilder builder);

    /**
     * Adds object value from given typeBuilder to uniquely identified by type field
     *
     * @param builder - another object builder
     * @return builder
     * @throws IllegalArgumentException if field cannot be uniquely identified by type
     */
    default T addItem(ObjectBuilder builder) {
        TypeDefinition targetTypeDef = builder.getTypeDef(); // target type must be unique
        List<PropertyDefinition> matchingFields = getTypeDef().getPropertyDefinitions().stream()
                .filter(propDef -> targetTypeDef.equals(propDef.getType()) || targetTypeDef.equals(propDef.getType().getValueType()))
                .limit(2)
                .collect(toList());

        if (matchingFields.isEmpty()) {
            throw new IllegalArgumentException("No field of type found: " + targetTypeDef);
        } else if (matchingFields.size() > 1) {
            throw new IllegalArgumentException("Too many fields of type found: " + matchingFields);
        }

        // only one type found
        return add(matchingFields.get(0), builder);
    }

    /**
     * @param type
     * @return new builder instance for given type
     */
    T newBuilderFor(TypeDefinition type);

    default ObjectBuilder newFieldBuilder(String fieldName) {
        TypeDefinition type = fieldDefOrThrow(fieldName).getType();
        return newBuilderFor(type);
    }

    default ObjectBuilder newFieldItemBuilder(String fieldName) {
        TypeDefinition fieldType = fieldDefOrThrow(fieldName)
                .getType();
        TypeDefinition type = fieldType.getValueType();
        Objects.requireNonNull(type, () -> "Expected, but not found valueType on: " + fieldType);
        return newBuilderFor(type);
    }

    /**
     * @param fieldName - field name
     * @return field definition by a given name or throw an exception
     */
    default PropertyDefinition fieldDefOrThrow(String fieldName) {
        PropertyDefinition field = getTypeDef().getFieldDefByName(fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Unknown field name: " + fieldName + " on " + this);
        }
        return field;
    }
}
