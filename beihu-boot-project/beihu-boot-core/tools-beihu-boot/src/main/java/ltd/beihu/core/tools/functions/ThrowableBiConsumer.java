package ltd.beihu.core.tools.functions;

public interface ThrowableBiConsumer<T, U, E extends Throwable> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     */
    void accept(T t, U u) throws E;
}
