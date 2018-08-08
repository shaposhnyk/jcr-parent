package com.ljcr.dynamics;

import com.ljcr.api.*;
import com.ljcr.api.definitions.ContainerTypeDefinition;
import com.ljcr.api.definitions.TypeDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

public class WrappingVisitor implements ImmutableItemVisitor<ImmutableNode> {
    private static final Logger logger = LoggerFactory.getLogger(WrappingVisitor.class);

    private final Repository staticRepo;

    public WrappingVisitor(Repository staticRepo) {
        this.staticRepo = staticRepo;
    }

    public Repository getStaticRepo() {
        return staticRepo;
    }

    @Override
    public ImmutableNode visit(@Nullable ImmutableNode node) {
        if (node == null || node instanceof DynamicItem) {
            return node;
        }

        TypeDefinition typeDefinition = node.getTypeDefinition();
        String type = typeDefinition.getIdentifier();
        if (type.endsWith("Ref") && node.getValue() instanceof String) {
            ContainerTypeDefinition containerType = staticRepo.findContainerType(type.substring(0, type.length() - 3));
            String reference = node.asString();
            ImmutableObjectNode referencedObj = containerType.findByReference(reference);
            if (referencedObj == null) {
                logger.warn("Unresolved reference of type {}: {}", containerType, reference);
                return null;
            }
            return wrap(referencedObj);
        }

        return new DynamicNode(this, node);
    }

    @Override
    public ImmutableObjectNode visit(@Nullable ImmutableObjectNode node) {
        if (node == null || node instanceof DynamicItem) {
            return node;
        }

        return new DynamicObjectNode(this, node);
    }

    @Override
    public ImmutableArrayNode visit(@Nullable ImmutableArrayNode node) {
        if (node == null || node instanceof DynamicItem) {
            return node;
        }

        return new DynamicArrayNode(this, node);
    }

    public ImmutableNode wrap(ImmutableNode node) {
        return node.accept(this);
    }

    public <T extends ImmutableNode> T wrapAs(Class<T> targetClazz, ImmutableNode node) {
        T result = (T) node.accept(this);
        return targetClazz.isInstance(result) ? targetClazz.cast(result) : null;
    }
}
