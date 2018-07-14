package com.ljcr.srdb;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Resource {

    @EmbeddedId
    private ResourceKey key;

    private String reference;

    public Resource() {
        // used by jpa
    }

    public Resource(ResourceKey key, String reference) {
        this.key = key;
        this.reference = reference;
    }

    public ResourceKey getKey() {
        return key;
    }

    public void setKey(ResourceKey key) {
        this.key = key;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}


