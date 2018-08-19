package com.ljcr.srdb;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.definitions.StandardTypes;
import org.springframework.beans.factory.annotation.Autowired;

public class RepositoryReader {

    @Autowired
    ResourceRepository res;

    @Autowired
    RelationRepository rels;

    /**
     * Reads whole relational repository in memory and returns its root node
     *
     * @return root node of the repository
     */
    public ImmutableNode readSchema() {
        Iterable<Resource> types = res.findAllOfType(StandardTypes.TYPEDEF);
        //Iterable<Resource> fields = res.findAllChildrenOfType(StandardTypes.TYPEDEF);
        //Iterable<Relation> relations = rels.findAllOfType(StandardTypes.TYPEDEF);
        return null;
    }
}
