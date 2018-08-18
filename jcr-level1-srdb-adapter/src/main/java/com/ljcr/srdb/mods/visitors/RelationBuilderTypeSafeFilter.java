package com.ljcr.srdb.mods.visitors;

import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

public class RelationBuilderTypeSafeFilter implements StandardTypeVisitor<Boolean> {


    @Override
    public Boolean visit(StandardTypes.StringType type, Object context) {
        return context instanceof String;
    }

    @Override
    public Boolean visit(StandardTypes.BinaryType type, Object context) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean visit(StandardTypes.LongType type, Object context) {
        return context instanceof Long;
    }

    @Override
    public Boolean visit(StandardTypes.DoubleType type, Object context) {
        return context instanceof Double;
    }

    @Override
    public Boolean visit(StandardTypes.DateTimeType type, Object context) {
        return context instanceof LocalDateTime;
    }

    @Override
    public Boolean visit(StandardTypes.BooleanType type, Object context) {
        return context instanceof Boolean;
    }

    @Override
    public Boolean visit(StandardTypes.IdentifierType type, Object context) {
        return context instanceof String;
    }

    @Override
    public Boolean visit(StandardTypes.PathType type, Object context) {
        return context instanceof String;
    }

    @Override
    public Boolean visit(StandardTypes.ReferenceType type, Object context) {
        return context instanceof String;
    }

    @Override
    public Boolean visit(StandardTypes.WeakReferenceType type, Object context) {
        return context instanceof String;
    }

    @Override
    public Boolean visit(StandardTypes.UriType type, Object context) {
        return context instanceof String;
    }

    @Override
    public Boolean visit(StandardTypes.DateType type, Object context) {
        return context instanceof LocalDate;
    }

    @Override
    public Boolean visit(StandardTypes.DecimalType type, Object context) {
        return context instanceof BigDecimal;
    }

    @Override
    public Boolean visit(StandardTypes.TypeDefinitionType type, Object context) {
        return Boolean.FALSE;
    }

    @Override
    public Boolean visit(StandardTypes.ArrayType type, Object context) {
        return context instanceof Collection;
    }

    @Override
    public Boolean visit(StandardTypes.MapType type, Object context) {
        return context instanceof Collection;
    }

    @Override
    public Boolean visit(TypeDefinition type, Object context) {
        return Boolean.TRUE;
    }
}
