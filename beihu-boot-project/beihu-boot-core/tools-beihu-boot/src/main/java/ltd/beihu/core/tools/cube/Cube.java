package ltd.beihu.core.tools.cube;

import com.google.common.annotations.Beta;
import ltd.beihu.core.tools.utils.Assert;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

public interface Cube<T> extends Serializable {
    /**
     * If {@code nullableReference} is non-null, returns an {@code Cube} instance containing that
     * reference; otherwise returns {@link Cube#absent}.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> this method is equivalent to Java 8's
     * {@code Cube.ofNullable}.
     */
    static <T> Cube<T> of(@Nullable T nullableReference) {
        return (nullableReference == null)
                ? Cube.<T>absent()
                : LightPresent.with(nullableReference);
    }

    /**
     * Returns an {@code Cube} instance with no contained reference.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> this method is equivalent to Java 8's
     * {@code Cube.empty}.
     */
    static <T> Cube<T> absent() {
        return LightAbsent.withType();
    }

    /**
     * Returns an {@code Cube} instance containing the given non-null reference. To have {@code
     * null} treated as {@link #absent}, use {@link #of} instead.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> no differences.
     *
     * @throws NullPointerException if {@code reference} is null
     */
    static <T> Cube<T> present(T reference) {
        Assert.notNull(reference);
        return LightPresent.with(checkNotNull(reference));
    }

    /**
     * Returns the equivalent {@code com.google.common.base.Cube} value to the given {@code
     * java.util.Optional}, or {@code null} if the argument is null.
     *
     * @since 21.0
     */
    @Nullable
    static <T> Cube<T> from(@Nullable java.util.Optional<T> javaUtilOptional) {
        return (javaUtilOptional == null) ? null : of(javaUtilOptional.orElse(null));
    }

    /**
     * Returns the value of each present instance from the supplied {@code optionals}, in order,
     * skipping over occurrences of {@link Cube#absent}. Iterators are unmodifiable and are
     * evaluated lazily.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> this method has no equivalent in Java 8's
     * {@code Cube} class; use
     * {@code optionals.stream().filter(Cube::isPresent).map(Cube::doGet)} instead.
     *
     * @since 11.0 (generics widened in 13.0)
     */
    @Beta
    static <T> Iterable<T> presentInstances(
            Iterable<? extends Cube<? extends T>> optionals) {
        Assert.notNull(optionals);
        Iterator<? extends Cube<? extends T>> iterator = optionals.iterator();
        Assert.notNull(iterator);
        return () -> new AbstractIterator<T>() {
            @Override
            protected T computeNext() {
                while (iterator.hasNext()) {
                    Cube<? extends T> optional = iterator.next();
                    if (optional.isPresent()) {
                        return optional.get();
                    }
                }
                return endOfData();
            }
        };
    }

    Cube<T> onCompleted(Runnable onCompleted);

    Cube<T> onCompleted(Consumer<T> onCompleted);

    boolean isPresent();

    void ifPresent(Consumer<T> consumer);

    void complete();

    T get();

    T or(T defaultValue);

    Cube<T> or(Cube<? extends T> secondChoice);

    T or(Supplier<? extends T> supplier);

    T orNull();

    Set<T> asSet();

    <V> Cube<V> map(Function<? super T, V> function);

    <V> Cube<V> flatMap(Function<? super T, Cube<V>> function);
}
