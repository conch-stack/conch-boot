package ltd.beihu.core.tools.function;

import java.util.Objects;
import java.util.function.Function;


@FunctionalInterface
public interface TwoArgFunc<A, B, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param a the first function argument
     * @param b the second function argument
     * @return the function result
     */
    R apply(A a, B b);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V>   the type of output of the {@code after} function, and of the
     *              composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    default <V> TwoArgFunc<A, B, V> andThen(Function<R, V> after) {
        Objects.requireNonNull(after);
        return (a, b) -> after.apply(apply(a, b));
    }

    /**
     * Returns a function that always returns its input argument.
     *
     * @param <T> the type of the input and output objects to the function
     * @return a function that always returns its input argument
     */
    static <T> Function<T, T> identity() {
        return t -> t;
    }
}
