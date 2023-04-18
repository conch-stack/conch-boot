package ltd.beihu.core.tools.functions;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class Functions {

    public static <T> Function<T, Boolean> from(Predicate<T> predicate) {
        return predicate::test;
    }

    /**
     * Creates a  {@code Function} that convert null as the given default value
     *
     * @param <T>        the type of the input and output of the function
     * @param defaultVal the result which returned if value is null
     * @return the function result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code defaultVal} is null
     */
    public static <T> Function<T, T> nullAs(T defaultVal) {
        Objects.requireNonNull(defaultVal);
        return value -> value == null ? defaultVal : value;
    }

    /**
     * Creates a  {@code Function} that convert null as the given default value
     *
     * @param <T>        the type of the input and output of the function
     * @param function   the result which used if value is not null
     * @param defaultVal the result which returned if value is null
     * @return the function result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code defaultVal} is null
     */
    public static <T, R> Function<T, R> nullAs(Function<T, R> function, R defaultVal) {
        Objects.requireNonNull(function);
        return value -> value == null ? defaultVal : function.apply(value);
    }

    /**
     * Returns a function which performs a map lookup. The returned function throws an {@link
     * IllegalArgumentException} if given a key that does not exist in the map. See also {@link
     * #forMap(Map, Object)}, which returns a default value in this case.
     * <p>
     * <p>Note: if {@code map} is a {@link com.google.common.collect.BiMap BiMap} (or can be one), you
     * can use {@link com.google.common.collect.Maps#asConverter Maps.asConverter} instead to get a
     * function that also supports reverse conversion.
     * <p>
     * <p><b>Java 8 users:</b> if you are okay with {@code null} being returned for an unrecognized
     * key (instead of an exception being thrown), you can use the method reference {@code map::get}
     * instead.
     */
    public static <K, V> Function<K, V> forMap(Map<K, V> map) {
        return com.google.common.base.Functions.forMap(map);
    }

    /**
     * Returns a function which performs a map lookup with a default value. The function created by
     * this method returns {@code defaultValue} for all inputs that do not belong to the map's key
     * set. See also {@link #forMap(Map)}, which throws an exception in this case.
     * <p>
     * <p><b>Java 8 users:</b> you can just write the lambda expression {@code k ->
     * map.getWithDefault(k, defaultValue)} instead.
     *
     * @param map          source map that determines the function behavior
     * @param defaultValue the value to return for inputs that aren't map keys
     * @return function that returns {@code map.get(a)} when {@code a} is a key, or {@code
     * defaultValue} otherwise
     */
    public static <K, V> Function<K, V> forMap(
            Map<K, ? extends V> map, @Nullable V defaultValue) {
        return com.google.common.base.Functions.forMap(map, defaultValue);
    }

    /**
     * Returns a function which performs a map lookup with a absentFunction. The function created by
     * this method returns value in the map for all inputs that belong to the map's key
     * set and return the value by the the given {@code absentFunction} if the key absent
     * <p>
     * <p><b>Java 8 users:</b> you can just write the lambda expression {@code k ->
     * map.getWithDefault(k, absentFunction.apply(k))} instead.
     *
     * @param map            source map that determines the function behavior
     * @param absentFunction the function to handle for inputs that aren't map keys
     * @return function that returns {@code map.get(k)} when {@code k} is a key, or {@code
     * absentFunction.get(k)} otherwise
     */
    public static <K, V> Function<K, V> forMap(
            Map<K, ? extends V> map, Function<K, V> absentFunction) {
        return new ForMapWithAbsentFunction<>(map, absentFunction);
    }

    private static class ForMapWithAbsentFunction<K, V> implements Function<K, V>, Serializable {
        final Map<K, ? extends V> map;
        final Function<K, V> absentFunction;

        ForMapWithAbsentFunction(Map<K, ? extends V> map, Function<K, V> absentFunction) {
            this.map = Objects.requireNonNull(map);
            this.absentFunction = absentFunction;
        }

        @Override
        public V apply(@Nullable K key) {
            V result = map.get(key);
            return (result != null || map.containsKey(key)) ? result : absentFunction.apply(key);
        }

        @Override
        public boolean equals(@Nullable Object o) {
            if (o instanceof ForMapWithAbsentFunction) {
                ForMapWithAbsentFunction<?, ?> that = (ForMapWithAbsentFunction<?, ?>) o;
                return map.equals(that.map) && Objects.equals(absentFunction, that.absentFunction);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(map, absentFunction);
        }

        @Override
        public String toString() {
            return "Functions.forMap(" + map + ", absentFunction=" + absentFunction + ")";
        }

        private static final long serialVersionUID = 0;
    }

    /**
     * Creates a safe {@code Function},
     *
     * @param <T>               the type of the input of the function
     * @param <R>               the type of the result of the function
     * @param throwableFunction the function that may throw an exception
     * @return the function result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code throwableFunction} is null
     */
    public static <T, R> Function<T, R> uncheck(
            ThrowableFunction<? super T, ? extends R, Throwable> throwableFunction) {
        Objects.requireNonNull(throwableFunction);
        return value -> {
            try {
                return throwableFunction.apply(value);
            } catch (Throwable throwable) {
                ExceptionUtils.wrapAndThrow(throwable);
                //never
                return null;
            }
        };
    }

    /**
     * Creates a safe {@code Function},
     *
     * @param <T>               the type of the input of the function
     * @param <R>               the type of the result of the function
     * @param throwableFunction the function that may throw an exception
     * @return the function result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code throwableFunction} is null
     */
    public static <T, R> Function<T, R> safe(
            ThrowableFunction<? super T, ? extends R, Throwable> throwableFunction) {
        return safe(throwableFunction, null);
    }

    /**
     * Creates a safe {@code Function},
     *
     * @param <T>               the type of the input of the function
     * @param <R>               the type of the result of the function
     * @param throwableFunction the function that may throw an exception
     * @param resultIfFailed    the result which returned if exception was thrown
     * @return the function result or {@code resultIfFailed}
     * @throws NullPointerException if {@code throwableFunction} is null
     * @see #safe(ThrowableFunction)
     */
    public static <T, R> Function<T, R> safe(
            final ThrowableFunction<? super T, ? extends R, Throwable> throwableFunction,
            final R resultIfFailed) {
        Objects.requireNonNull(throwableFunction);
        return value -> {
            try {
                return throwableFunction.apply(value);
            } catch (Throwable throwable) {
                return resultIfFailed;
            }
        };
    }

    /**
     * Creates a safe {@code BiFunction},
     *
     * @param <T>                 the type of the input of the biFunction
     * @param <R>                 the type of the result of the biFunction
     * @param throwableBiFunction the biFunction that may throw an exception
     * @return the biFunction result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code throwableBiFunction} is null
     */
    public static <T, U, R> BiFunction<T, U, R> uncheck(
            ThrowableBiFunction<? super T, ? super U, ? extends R, Throwable> throwableBiFunction) {
        Objects.requireNonNull(throwableBiFunction);
        return (t, u) -> {
            try {
                return throwableBiFunction.apply(t, u);
            } catch (Throwable throwable) {
                ExceptionUtils.wrapAndThrow(throwable);
                //never
                return null;
            }
        };
    }

    /**
     * Creates a safe {@code BiFunction},
     *
     * @param <T>                 the type of the input of the biFunction
     * @param <R>                 the type of the result of the biFunction
     * @param throwableBiFunction the biFunction that may throw an exception
     * @return the biFunction result or {@code null} if exception was thrown
     * @throws NullPointerException if {@code throwableBiFunction} is null
     */
    public static <T, U, R> BiFunction<T, U, R> safe(
            ThrowableBiFunction<? super T, ? super U, ? extends R, Throwable> throwableBiFunction) {
        return safe(throwableBiFunction, null);
    }

    /**
     * Creates a safe {@code BiFunction},
     *
     * @param <T>                 the type of the input of the biFunction
     * @param <R>                 the type of the result of the biFunction
     * @param throwableBiFunction the biFunction that may throw an exception
     * @param resultIfFailed      the result which returned if exception was thrown
     * @return the biFunction result or {@code resultIfFailed}
     * @throws NullPointerException if {@code throwableBiFunction} is null
     * @see #safe(ThrowableBiFunction)
     */
    public static <T, U, R> BiFunction<T, U, R> safe(
            final ThrowableBiFunction<? super T, ? super U, ? extends R, Throwable> throwableBiFunction,
            final R resultIfFailed) {
        Objects.requireNonNull(throwableBiFunction);
        return (t, u) -> {
            try {
                return throwableBiFunction.apply(t, u);
            } catch (Throwable throwable) {
                return resultIfFailed;
            }
        };
    }

    public static <T> Function<T, Pair<T, Integer>> indexed() {
        return new Function<T, Pair<T, Integer>>() {
            private int index = 0;

            @Override
            public Pair<T, Integer> apply(T t) {
                return Pair.of(t, index++);
            }
        };
    }

    public static <T, R> Function<T, R> indexed(BiFunction<T, Integer, R> function) {
        return new Function<T, R>() {
            private int index = 0;

            @Override
            public R apply(T t) {
                return function.apply(t, index++);
            }
        };
    }
}
