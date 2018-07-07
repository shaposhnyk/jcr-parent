package javax.jcr.api;

public interface ImmutableArrayNode extends ImmutableItem {

    @Override
    default boolean isArrayNode() {
        return true;
    }
}
