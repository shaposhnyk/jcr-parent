package com.ljcr.dynamics;

import com.ljcr.api.ImmutableObjectNode;
import com.ljcr.api.definitions.ContainerTypeDefinition;
import org.apache.avro.Schema;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public class AvroContainerTypeDefinition extends AvroTypeDefinition implements ContainerTypeDefinition {
    private final List<ImmutableObjectNode> containers;

    public AvroContainerTypeDefinition(Schema object, List<ImmutableObjectNode> maps) {
        super(object);
        this.containers = maps;
    }

    @Nullable
    @Override
    public ImmutableObjectNode findByReference(String id) {
        return containers.stream()
                .map(c -> c.getItem(id))
                .filter(o -> o != null && o.isObjectNode())
                .map(o -> o.asObjectNode())
                .findFirst()
                .orElse(null);
    }

    @Override
    public Stream<ImmutableObjectNode> getItems() {
        return containers.stream()
                .flatMap(c -> c.getItems())
                .filter(o -> o != null && o.isObjectNode())
                .map(o -> o.asObjectNode());
    }
}
