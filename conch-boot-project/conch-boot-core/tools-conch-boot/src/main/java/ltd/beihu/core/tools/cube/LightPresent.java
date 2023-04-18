package ltd.beihu.core.tools.cube;

import ltd.beihu.core.tools.utils.Assert;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LightPresent<T> implements Cube<T> {
    private final T reference;

    static <T> Cube<T> with(T reference) {
        return new LightPresent<>(reference);
    }

    private LightPresent(T reference) {
        this.reference = reference;
    }

    @Override
    public Cube<T> onCompleted(Runnable onCompleted) {
        return Present.with(reference).onCompleted(onCompleted);
    }

    @Override
    public Cube<T> onCompleted(Consumer<T> onCompleted) {
        return Present.with(reference).onCompleted(onCompleted);
    }

    @Override
    public boolean isPresent() {
        return true;
    }

    @Override
    public void ifPresent(Consumer<T> consumer) {
        consumer.accept(reference);
    }

    @Override
    public void complete() {

    }

    @Override
    public T get() {
        return reference;
    }

    @Override
    public T or(T defaultValue) {
        Assert.notNull(defaultValue, "use Cube.orNull() instead of Cube.or(null)");
        return reference;
    }

    @Override
    public Cube<T> or(Cube<? extends T> secondChoice) {
        return this;
    }

    @Override
    public T or(Supplier<? extends T> supplier) {
        Assert.notNull(supplier);
        return reference;
    }

    @Override
    public T orNull() {
        return reference;
    }

    @Override
    public Set<T> asSet() {
        return Collections.singleton(reference);
    }

    @Override
    public <V> Cube<V> map(Function<? super T, V> function) {
        return Cube.of(function.apply(reference));
    }

    @Override
    public <V> Cube<V> flatMap(Function<? super T, Cube<V>> function) {
        return function.apply(reference);
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object instanceof LightPresent) {
            LightPresent<?> other = (LightPresent<?>) object;
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
