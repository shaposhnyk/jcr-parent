package com.ljcr.srdb;

import org.springframework.data.repository.CrudRepository;

public interface RelationRepository extends CrudRepository<ResourceRelation, Long> {

    //@Query("FROM Relation RL where RL.rid IN (SELECT t.id FROM Resource WHERE t.typeId = :typeId) ORDER BY RL.parent_id ASC")
    //Iterable<Relation> findAllOfType(@Param("typeId") Long typeId);

    //default Iterable<Relation> findAllOfType(StandardType type) {
    //    return findAllOfType(type.getNumericCode() * 1L);
    //}
}
