package com.ljcr.dynamics;

import com.ljcr.api.ImmutableArrayNode;
import com.ljcr.api.ImmutableItemVisitor;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.utils.ImmutableArrayWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.stream.Stream;

public class DynamicArrayNode extends ImmutableArrayWrapper implements ImmutableArrayNode, DynamicItem {
    private final WrappingVisitor visitor;

    public DynamicArrayNode(WrappingVisitor wrappingVisitor, ImmutableArrayNode node) {
        super(node);
        this.visitor = wrappingVisitor;
    }

    @Override
    public <T> T accept(@Nonnull ImmutableItemVisitor<T> visitor) {
        return this.accept(visitor);
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

    @Override
    public Stream<ImmutableNode> getElements() {
        return super.getElements()
                .map(visitor::wrap);
    }
}
