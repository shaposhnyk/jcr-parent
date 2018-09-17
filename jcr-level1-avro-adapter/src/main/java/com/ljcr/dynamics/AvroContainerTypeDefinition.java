package com.ljcr.dynamics;

import com.ljcr.api.ImmutableNodeObject;
import com.ljcr.api.definitions.ContainerTypeDefinition;
import org.apache.avro.Schema;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class AvroContainerTypeDefinition extends AvroTypeDefinition implements ContainerTypeDefinition {
    private final List<ImmutableNodeObject> containers;

    public AvroContainerTypeDefinition(Schema object, List<ImmutableNodeObject> maps) {
        super(object);
        this.containers = maps;
    }

    @Nullable
    @Override
    public ImmutableNodeObject findByReference(String id) {
        return containers.stream()
                .map(c -> c.getItem(id))
                .filter(o -> o != null && o.isObject())
                .map(o -> o.asObjectNode())
                .findFirst()
                .orElse(null);
    }

    @Override
    public Stream<ImmutableNodeObject> getItems() {
        return containers.stream()
                .flatMap(c -> c.getElements())
                .filter(o -> o != null && o.isObject())
                .map(o -> o.asObjectNode());
    }
}
