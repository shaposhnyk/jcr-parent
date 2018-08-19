package com.ljcr.srdb.mods;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.srdb.RelationalTypeDefinition;
import com.ljcr.srdb.Resource;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

class RelationalTypeDefinitionImpl implements RelationalTypeDefinition {
    private final String typeName;
    private final Resource typeRes;
    private final CompletableFuture<RelationalTypeDefinition> fRefType;
    private final Map<String, PropertyDefinition> fieldsMap;
    private final boolean referencable;
    private final TypeDefinition valueType;

    public RelationalTypeDefinitionImpl(String typeName,
                                        Resource typeRes,
                                        boolean referencable,
                                        CompletableFuture<RelationalTypeDefinition> fRefType,
                                        Map<String, PropertyDefinition> fieldsMap,
                                        TypeDefinition valueType
    ) {
        this.typeName = typeName;
        this.typeRes = typeRes;
        this.fRefType = fRefType;
        this.fieldsMap = fieldsMap;
        this.referencable = referencable;
        this.valueType = valueType;
    }

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
        try {
            return fRefType.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
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

    @Nullable
    @Override
    public TypeDefinition getValueType() {
        return valueType;
    }

    @Override
    public String toString() {
        if (valueType != null) {
            return String.format("RelationalType[%s,vt=%s]", typeName, valueType);
        }
        return String.format("RelationalType[%s]", typeName);
    }
}
