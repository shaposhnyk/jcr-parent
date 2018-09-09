package com.ljcr.srdb.mods.visitors;

import com.ljcr.api.definitions.StandardTypeVisitor;
import com.ljcr.api.definitions.StandardTypes;
import com.ljcr.api.definitions.TypeDefinition;

import java.util.*;

public class BatchingVisitor<T> implements StandardTypeVisitor<T> {

    private final Collection<StandardTypeVisitor<T>> delegates;

    public BatchingVisitor(Collection<StandardTypeVisitor<T>> delegates) {
        this.delegates = Objects.requireNonNull(delegates);
    }

    @Override
    public T visit(StandardTypes.StringType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.BinaryType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.LongType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.DoubleType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.DateTimeType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.BooleanType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.IdentifierType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.PathType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.ReferenceType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.WeakReferenceType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.UriType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.DateType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.DecimalType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.TypeDefinitionType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.ArrayType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(StandardTypes.MapType type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    @Override
    public T visit(TypeDefinition type, Object context) {
        for (StandardTypeVisitor<T> delegate : delegates) {
            T res = delegate.visit(type, context);
            if (res != null) {
                return res;
            }
        }
        return null;
    }

    public static <T> StandardTypeVisitor<T> of(StandardTypeVisitor<T> source) {
        return new BatchingVisitor<>(Arrays.asList(source));
    }
}
