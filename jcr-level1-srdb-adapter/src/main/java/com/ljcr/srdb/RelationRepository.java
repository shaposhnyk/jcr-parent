package com.ljcr.srdb;

import com.ljcr.api.definitions.PropertyDefinition;
import com.ljcr.api.definitions.StandardType;
import com.ljcr.api.definitions.TypeDefinition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RelationRepository extends CrudRepository<ResourceRelation, Long> {

    @Query("FROM ResourceRelation RL WHERE RL.parent.id IN (SELECT R.id FROM Resource R WHERE R.typeId = :typeId) ORDER BY RL.parent.id ASC, RL.id ASC")
    Iterable<ResourceRelation> allRelationsOfType(@Param("typeId") Long typeId);

    default Iterable<ResourceRelation> allRelationsOfType(StandardType type) {
        return allRelationsOfType(type.getNumericCode() * 1L);
    }

    @Query("FROM ResourceRelation RL WHERE RL.parent.id = :parentId ORDER BY RL.id ASC")
    Iterable<ResourceRelation> allRelationsOfRes(@Param("parentId") Long objResourceId);

    default Iterable<ResourceRelation> allRelationsOfRes(Resource objResource) {
        return allRelationsOfRes(objResource.getId());
    }

    @Query("FROM ResourceRelation RL WHERE RL.parent.id = :parentId AND RL.child.id = :childId ORDER BY RL.id ASC")
    Iterable<ResourceRelation> relationsBetween(@Param("parentId") Long parentId,
                                                @Param("childId") Long childId);

    default Iterable<ResourceRelation> relationsBetween(Resource parent, Resource child) {
        return relationsBetween(parent.getId(), child.getId());
    }

    @Query("FROM ResourceRelation RL WHERE RL.parent.id = :parentId AND RL.child.id IN (SELECT R.id FROM Resource R WHERE R.reference = :cReference AND R.typeId = :cTypeId) ORDER BY RL.id ASC")
    Iterable<ResourceRelation> relationsBetween(
            @Param("parentId") Long parentId,
            @Param("cReference") String fieldRef,
            @Param("cTypeId") Long fieldType);

    default Iterable<ResourceRelation> relationsBetween(Resource parent, PropertyDefinition field) {
        TypeDefinition type = field.getType();
        long typeId = ((StandardType) type).getNumericCode() * 1L;
        return relationsBetween(parent.getId(), field.getIdentifier(), typeId);
    }
}
