package com.ljcr.dynamics;

import com.ljcr.api.ImmutableNode;
import com.ljcr.api.ImmutableNodeCollection;
import com.ljcr.api.ImmutableNodeObject;
import com.ljcr.api.exceptions.PathNotFoundException;
import com.ljcr.api.exceptions.UnsupportedRepositoryOperationException;
import com.ljcr.utils.ImmutableNodeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.stream.Stream;

public class DynamicNodeCollection extends ImmutableNodeWrapper implements ImmutableNodeCollection, DynamicItem {
    private final WrappingVisitor visitor;

    public DynamicNodeCollection(WrappingVisitor wrappingVisitor, ImmutableNodeCollection node) {
        super(node);
        this.visitor = wrappingVisitor;
    }

    @Override
    public ImmutableNodeObject asObjectNode() {
        throw new UnsupportedRepositoryOperationException();
    }

    @Override
    public ImmutableNodeCollection asArrayNode() {
        return this;
    }

    @Nullable
    @Override
    public ImmutableNode getItem(@Nonnull String fieldName) throws PathNotFoundException {
        return visitor.wrap(super.getItem(fieldName));
    }

    @Override
    public Stream<ImmutableNode> getElements() {
        return super.getElements()
                .map(visitor::wrap);
    }

    @Nullable
    @Override
    public Collection<ImmutableNode> getValue() {
        return null;
    }
}
