package com.ljcr.srdb.readers;

import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.srdb.ResourceRelation;
import com.ljcr.srdb.mods.visitors.DelegatingVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class IterableRelationToValueVisitor extends DelegatingVisitor<Object>
        implements StandardTypeVisitor<Object> {

    private static final Logger dmLogger = LoggerFactory.getLogger(DataModel.class);

    public IterableRelationToValueVisitor() {
        super(SimpleValueNodeFactory.newThrowingVisitor());
    }

    @Override
    public String visit(StandardTypes.StringType type, Object context) {
        Optional<ResourceRelation> scalar = extractScalar(type, context);
        return scalar
                .map(rel -> rel.getStringValue())
                .orElse(null);
    }

    @Override
    public Long visit(StandardTypes.LongType type, Object context) {
        Optional<ResourceRelation> scalar = extractScalar(type, context);
        return scalar
                .map(rel -> rel.getLongValue())
                .orElse(null);
    }

    @Override
    public Double visit(StandardTypes.DoubleType type, Object context) {
        Optional<ResourceRelation> scalar = extractScalar(type, context);
        return scalar
                .filter(rel -> rel.getDecimalValue() != null)
                .map(rel -> rel.getDecimalValue().doubleValue())
                .orElse(null);
    }

    @Override
    public Boolean visit(StandardTypes.BooleanType type, Object context) {
        Optional<ResourceRelation> scalar = extractScalar(type, context);
        return scalar
                .map(rel -> BigDecimal.ONE.equals(rel.getDecimalValue()))
                .orElse(null);
    }

    @Override
    public String visit(StandardTypes.IdentifierType type, Object context) {
        Optional<ResourceRelation> scalar = extractScalar(type, context);
        return scalar
                .map(rel -> rel.getStringValue())
                .orElse(null);
    }

    @Override
    public BigDecimal visit(StandardTypes.DecimalType type, Object context) {
        Optional<ResourceRelation> scalar = extractScalar(type, context);
        return scalar
                .map(rel -> rel.getDecimalValue())
                .orElse(null);
    }

    @Override
    public Stream<Object> visit(StandardTypes.ArrayType type, Object context) {
        if (context == null) {
            return Stream.empty();
        }

        TypeDefinition valueType = type.getValueType();
        Iterable<ResourceRelation> res = (Iterable<ResourceRelation>) context;
        return StreamSupport.stream(res.spliterator(), false)
                .map(rel -> valueType.accept(this, rel));
    }

    private Optional<ResourceRelation> extractScalar(TypeDefinition type, Object context) {
        if (context == null || context instanceof ResourceRelation) {
            return Optional.ofNullable((ResourceRelation) context);
        }

        Iterable<ResourceRelation> res = (Iterable<ResourceRelation>) context;
        Iterator<ResourceRelation> it = res.iterator();
        if (!it.hasNext()) {
            return Optional.empty();
        }
        Optional<ResourceRelation> result = Optional.ofNullable(res.iterator().next());
        if (it.hasNext()) {
            dmLogger.debug("Expected scalar value for type {}, but a list received. Returning first element", type);
        }
        return result;
    }
}
