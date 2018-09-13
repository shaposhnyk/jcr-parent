package com.ljcr.srdb;

import com.ljcr.api.ImmutableScalar;
import com.ljcr.api.definitions.PropertyDefinition;

public interface PropertyFactory {
    ImmutableScalar getValue(PropertyDefinition field);
}
