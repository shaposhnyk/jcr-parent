package com.ljcr.dynamics;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableObjectNode;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.utils.ImmutableObjectWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public class DynamicObjectNode extends ImmutableObjectWrapper implements ImmutableObjectNode, DynamicItem {
    private final WrappingVisitor visitor;

    public DynamicObjectNode(WrappingVisitor wrappingVisitor, ImmutableObjectNode node) {
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
