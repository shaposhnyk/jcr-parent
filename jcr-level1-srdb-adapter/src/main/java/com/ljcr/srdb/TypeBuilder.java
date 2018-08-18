package com.ljcr.srdb;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.TypeDefinition;

public interface TypeBuilder<T extends TypeBuilder> {
    TypeDefinition getTypeDef();

    default T setReference(String reference) {
        return set(getTypeDef().getFieldDefByName("reference"), reference);
    }

    default T set(String fieldName, TypeBuilder builder) {
        return set(getTypeDef().getFieldDefByName(fieldName), builder);
    }

    T set(PropertyDefinition field, TypeBuilder builder);

    T set(PropertyDefinition field, Object value);

    default T add(String fieldName, TypeBuilder builder) {
        return add(getTypeDef().getFieldDefByName(fieldName), builder);
    }

    T add(PropertyDefinition field, TypeBuilder builder);

    T add(PropertyDefinition field, Object value);

    TypeBuilder newFieldBuilder(String colors);

    TypeBuilder newFieldTypeBuilder(String colors);

    T setLocalized(String fieldName, String locale, String value);

    T addItem(TypeBuilder builder);

    ImmutableNode build();
}
