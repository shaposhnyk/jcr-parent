package com.ljcr.srdb;

import javax.persistence.*;

@Entity
@Table(name = "res")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public Resource(Long id, Long type_id, String reference) {
        this.id = id;
        this.typeId = type_id;
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
        return String.format("%s - %s[%s]", typeId, reference, version);
    }
}


