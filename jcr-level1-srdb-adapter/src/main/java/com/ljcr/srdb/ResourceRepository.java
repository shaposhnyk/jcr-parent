package com.ljcr.srdb;

import com.ljcr.api.definitions.StandardTypes;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ResourceRepository extends CrudRepository<Resource, Long> {

    default Optional<Resource> findType(StandardTypes.StandardScalar type) {
        return findById(type.getNumericCode() * 1L);
    }

    default Optional<Resource> findByReference(@Param("ref") String ref, StandardTypes.StandardScalar type) {
        return findByReferenceAndType(ref, type.getNumericCode() * 1L);
    }

    @Query("FROM Resource t where t.reference = :ref AND t.typeId = :typeId")
    Optional<Resource> findByReferenceAndType(@Param("ref") String ref, @Param("typeId") Long typeId);
}
