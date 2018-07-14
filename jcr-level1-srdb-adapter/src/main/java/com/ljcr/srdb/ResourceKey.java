package com.ljcr.srdb;

import javax.persistence.Embeddable;

@Embeddable
public class ResourceKey {

    private long typeId;

    private long itemId;

    public ResourceKey() {
        // jpa
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }
}
