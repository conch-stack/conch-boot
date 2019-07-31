package ltd.beihu.core.tools.functions;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Consumers {

    /**
     * Creates a safe {@code Consumer} without catch Throwable.
     *
     * @param <T>               the type of the input to the consumer
     * @param throwableConsumer the consumer that may throw an exception
     * @return a {@code Consumer}
     * @throws NullPointerException if {@code throwableConsumer} is null
     * @see #safe(ThrowableConsumer, Consumer)
     */
    public static <T> Consumer<T> uncheck(ThrowableConsumer<? super T, Throwable> throwableConsumer) {
        Objects.requireNonNull(throwableConsumer);
        return value -> {
            try {
                throwableConsumer.accept(value);
            } catch (Throwable ex) {
                ExceptionUtils.wrapAndThrow(ex);
            }
        };
    }

    /**
     * Creates a safe {@code Consumer}.
     *
     * @param <T>               the type of the input to the consumer
     * @param throwableConsumer the consumer that may throw an exception
     * @return a {@code Consumer}
     * @throws NullPointerException if {@code throwableConsumer} is null
     * @see #safe(ThrowableConsumer, Consumer)
     */
    public static <T> Consumer<T> safe(ThrowableConsumer<? super T, Throwable> throwableConsumer) {
        Objects.requireNonNull(throwableConsumer);
        return value -> {
            try {
                throwableConsumer.accept(value);
            } catch (Throwable ignored) {
            }
        };
    }

    /**
     * Creates a safe {@code Consumer}.
     *
     * @param <T>               the type of the input to the consumer
     * @param throwableConsumer the consumer that may throw an exception
     * @param onFailedConsumer  the consumer which applies if exception was thrown
     * @return a {@code Consumer}
     * @throws NullPointerException if {@code throwableConsumer} is null
     * @see #safe(ThrowableConsumer)
     */
    public static <T> Consumer<T> safe(
            final ThrowableConsumer<? super T, Throwable> throwableConsumer,
            final Consumer<? super T> onFailedConsumer) {
        Objects.requireNonNull(throwableConsumer);
        Objects.requireNonNull(onFailedConsumer);
        return value -> {
            try {
                throwableConsumer.accept(value);
            } catch (Throwable ex) {
                onFailedConsumer.accept(value);
            }
        };
    }

    /**
     * Creates a safe {@code BiConsumer} without catch Throwable.
     *
     * @param <T>               the type of the first argument to the operation
     * @param <U>               the type of the second argument to the operation
     * @param throwableConsumer the consumer that may throw an exception
     * @return a {@code BiConsumer}
     * @throws NullPointerException if {@code throwableConsumer} is null
     * @see #safe(ThrowableBiConsumer, BiConsumer)
     */
    public static <T, U> BiConsumer<T, U> uncheck(ThrowableBiConsumer<? super T, ? super U, Throwable> throwableConsumer) {
        Objects.requireNonNull(throwableConsumer);
        return (t, u) -> {
            try {
                throwableConsumer.accept(t, u);
            } catch (Throwable ex) {
                ExceptionUtils.wrapAndThrow(ex);
            }
        };
    }

    /**
     * Creates a safe {@code BiConsumer}.
     *
     * @param <T>               the type of the first argument to the operation
     * @param <U>               the type of the second argument to the operation
     * @param throwableConsumer the consumer that may throw an exception
     * @return a {@code BiConsumer}
     * @throws NullPointerException if {@code throwableConsumer} is null
     * @see #safe(ThrowableBiConsumer, BiConsumer)
     */
    public static <T, U> BiConsumer<T, U> safe(ThrowableBiConsumer<? super T, ? super U, Throwable> throwableConsumer) {
        Objects.requireNonNull(throwableConsumer);
        return (t, u) -> {
            try {
                throwableConsumer.accept(t, u);
            } catch (Throwable ignored) {
            }
        };
    }

    /**
     * Creates a safe {@code BiConsumer}.
     *
     * @param <T>               the type of the first argument to the operation
     * @param <U>               the type of the second argument to the operation
     * @param throwableConsumer the consumer that may throw an exception
     * @param onFailedConsumer  the consumer which applies if exception was thrown
     * @return a {@code BiConsumer}
     * @throws NullPointerException if {@code throwableConsumer} is null
     * @see #safe(ThrowableBiConsumer)
     */
    public static <T, U> BiConsumer<T, U> safe(
            final ThrowableBiConsumer<? super T, ? super U, Throwable> throwableConsumer,
            final BiConsumer<? super T, ? super U> onFailedConsumer) {
        Objects.requireNonNull(throwableConsumer);
        Objects.requireNonNull(onFailedConsumer);
        return (t, u) -> {
            try {
                throwableConsumer.accept(t, u);
            } catch (Throwable ex) {
                onFailedConsumer.accept(t, u);
            }
        };
    }

    public static <T> Consumer<T> indexed(BiConsumer<T, Integer> function) {
        return new Consumer<T>() {
            private int index = 0;

            @Override
            public void accept(T t) {
                function.accept(t, index++);
            }
        };
    }
}
