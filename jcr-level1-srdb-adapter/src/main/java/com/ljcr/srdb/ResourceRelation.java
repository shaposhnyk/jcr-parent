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
    private Resource parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.PROXY)
    private Resource child;

    @Column(name = "l", length = 5)
    private String loc;

    @Column(name = "vstr")
    private String stringValue;

    @Column(name = "vdec", precision = 5)
    private BigDecimal decimalValue;

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

    public ResourceRelation parent(Resource parent) {
        setParent(parent);
        return this;
    }

    public Resource getChild() {
        return child;
    }

    public void setChild(Resource child) {
        this.child = child;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
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

    @Override
    public String toString() {
        if (loc == null) {
            return String.format("(%sX%s)[s=%s,d=%s,r=%s]", parent.getId(), child.getId(), stringValue, decimalValue, id);
        }
        return String.format("(%sX%s[%s])[s=%s,d=%s,r=%s]", parent.getId(), child.getId(), loc, stringValue, decimalValue, id);
    }
}


