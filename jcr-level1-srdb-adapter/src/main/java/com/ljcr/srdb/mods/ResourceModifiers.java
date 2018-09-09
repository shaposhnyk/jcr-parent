package com.ljcr.srdb.mods;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.srdb.ObjectBuilder;
import com.ljcr.srdb.RelationalResourceBuilder;
import com.ljcr.srdb.Resource;
import com.ljcr.srdb.ResourceRelation;
import com.ljcr.srdb.mods.visitors.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

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

            @Override
            public String toString() {
                return String.format("newResource[%s]", res);
            }
        };
    }

    public static ResourceModifier updateResource(Resource res) {
        return new ResourceModifier() {
            @Override
            public DatabaseOperation getDbOperation() {
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

            @Override
            public String toString() {
                return String.format("newResource[%s]", res);
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

            @Override
            public String toString() {
                return String.format("pRelMod[%s]", rel);
            }
        };
    }

    private static StandardTypeVisitor<ResourceRelation> relationFactory() {
        StandardTypeVisitor<ResourceRelation> rawValueFactory = new ConditionalVisitorOrThrow<>(new RelationBuilderTypeSafeFilter(), new RelationBuilder());
        StandardTypeVisitor<ResourceRelation> nullValueFactory = new ConditionalVisitorOrNull<>(new ValuePredicate(Objects::isNull), new NullRelationFactory());
        return new BatchingVisitor<>(Arrays.asList(nullValueFactory, rawValueFactory));
    }

    public static ResourceModifier subType(Resource fieldRes, PropertyDefinition field, ObjectBuilder builder) {
        return new ResourceModifier() {
            @Override
            public DatabaseOperation getDbOperation() {
                Resource relationResource = ((RelationalResourceBuilder) builder).buildResource();
                ResourceRelation rel = new ResourceRelation()
                        .withChild(fieldRes)
                        .withResourceValue(relationResource);
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

            @Override
            public String toString() {
                return String.format("relMod[field=%s]", field);
            }
        };
    }

    public static ResourceModifier newLazyRelation(Resource fieldRes, PropertyDefinition field, String locale, Supplier<Resource> valueSupplier) {
        StandardTypeVisitor<ResourceRelation> factory = relationFactory();
        Objects.requireNonNull(fieldRes);
        Objects.requireNonNull(valueSupplier);

        return new ResourceModifier() {
            @Override
            public DatabaseOperation getDbOperation() {
                Resource value = valueSupplier.get();

                ResourceRelation rel = field.getType()
                        .accept(factory, value)
                        .withLocale(locale)
                        .withChild(fieldRes);

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
}
