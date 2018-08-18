package com.ljcr.srdb.mods.visitors;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.srdb.Resource;
import com.ljcr.srdb.ResourceRelation;

public class RelationFactory {
    public ResourceRelation newRelation(Resource parent, Resource typeRes, PropertyDefinition field, Object value) {
        ConditionalVisitorOrThrow<ResourceRelation> factory = new ConditionalVisitorOrThrow<>(new RelationBuilderTypeSafeFilter(), new RelationBuilder());

        ResourceRelation newRelation = field.getType()
                .accept(factory, value)
                .withParent(parent)
                .withChild(typeRes);

        return newRelation;
    }
}
