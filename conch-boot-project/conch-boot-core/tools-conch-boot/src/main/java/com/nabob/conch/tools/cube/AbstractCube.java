package com.nabob.conch.tools.cube;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractCube<T> implements Cube<T> {

    private Runnable onCompleted;

    @Override
    public Cube<T> onCompleted(final Runnable onCompleted) {
        if (this.onCompleted == null) {
            this.onCompleted = onCompleted;
        } else {
            Runnable old = this.onCompleted;
            this.onCompleted = () -> {
                old.run();
                onCompleted.run();
            };
        }
        return this;
    }

    @Override
    public Cube<T> onCompleted(final Consumer<T> onCompleted) {
        T value = doOrNull();
        if (this.onCompleted == null) {
            this.onCompleted = () -> {
                onCompleted.accept(value);
            };
        } else {
            Runnable old = this.onCompleted;
            this.onCompleted = () -> {
                old.run();
                onCompleted.accept(value);
            };
        }
        return this;
    }

    private <V> Cube<V> init(Cube<V> cube) {
        if (cube != this) {
            cube.onCompleted(this.onCompleted);
        }
        return cube;
    }

    public void complete() {
        if (onCompleted != null) {
            onCompleted.run();
            onCompleted = null;
        }
    }

    /**
     * Returns {@code true} if this holder contains a (non-null) instance.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> no differences.
     */
    @Override
    public boolean isPresent() {

        try {
            return checkPresent();
        } finally {
            complete();
        }
    }

    public abstract boolean checkPresent();

    @Override
    public void ifPresent(Consumer<T> consumer) {
        try {
            doIfPresent(consumer);
        } finally {
            complete();
        }

    }

    public abstract void doIfPresent(Consumer<T> consumer);

    /**
     * Returns the contained instance, which must be present. If the instance might be absent, use
     * {@link #doOr(Object)} or {@link #doOrNull} instead.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> when the value is absent, this method
     * throws {@link IllegalStateException}, whereas the Java 8 counterpart throws
     * {@link java.util.NoSuchElementException NoSuchElementException}.
     *
     * @throws IllegalStateException if the instance is absent ({@link #isPresent} returns
     *                               {@code false}); depending on this <i>specific</i> exception type (over the more general
     *                               {@link RuntimeException}) is discouraged
     */
    @Override
    public T get() {
        try {
            return doGet();
        } finally {
            complete();
        }
    }

    protected abstract T doGet();

    /**
     * Returns the contained instance if it is present; {@code defaultValue} otherwise. If no default
     * value should be required because the instance is known to be present, use {@link #doGet()}
     * instead. For a default value of {@code null}, use {@link #doOrNull}.
     * <p>
     * <p>Note about generics: The signature {@code public T or(T defaultValue)} is overly
     * restrictive. However, the ideal signature, {@code public <S super T> S or(S)}, is not legal
     * Java. As a result, some sensible operations involving subtypes are compile errors:
     * <pre>   {@code
     *
     *   Cube<Integer> optionalInt = getSomeCubeInt();
     *   Number value = optionalInt.or(0.5); // error
     *
     *   FluentIterable<? extends Number> numbers = getSomeNumbers();
     *   Cube<? extends Number> first = numbers.first();
     *   Number value = first.or(0.5); // error}</pre>
     * <p>
     * <p>As a workaround, it is always safe to cast an {@code Cube<? extends T>} to {@code
     * Cube<T>}. Casting either of the above example {@code Cube} instances to {@code
     * Cube<Number>} (where {@code Number} is the desired output type) solves the problem:
     * <pre>   {@code
     *
     *   Cube<Number> optionalInt = (Cube) getSomeCubeInt();
     *   Number value = optionalInt.or(0.5); // fine
     *
     *   FluentIterable<? extends Number> numbers = getSomeNumbers();
     *   Cube<Number> first = (Cube) numbers.first();
     *   Number value = first.or(0.5); // fine}</pre>
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> this method is similar to Java 8's
     * {@code Cube.orElse}, but will not accept {@code null} as a {@code defaultValue}
     * ({@link #doOrNull} must be used instead). As a result, the value returned by this method is
     * guaranteed non-null, which is not the case for the {@code java.util} equivalent.
     */
    @Override
    public T or(T defaultValue) {
        try {
            return doOr(defaultValue);
        } finally {
            complete();
        }
    }

    public abstract T doOr(T defaultValue);

    @Override
    public Cube<T> or(Cube<? extends T> secondChoice) {
        return init(doOr(secondChoice));
    }

    /**
     * Returns this {@code Cube} if it has a value present; {@code secondChoice} otherwise.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> this method has no equivalent in Java 8's
     * {@code Cube} class; write {@code thisCube.isPresent() ? thisCube : secondChoice}
     * instead.
     */
    protected abstract Cube<T> doOr(Cube<? extends T> secondChoice);

    @Override
    public T or(Supplier<? extends T> supplier) {
        try {
            return doOr(supplier);
        } finally {
            complete();
        }
    }

    /**
     * Returns the contained instance if it is present; {@code supplier.doGet()} otherwise.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> this method is similar to Java 8's
     * {@code Cube.orElseGet}, except when {@code supplier} returns {@code null}. In this case
     * this method throws an exception, whereas the Java 8 method returns the {@code null} to the
     * caller.
     *
     * @throws NullPointerException if this optional's value is absent and the supplier returns
     *                              {@code null}
     */
    public abstract T doOr(Supplier<? extends T> supplier);

    @Override
    public T orNull() {
        try {
            return doOrNull();
        } finally {
            complete();
        }
    }

    /**
     * Returns the contained instance if it is present; {@code null} otherwise. If the instance is
     * known to be present, use {@link #doGet()} instead.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> this method is equivalent to Java 8's
     * {@code Cube.orElse(null)}.
     */
    @Nullable
    public abstract T doOrNull();

    /**
     * Returns an immutable singleton {@link Set} whose only element is the contained instance if it
     * is present; an empty immutable {@link Set} otherwise.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> this method has no equivalent in Java 8's
     * {@code Cube} class. However, this common usage: <pre>   {@code
     * <p>
     *   for (Foo foo : possibleFoo.asSet()) {
     *     doSomethingWith(foo);
     *   }}</pre>
     * <p>
     * ... can be replaced with: <pre>   {@code
     * <p>
     *   possibleFoo.ifPresent(foo -> doSomethingWith(foo));}</pre>
     *
     * @since 11.0
     */
    @Override
    public Set<T> asSet() {
        try {
            return doAsSet();
        } finally {
            complete();
        }
    }

    /**
     * Returns an immutable singleton {@link Set} whose only element is the contained instance if it
     * is present; an empty immutable {@link Set} otherwise.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> this method has no equivalent in Java 8's
     * {@code Cube} class. However, this common usage: <pre>   {@code
     * <p>
     *   for (Foo foo : possibleFoo.asSet()) {
     *     doSomethingWith(foo);
     *   }}</pre>
     * <p>
     * ... can be replaced with: <pre>   {@code
     * <p>
     *   possibleFoo.ifPresent(foo -> doSomethingWith(foo));}</pre>
     *
     * @since 11.0
     */
    public abstract Set<T> doAsSet();

    /**
     * If the instance is present, it is transformed with the given {@link Function}; otherwise,
     * {@link AbstractCube#absent} is returned.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> this method is similar to Java 8's
     * {@code Cube.map}, except when {@code function} returns {@code null}. In this case this
     * method throws an exception, whereas the Java 8 method returns {@code Cube.absent()}.
     *
     * @throws NullPointerException if the function returns {@code null}
     * @since 12.0
     */
    @Override
    public <V> Cube<V> map(Function<? super T, V> function) {
        return init(doMap(function));

    }

    protected abstract <V> Cube<V> doMap(Function<? super T, V> function);

    @Override
    public <V> Cube<V> flatMap(Function<? super T, Cube<V>> function) {
        return init(doFlatMap(function));
    }

    public abstract <V> Cube<V> doFlatMap(Function<? super T, Cube<V>> function);

    /**
     * Returns {@code true} if {@code object} is an {@code Cube} instance, and either the
     * contained references are {@linkplain Object#equals equal} to each other or both are absent.
     * Note that {@code Cube} instances of differing parameterized types can be equal.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> no differences.
     */
    @Override
    public abstract boolean equals(@Nullable Object object);

    /**
     * Returns a hash code for this instance.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> this class leaves the specific choice of
     * hash code unspecified, unlike the Java 8 equivalent.
     */
    @Override
    public abstract int hashCode();

    /**
     * Returns a string representation for this instance.
     * <p>
     * <p><b>Comparison to {@code java.util.Optional}:</b> this class leaves the specific string
     * representation unspecified, unlike the Java 8 equivalent.
     */
    @Override
    public abstract String toString();

    private static final long serialVersionUID = 0;
}
