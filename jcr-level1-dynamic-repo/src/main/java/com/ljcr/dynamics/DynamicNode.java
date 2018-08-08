package com.ljcr.dynamics;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.utils.ImmutableNodeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public class DynamicNode extends ImmutableNodeWrapper implements ImmutableNode, DynamicItem {
    private final WrappingVisitor visitor;

    public DynamicNode(WrappingVisitor wrappingVisitor, ImmutableNode node) {
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
