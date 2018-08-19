package com.ljcr.srdb;

import com.ljcr.api.definitions.StandardType;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ResourceRepository extends CrudRepository<Resource, Long> {

    default Optional<Resource> findType(StandardType type) {
        return findById(type.getNumericCode() * 1L);
    }

    default Optional<Resource> findByReference(@Param("ref") String ref, TypeDefinition type) {
        if (type instanceof StandardType) {
            return findByReferenceAndType(ref, ((StandardType) type).getNumericCode() * 1L);
        } else if (type instanceof RelationalTypeDefinition) {
            return findByReferenceAndType(ref, ((RelationalTypeDefinition) type).getTypeResource().getId());
        }
        throw new IllegalArgumentException("Unknown kind of type: " + type);
    }

    @Query("FROM Resource t where t.reference = :ref AND t.typeId = :typeId")
    Optional<Resource> findByReferenceAndType(@Param("ref") String ref, @Param("typeId") Long typeId);

    default Resource getOrCreateValueType(StandardType type) {
        Optional<Resource> valueType = findByReference(type.getIdentifier(), StandardTypes.TYPEDEF);
        if (valueType.isPresent()) {
            return valueType.get();
        }
        Resource res = new Resource(StandardTypes.TYPEDEF.getNumericCode() * 1L, type.getIdentifier());
        return save(res);
    }

    @Query("FROM Resource t where t.typeId = :typeId ORDER BY id ASC")
    Iterable<Resource> findAllOfType(@Param("typeId") Long typeId);

    default Iterable<Resource> findAllOfType(StandardType type) {
        return findAllOfType(type.getNumericCode() * 1L);
    }
}
