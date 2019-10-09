package ltd.beihu.core.tools.function;

import java.util.Objects;


@FunctionalInterface
public interface BiConsumer<A, B> {
    /**
     * Performs this operation on the given argument.
     *
     * @param a the first input argument
     * @param b the second input argument
     */
    void accept(A a, B b);

    /**
     * Returns a composed {@code Consumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code Consumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default BiConsumer<A, B> andThen(BiConsumer<A, B> after) {
        Objects.requireNonNull(after);
        return (A a, B b) -> {
            accept(a, b);
            after.accept(a, b);
        };
    }
}
