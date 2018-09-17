package com.ljcr.utils;

import com.ljcr.api.ImmutableNodeCollection;
import com.ljcr.api.ImmutableNode;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.StandardValueNodes;
import com.ljcr.api.definitions.TypeDefinition;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.stream.Stream;

public class ImmutableScalarCollectionCollection<E> implements ImmutableNodeCollection {
    private final TypeDefinition type;
    private final Collection<E> elements;

    ImmutableScalarCollectionCollection(TypeDefinition type, Collection<E> elements) {
        this.type = type;
        this.elements = elements;
    }

    public static ImmutableScalarCollectionCollection ofStrings(Collection<String> elements) {
        return new ImmutableScalarCollectionCollection(StandardTypes.arrayOf(StandardTypes.STRING), elements);
    }

    @Nonnull
    @Override
    public Stream<ImmutableNode> getElements() {
        return elements.stream()
                .map(e -> StandardValueNodes.of((String) null));
    }

    @Nonnull
    @Override
    public TypeDefinition getTypeDefinition() {
        return type;
    }
}
