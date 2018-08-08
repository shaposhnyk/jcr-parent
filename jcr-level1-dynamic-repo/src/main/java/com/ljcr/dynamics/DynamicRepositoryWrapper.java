package com.ljcr.dynamics;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableObjectNode;
import com.ljcr.api.definitions.TypeDefinition;
import com.ljcr.api.exceptions.ItemNotFoundException;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.utils.RepositoryWrapper;

import javax.annotation.Nullable;
import java.nio.file.Path;

class DynamicRepositoryWrapper extends RepositoryWrapper {
    private final WrappingVisitor visitor;

    public DynamicRepositoryWrapper(WrappingVisitor visitor) {
        super(visitor.getStaticRepo());
        this.visitor = visitor;
    }

    @Override
    public ImmutableNode getRootNode() {
        return visitor.wrap(super.getRootNode());
    }

    @Nullable
    @Override
    public ImmutableNode getItem(Path absPath) throws PathNotFoundException {
        return visitor.wrap(super.getItem(absPath));
    }

    @Nullable
    @Override
    public ImmutableObjectNode getNodeByReference(String typeName, Object id) {
        return visitor.wrapAs(ImmutableObjectNode.class, super.getNodeByReference(typeName, id));
    }

    @Nullable
    @Override
    public ImmutableObjectNode getNodeByReference(TypeDefinition type, String id) throws ItemNotFoundException {
        return visitor.wrapAs(ImmutableObjectNode.class, super.getNodeByReference(type, id));
    }
}
