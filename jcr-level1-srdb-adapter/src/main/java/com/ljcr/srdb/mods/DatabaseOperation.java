package com.ljcr.srdb.mods;

import com.ljcr.srdb.Resource;
import com.ljcr.srdb.ResourceRelation;

public interface DatabaseOperation {
    Object accept(DatabaseOperationVisitor r);

    default boolean isOnResource() {
        return false;
    }

    default Resource getValue() {
        return null;
    }

    ResourceRelation getRelation();

    DatabaseOperation completeRelation(Resource mainResource);
}
