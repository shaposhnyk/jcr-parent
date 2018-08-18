package com.ljcr.srdb.mods;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.srdb.RelationalTypeBuilder;
import com.ljcr.srdb.Resource;
import com.ljcr.srdb.ResourceRelation;
import com.ljcr.srdb.TypeBuilder;
import com.ljcr.srdb.mods.visitors.*;
import org.apache.logging.log4j.util.TriConsumer;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;

public final class ResourceModifiers {
    static ResourceModifier updateReference(Resource res, String reference) {
        Objects.requireNonNull(res);
        Objects.requireNonNull(reference);
        return new ResourceModifier() {
            @Override
            public DatabaseOperation getDbOperation() {
                res.setReference(reference);
                return new UpdateOperation() {
                    @Override
                    public Resource accept(DatabaseOperationVisitor r) {
                        return (Resource) r.visit(this);
                    }

                    @Override
                    public Resource getValue() {
                        return res;
                    }

                    @Override
                    public boolean isOnResource() {
                        return true;
                    }

                    @Override
                    public ResourceRelation getRelation() {
                        return null;
                    }

                    @Override
                    public DatabaseOperation completeRelation(Resource mainResource) {
                        return null;
                    }
                };
            }
        };
    }

    public static ResourceModifier newResource(Resource res) {
        return new ResourceModifier() {
            @Override
            public DatabaseOperation getDbOperation() {
                return new InsertOperation() {
                    @Override
                    public Resource accept(DatabaseOperationVisitor r) {
                        return (Resource) r.visit(this);
                    }

                    @Override
                    public Resource getValue() {
                        return res;
                    }

                    @Override
                    public boolean isOnResource() {
                        return true;
                    }

                    @Override
                    public ResourceRelation getRelation() {
                        return null;
                    }

                    @Override
                    public DatabaseOperation completeRelation(Resource mainResource) {
                        return null;
                    }
                };
            }
        };
    }

    public static ResourceModifier newPrimitiveRelation(Resource fieldRes, PropertyDefinition field, String locale, Object value) {
        StandardTypeVisitor<ResourceRelation> factory = relationFactory();

        ResourceRelation rel = field.getType()
                .accept(factory, value)
                .withLocale(locale)
                .withChild(fieldRes);

        return new ResourceModifier() {
            @Override
            public DatabaseOperation getDbOperation() {
                return new InsertOperation() {
                    @Override
                    public Object accept(DatabaseOperationVisitor r) {
                        return r.visit(this);
                    }

                    @Override
                    public ResourceRelation getRelation() {
                        return rel;
                    }

                    @Override
                    public DatabaseOperation completeRelation(Resource mainResource) {
                        rel.setParent(mainResource);
                        return this;
                    }
                };
            }
        };
    }

    private static StandardTypeVisitor<ResourceRelation> relationFactory() {
        StandardTypeVisitor<ResourceRelation> rawValueFactory = new ConditionalVisitorOrThrow<>(new RelationBuilderTypeSafeFilter(), new RelationBuilder());
        StandardTypeVisitor<ResourceRelation> nullValueFactory = new ConditionalVisitorOrNull<>(new ValuePredicate(Objects::isNull), new RelationNullBuilder());
        return new BatchingVisitor<>(Arrays.asList(nullValueFactory, rawValueFactory));
    }

    private static TriConsumer<ResourceRelation, String, Object> setterByField(PropertyDefinition field) {
        String name = field.getType().getIdentifier();
        if (StandardTypes.STRING.getIdentifier().equals(name) || StandardTypes.NAME.equals(name)) {
            return (rel, l, v) -> rel
                    .withStringValue((String) v)
                    .withLocale(l);
        } else if (StandardTypes.LONG.getIdentifier().equals(name)) {
            return (rel, l, v) -> rel
                    .withLongValue((Long) v)
                    .withLocale(l);
        } else if (StandardTypes.DECIMAL.getIdentifier().equals(name)) {
            return (rel, l, v) -> {
                final BigDecimal decimal;
                if (v instanceof Long) {
                    decimal = BigDecimal.valueOf((Long) v);
                } else if (v instanceof BigDecimal) {
                    decimal = (BigDecimal) v;
                } else if (v == null) {
                    decimal = null;
                } else {
                    decimal = new BigDecimal(v.toString());
                }
                rel
                        .withDecimalValue(decimal)
                        .withLocale(l);
            };
        } else if ("LocalizedString".equals(name)) {
            return (rel, l, v) -> rel
                    .withStringValue((String) v)
                    .withLocale(l);
        }

        // or should be object type
        throw new IllegalArgumentException("Unknown field type: " + name + " - " + field);
    }

    private static BiConsumer<String, Object> validatorByField(PropertyDefinition field) {
        return (a, b) -> {
        };
    }

    public static ResourceModifier subType(Resource fieldRes, PropertyDefinition field, TypeBuilder builder) {
        return new ResourceModifier() {
            @Override
            public DatabaseOperation getDbOperation() {
                Resource relationResource = ((RelationalTypeBuilder) builder).buildResource();
                ResourceRelation rel = new ResourceRelation().withChild(relationResource);
                return new InsertOperation() {
                    @Override
                    public Object accept(DatabaseOperationVisitor r) {
                        return r.visit(this);
                    }

                    @Override
                    public ResourceRelation getRelation() {
                        return rel;
                    }

                    @Override
                    public DatabaseOperation completeRelation(Resource mainResource) {
                        rel.withParent(mainResource);
                        return this;
                    }
                };
            }
        };
    }
}
