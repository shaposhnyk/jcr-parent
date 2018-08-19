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

public class RelationBuilder implements StandardTypeVisitor<ResourceRelation> {
    @Override
    public ResourceRelation visit(StandardTypes.StringType type, Object context) {
        return new ResourceRelation().withStringValue((String) context);
    }

    @Override
    public ResourceRelation visit(StandardTypes.BinaryType type, Object context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResourceRelation visit(StandardTypes.LongType type, Object context) {
        return new ResourceRelation().withLongValue((Long) context);
    }

    @Override
    public ResourceRelation visit(StandardTypes.DoubleType type, Object context) {
        return new ResourceRelation().withDecimalValue(BigDecimal.valueOf((Double) context));
    }

    @Override
    public ResourceRelation visit(StandardTypes.DateTimeType type, Object context) {
        String isoDateTimeInUTC = DateTimeFormatter.ISO_DATE.format((LocalDateTime) context);
        return new ResourceRelation()
                .withStringValue(isoDateTimeInUTC);
    }

    @Override
    public ResourceRelation visit(StandardTypes.BooleanType type, Object context) {
        return new ResourceRelation().withDecimalValue(Boolean.TRUE.equals(context) ? BigDecimal.ONE : BigDecimal.ZERO);
    }

    @Override
    public ResourceRelation visit(StandardTypes.IdentifierType type, Object context) {
        return new ResourceRelation().withStringValue((String) context);
    }

    @Override
    public ResourceRelation visit(StandardTypes.PathType type, Object context) {
        return new ResourceRelation().withStringValue((String) context);
    }

    @Override
    public ResourceRelation visit(StandardTypes.ReferenceType type, Object context) {
        return new ResourceRelation().withResourceValue((Resource) context);
    }

    @Override
    public ResourceRelation visit(StandardTypes.WeakReferenceType type, Object context) {
        return new ResourceRelation().withStringValue((String) context);
    }

    @Override
    public ResourceRelation visit(StandardTypes.UriType type, Object context) {
        return new ResourceRelation().withStringValue((String) context);
    }

    @Override
    public ResourceRelation visit(StandardTypes.DateType type, Object context) {
        String isoDate = DateTimeFormatter.ISO_DATE.format((LocalDate) context);
        return new ResourceRelation()
                .withStringValue(isoDate);
    }

    @Override
    public ResourceRelation visit(StandardTypes.DecimalType type, Object context) {
        return new ResourceRelation()
                .withDecimalValue((BigDecimal) context);
    }

    @Override
    public ResourceRelation visit(StandardTypes.TypeDefinitionType type, Object context) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResourceRelation visit(StandardTypes.ArrayType type, Object context) {
        return new ResourceRelation()
                .withChild((Resource) context);
    }

    @Override
    public ResourceRelation visit(StandardTypes.MapType type, Object context) {
        return new ResourceRelation()
                .withChild((Resource) context);
    }

    @Override
    public ResourceRelation visit(TypeDefinition type, Object context) {
        if ("LocalizedString".equals(type.getIdentifier())) {
            return visit(StandardTypes.STRING, context);
        } else if (type.getIdentifier().endsWith("Ref")) {
            return visit(StandardTypes.REFERENCE, context);
        }

        return new ResourceRelation()
                .withChild((Resource) context);
    }
}
