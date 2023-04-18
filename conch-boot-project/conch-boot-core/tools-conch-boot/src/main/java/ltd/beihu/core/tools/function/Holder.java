package ltd.beihu.core.tools.function;

import java.util.function.Function;

/**
 * Created by tangming on 4/1/17.
 */
public class Holder<T> {
    private T data;

    public Holder() {
        this(null);
    }

    public Holder(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void updateData(T data) {
        this.data = data;
    }

    public void updateData(Function<T, T> updater) {
        this.data = updater.apply(data);
    }

    public void clear() {
        this.data = null;
    }
}
