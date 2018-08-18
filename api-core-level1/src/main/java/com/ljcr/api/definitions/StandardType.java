package com.ljcr.api.definitions;

import java.util.Objects;

public abstract class StandardType implements TypeDefinition {

    private final int id;
    private final String code;

    StandardType(int id, String code) {
        this.id = id;
        this.code = Objects.requireNonNull(code);
    }

    @Override
    public String getIdentifier() {
        return code;
    }

    public int getNumericCode() {
        return id;
    }

    @Override
    public boolean isQueryable() {
        return false;
    }

    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof TypeDefinition) {
            return getIdentifier().equals(((TypeDefinition) obj).getIdentifier());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }
}
