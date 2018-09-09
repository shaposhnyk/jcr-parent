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

    @Query("FROM Resource R WHERE R.typeId = :typeId AND R.id <> R.typeId ORDER BY R.typeId, R.id, R.reference ASC")
    Iterable<Resource> allOf(@Param("typeId") Long typeId);

    @Query("FROM Resource R WHERE R.id IN (SELECT RR.child.id FROM ResourceRelation RR WHERE RR.parent.id IN (SELECT R2.id FROM Resource R2 WHERE (R2.typeId = :typeId AND R2.id <> R2.typeId))) ORDER BY R.reference, R.typeId, R.id")
    Iterable<Resource> childrenOf(@Param("typeId") Long typeId);

    @Query("FROM Resource R WHERE (R.typeId = :typeId AND R.id <> R.typeId AND R.id > 0) OR R.id IN (SELECT RR.child.id FROM ResourceRelation RR WHERE RR.parent.id IN (SELECT R2.id FROM Resource R2 WHERE (R2.typeId = :typeId AND R2.id <> R2.typeId))) ORDER BY R.reference, R.typeId, R.id")
    Iterable<Resource> allOfAndTheirChildren(@Param("typeId") Long typeId);

    default Iterable<Resource> allOf(StandardType type) {
        return allOf(type.getNumericCode() * 1L);
    }

    default Iterable<Resource> childrenOf(StandardType type) {
        return childrenOf(type.getNumericCode() * 1L);
    }

    default Iterable<Resource> allOfAndTheirChildren(StandardType type) {
        return allOfAndTheirChildren(type.getNumericCode() * 1L);
    }

    @Query("FROM Resource R WHERE R.reference = :ref AND R.typeId IN (SELECT T.id FROM Resource T WHERE T.reference = :typeRef And T.typeId = :objTypeId)")
    Optional<Resource> findObject(
            @Param("ref") String objectReference,
            @Param("typeRef") String typeName,
            @Param("objTypeId") Long objectTypeId);

    default Optional<Resource> findObject(String reference, TypeDefinition type) {
        return findObject(reference, type.getIdentifier(), StandardTypes.TYPEDEF.getNumericCode() * 1L);
    }
}
