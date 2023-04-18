package ltd.beihu.core.tools.functions;

import java.util.function.DoubleConsumer;

/**
 * Represents an operation on a {@code double}-valued input argument.
 *
 * @param <E> the type of the exception
 * @see DoubleConsumer
 * @since 1.1.7
 */
public interface ThrowableDoubleConsumer<E extends Throwable> {

    /**
     * Performs operation on the given argument.
     *
     * @param value the input argument
     * @throws E an exception
     */
    void accept(double value) throws E;
}
