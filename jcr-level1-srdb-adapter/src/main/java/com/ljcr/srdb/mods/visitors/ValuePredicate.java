package com.ljcr.srdb.mods.visitors;

import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;

import java.util.function.Predicate;

public class ValuePredicate implements StandardTypeVisitor<Boolean> {

    private final Predicate<Object> predicate;

    public ValuePredicate(Predicate<Object> predicate) {
        this.predicate = predicate;
    }

    @Override
    public Boolean visit(StandardTypes.StringType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.BinaryType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.LongType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.DoubleType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.DateTimeType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.BooleanType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.IdentifierType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.PathType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.ReferenceType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.WeakReferenceType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.UriType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.DateType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.DecimalType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.TypeDefinitionType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.ArrayType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(StandardTypes.MapType type, Object context) {
        return predicate.test(context);
    }

    @Override
    public Boolean visit(TypeDefinition type, Object context) {
        return predicate.test(context);
    }
}
