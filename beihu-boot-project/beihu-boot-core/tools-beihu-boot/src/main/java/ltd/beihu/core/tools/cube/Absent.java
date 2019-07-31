package ltd.beihu.core.tools.cube;

import ltd.beihu.core.tools.utils.Assert;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Absent<T> extends AbstractCube<T> {

    @SuppressWarnings("unchecked") // implementation is "fully variant"
    static <T> Cube<T> withType() {
        return new Absent();
    }

    private Absent() {
    }

    @Override
    public boolean checkPresent() {
        return false;
    }

    @Override
    public T doGet() {
        throw new IllegalStateException("Cube.get() cannot be called on an absent value");
    }

    @Override
    public T doOr(T defaultValue) {
        return defaultValue;
    }

    @SuppressWarnings("unchecked") // safe covariant cast
    @Override
    public Cube<T> doOr(Cube<? extends T> secondChoice) {
        Assert.notNull(secondChoice);
        return (Cube<T>) secondChoice;
    }

    @Override
    public T doOr(Supplier<? extends T> supplier) {
        return supplier.get();
    }

    @Override
    @Nullable
    public T doOrNull() {
        return null;
    }

    @Override
    public Set<T> doAsSet() {
        return Collections.emptySet();
    }

    @Override
    public <V> Cube<V> doMap(Function<? super T, V> function) {
        return withType();
    }

    @Override
    public <V> Cube<V> doFlatMap(Function<? super T, Cube<V>> function) {
        return withType();
    }

    @Override
    public boolean equals(@Nullable Object object) {
        return object == this;
    }

    @Override
    public int hashCode() {
        return 0x79a31aac;
    }

    @Override
    public String toString() {
        return "Cube.absent()";
    }

    private static final long serialVersionUID = 0;

    @Override
    public void doIfPresent(Consumer<T> consumer) {
    }
}
