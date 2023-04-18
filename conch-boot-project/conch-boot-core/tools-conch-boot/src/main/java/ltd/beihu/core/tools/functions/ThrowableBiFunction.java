package ltd.beihu.core.tools.functions;

import java.util.function.BiFunction;

/**
 * Represents a function which produces result from input arguments and can throw an exception.
 *
 * @param <T> the type of the first argument to the function
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 * @param <E> the type of the exception
 * @see BiFunction
 */
public interface ThrowableBiFunction<T, U, R, E extends Throwable> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @return the function result
     */
    R apply(T t, U u) throws E;
}
