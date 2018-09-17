package com.ljcr.srdb;

import com.ljcr.api.ImmutableNodeScalar;
import com.ljcr.api.definitions.PropertyDefinition;

public interface PropertyFactory {
    ImmutableNodeScalar getValue(PropertyDefinition field);
}
