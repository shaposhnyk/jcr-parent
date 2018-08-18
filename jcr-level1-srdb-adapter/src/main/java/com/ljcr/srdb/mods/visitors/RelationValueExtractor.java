package com.ljcr.srdb.mods.visitors;

import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.srdb.Resource;
import com.ljcr.srdb.ResourceRelation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RelationValueExtractor implements StandardTypeVisitor<Object> {
    @Override
    public String visit(StandardTypes.StringType type, Object context) {
        return ((ResourceRelation) context).getStringValue();
    }

    @Override
    public Object visit(StandardTypes.BinaryType type, Object context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Long visit(StandardTypes.LongType type, Object context) {
        return ((ResourceRelation) context).getLongValue();
    }

    @Override
    public Double visit(StandardTypes.DoubleType type, Object context) {
        return ((ResourceRelation) context).getDecimalValue().doubleValue();
    }

    @Override
    public LocalDateTime visit(StandardTypes.DateTimeType type, Object context) {
        return LocalDateTime.parse(((ResourceRelation) context).getStringValue(), DateTimeFormatter.ISO_DATE_TIME);
    }

    @Override
    public Boolean visit(StandardTypes.BooleanType type, Object context) {
        return BigDecimal.ONE.equals(((ResourceRelation) context).getDecimalValue());
    }

    @Override
    public String visit(StandardTypes.IdentifierType type, Object context) {
        return ((ResourceRelation) context).getStringValue();
    }

    @Override
    public String visit(StandardTypes.PathType type, Object context) {
        return ((ResourceRelation) context).getStringValue();
    }

    @Override
    public Resource visit(StandardTypes.ReferenceType type, Object context) {
        return ((ResourceRelation) context).getResourceValue();
    }

    @Override
    public String visit(StandardTypes.WeakReferenceType type, Object context) {
        return ((ResourceRelation) context).getStringValue();
    }

    @Override
    public String visit(StandardTypes.UriType type, Object context) {
        return ((ResourceRelation) context).getStringValue();
    }

    @Override
    public LocalDate visit(StandardTypes.DateType type, Object context) {
        return LocalDate.parse(((ResourceRelation) context).getStringValue(), DateTimeFormatter.ISO_DATE);
    }

    @Override
    public BigDecimal visit(StandardTypes.DecimalType type, Object context) {
        return ((ResourceRelation) context).getDecimalValue();
    }

    @Override
    public Object visit(StandardTypes.TypeDefinitionType type, Object context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Resource visit(StandardTypes.ArrayType type, Object context) {
        return ((ResourceRelation) context).getResourceValue();
    }

    @Override
    public Resource visit(StandardTypes.MapType type, Object context) {
        return ((ResourceRelation) context).getResourceValue();
    }

    @Override
    public Object visit(TypeDefinition type, Object context) {
        return ((ResourceRelation) context).getResourceValue();
    }
}
