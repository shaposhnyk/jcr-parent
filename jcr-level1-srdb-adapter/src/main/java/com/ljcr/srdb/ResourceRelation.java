package com.ljcr.srdb;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class ResourceRelation {
    @EmbeddedId
    private ResourceKey parentKey;

    private ResourceKey childKey;

    private String stringValue;

    private BigDecimal decimalValue;

    public ResourceRelation() {
        // jpa
    }

    public ResourceRelation(ResourceKey parentKey, ResourceKey childKey) {
        this.parentKey = parentKey;
        this.childKey = childKey;
    }

    public ResourceKey getParentKey() {
        return parentKey;
    }

    public void setParentKey(ResourceKey parentKey) {
        this.parentKey = parentKey;
    }

    public ResourceKey getChildKey() {
        return childKey;
    }

    public void setChildKey(ResourceKey childKey) {
        this.childKey = childKey;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public BigDecimal getDecimalValue() {
        return decimalValue;
    }

    public void setDecimalValue(BigDecimal decimalValue) {
        this.decimalValue = decimalValue;
    }
}


