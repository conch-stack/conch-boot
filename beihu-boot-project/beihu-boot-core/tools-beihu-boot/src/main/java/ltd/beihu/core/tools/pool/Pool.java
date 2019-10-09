package ltd.beihu.core.tools.pool;


import java.util.function.Consumer;
import java.util.function.Function;

public interface Pool<T> {
    <R> R call(Function<T, R> function);

    void run(Consumer<T> action);
}
