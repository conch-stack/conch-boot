package ltd.beihu.core.tools.cube;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import ltd.beihu.core.tools.utils.Assert;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class AbstractIterator<T> implements Iterator<T> {
    private AbstractIterator.State state = AbstractIterator.State.NOT_READY;

    protected AbstractIterator() {
    }

    private enum State {
        READY,
        NOT_READY,
        DONE,
        FAILED,
    }

    private T next;

    protected abstract T computeNext();

    @Nullable
    @CanIgnoreReturnValue
    protected final T endOfData() {
        state = AbstractIterator.State.DONE;
        return null;
    }

    @Override
    public final boolean hasNext() {
        Assert.isTrue(state != AbstractIterator.State.FAILED);
        switch (state) {
            case READY:
                return true;
            case DONE:
                return false;
            default:
        }
        return tryToComputeNext();
    }

    private boolean tryToComputeNext() {
        state = AbstractIterator.State.FAILED; // temporary pessimism
        next = computeNext();
        if (state != AbstractIterator.State.DONE) {
            state = AbstractIterator.State.READY;
            return true;
        }
        return false;
    }

    @Override
    public final T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        state = AbstractIterator.State.NOT_READY;
        T result = next;
        next = null;
        return result;
    }

    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}