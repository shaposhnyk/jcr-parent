package com.ljcr.srdb;

import com.ljcr.api.definitions.StandardTypes;

import javax.persistence.*;

@Entity
@Table(name = "res")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_res_id")
    @SequenceGenerator(name = "sq_res_id", sequenceName = "sq_res_id", allocationSize = 1, initialValue = 100)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type_id", nullable = false)
    private Long typeId;

    @Column(name = "ref", nullable = false)
    private String reference;

    @Version
    @Column(name = "ver")
    private Long version;

    public Resource(Long id, Resource type, String reference) {
        this(id, type.getId(), reference);
    }

    public Resource(Long typeId, String reference) {
        this(null, typeId, reference);
    }

    public Resource(Long id, Long typeId, String reference) {
        this.id = id;
        this.typeId = typeId;
        this.reference = reference;
    }

    public Resource(Resource type, String reference) {
        this.typeId = type.getId();
        this.reference = reference;
    }

    public Resource() {
        // used by jpa
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long type_id) {
        this.typeId = type_id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        if (version == null || version == 0L) {
            return String.format("%s(t=%s,id=%s)", reference, typeId, id);
        }
        return String.format("%s(t=%s,id=%s,v=%s)", reference, typeId, id, version);
    }

    public static Resource newScalarType(StandardTypes.StandardScalar type, String reference) {
        return new Resource(type.getNumericCode() * 1L, reference);
    }

    /**
     * @param uniqueTypeName - type name unique with-in repository
     * @return new Resource instance
     */
    public static Resource newObjectType(String uniqueTypeName) {
        return new Resource(StandardTypes.TYPEDEF.getNumericCode() * 1L, uniqueTypeName);
    }
}


