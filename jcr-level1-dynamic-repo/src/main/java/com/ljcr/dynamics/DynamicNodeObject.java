package com.ljcr.dynamics;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableNodeObject;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.utils.ImmutableObjectWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public class DynamicNodeObject extends ImmutableObjectWrapper implements ImmutableNodeObject, DynamicItem {
    private final WrappingVisitor visitor;

    public DynamicNodeObject(WrappingVisitor wrappingVisitor, ImmutableNodeObject node) {
        super(node);
        this.visitor = wrappingVisitor;
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        return visitor.wrap(super.getItem(fieldName));
    }

    @Override
    public Stream<ImmutableNode> getItems() {
        return super.getItems()
                .map(visitor::wrap);
    }
}
