package ltd.beihu.core.tools.cube;

import ltd.beihu.core.tools.utils.Assert;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Present<T> extends AbstractCube<T> {
    private final T reference;

    static <T> Cube<T> with(T reference) {
        return new Present<>(reference);
    }

    private Present(T reference) {
        this.reference = reference;
    }

    @Override
    public boolean checkPresent() {
        return true;
    }

    @Override
    public void doIfPresent(Consumer<T> consumer) {
        consumer.accept(reference);
    }

    @Override
    public T doGet() {
        return reference;
    }

    @Override
    public T doOr(T defaultValue) {
        return reference;
    }

    @Override
    public Cube<T> doOr(Cube<? extends T> secondChoice) {
        return this;
    }

    @Override
    public T doOr(Supplier<? extends T> supplier) {
        Assert.notNull(supplier);
        return reference;
    }

    @Override
    public T doOrNull() {
        return reference;
    }

    @Override
    public Set<T> doAsSet() {
        return Collections.singleton(reference);
    }

    @Override
    public <V> Cube<V> doMap(Function<? super T, V> function) {
        return Cube.of(function.apply(reference));
    }

    @Override
    public <V> Cube<V> doFlatMap(Function<? super T, Cube<V>> function) {
        return function.apply(reference);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object instanceof Present) {
            Present<?> other = (Present<?>) object;
            return reference.equals(other.reference);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 0x598df91c + reference.hashCode();
    }

    @Override
    public String toString() {
        return "Cube.of(" + reference + ")";
    }

    private static final long serialVersionUID = 0;

}
