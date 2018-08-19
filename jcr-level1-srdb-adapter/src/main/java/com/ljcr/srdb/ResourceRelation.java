package com.ljcr.srdb;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "rel")
public class ResourceRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_rel_id")
    @SequenceGenerator(name = "sq_rel_id", sequenceName = "sq_rel_id")
    @Column(name = "rid", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
    @JoinColumn(name = "parent_id")
    private Resource parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
    @JoinColumn(name = "field_id")
    private Resource child;

    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
    @JoinColumn(name = "value_id")
    private Resource value;

    @Column(name = "l", length = 5)
    private String loc;

    @Column(name = "vstr")
    private String stringValue;

    @Column(name = "vdec", precision = 20, scale = 12)
    private BigDecimal decimal;

    public ResourceRelation() {
        // jpa
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Resource getParent() {
        return parent;
    }

    public void setParent(Resource parent) {
        this.parent = parent;
    }

    public ResourceRelation withParent(Resource parent) {
        setParent(parent);
        return this;
    }

    public Resource getChild() {
        return child;
    }

    public void setChild(Resource child) {
        this.child = child;
    }

    public ResourceRelation withChild(Resource child) {
        setChild(child);
        return this;
    }

    public Resource getResourceValue() {
        return value;
    }

    public void setValue(Resource value) {
        this.value = value;
    }

    public ResourceRelation withResourceValue(Resource resourceValue) {
        setValue(resourceValue);
        return this;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public ResourceRelation withLocale(String l) {
        setLoc(l);
        return this;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public ResourceRelation withStringValue(String stringValue) {
        setStringValue(stringValue);
        return this;
    }

    public BigDecimal getDecimalValue() {
        return decimal;
    }

    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    public ResourceRelation withDecimalValue(BigDecimal decimal) {
        setDecimal(decimal);
        return this;
    }

    public Long getLongValue() {
        return decimal.longValue();
    }

    public ResourceRelation withLongValue(Long value) {
        setDecimal(BigDecimal.valueOf(value));
        return this;
    }

    @Override
    public String toString() {
        if (loc == null) {
            return String.format("(%sX%s)[s=%s,d=%s,r=%s,id=%s]", parent.getId(), child.getId(), stringValue, decimal, value == null ? null : value.getId(), id);
        }
        return String.format("(%sX%s[%s])[s=%s,d=%s,r=%s,id=%s]", parent.getId(), child.getId(), loc, stringValue, decimal, value == null ? null : value.getId(), id);
    }
}


