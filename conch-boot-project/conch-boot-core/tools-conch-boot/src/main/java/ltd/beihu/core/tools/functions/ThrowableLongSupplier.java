package ltd.beihu.core.tools.functions;

/**
 * Represents a supplier of {@code long}-valued results.
 *
 * @param <E> the type of the exception
 * @since 1.1.7
 * @see java.util.function.LongSupplier
 */
public interface ThrowableLongSupplier<E extends Throwable> {

    /**
     * Gets a result.
     *
     * @return a result
     * @throws E an exception
     */
    long getAsLong() throws E;
}
