package com.ljcr.srdb.mods.visitors;

import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.srdb.ResourceRelation;

public class RelationNullBuilder implements StandardTypeVisitor<ResourceRelation> {

    @Override
    public ResourceRelation visit(StandardTypes.StringType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.BinaryType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.LongType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.DoubleType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.DateTimeType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.BooleanType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.IdentifierType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.PathType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.ReferenceType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.WeakReferenceType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.UriType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.DateType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.DecimalType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.TypeDefinitionType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.ArrayType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(StandardTypes.MapType type, Object context) {
        return new ResourceRelation();
    }

    @Override
    public ResourceRelation visit(TypeDefinition type, Object context) {
        return new ResourceRelation();
    }
}
