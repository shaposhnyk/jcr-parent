package com.ljcr.srdb;

import com.ljcr.api.ImmutableValue;
import com.ljcr.api.definitions.PropertyDefinition;

public interface RelationalPropertyFactory {
    ImmutableValue getValue(PropertyDefinition field);
}
