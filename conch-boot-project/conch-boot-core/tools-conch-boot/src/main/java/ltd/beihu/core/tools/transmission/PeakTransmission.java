package ltd.beihu.core.tools.transmission;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.concurrent.locks.Condition;

/**
 * 峰值限制:
 * 每次操作完成需要及时release
 */
public class PeakTransmission extends AbstractTransmission {

    private int acquires = 0;
    private volatile int peak;
    private boolean errorReleaseNonePermit;

    protected Condition completed;

    public PeakTransmission(int peak) {
        if (peak < 0) throw new IllegalArgumentException();
        this.peak = peak;
        completed = lock.newCondition();
    }

    public PeakTransmission(int peak, boolean fair) {
        super(fair);
        if (peak < 0) throw new IllegalArgumentException();
        this.peak = peak;
        completed = lock.newCondition();
    }

    @Override
    protected void onAcquired() {
        ++acquires;
    }

    @Override
    protected boolean acquirable() {
        return acquires < peak;
    }

    public void release() {
        lock.lock();
        try {
            if (acquires == 0) {
                if (errorReleaseNonePermit) {
                    throw new IllegalStateException("none permit need release!");
                }
            } else {
                --acquires;
                next.signal();
                if (acquires == 0) {
                    completed.signalAll();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void setPeak(int peak) {
        lock.lock();
        try {
            if (this.peak < peak) {
                this.peak = peak;
                next.signalAll();
            } else {
                this.peak = peak;
            }
        } finally {
            lock.unlock();
        }
    }

    public void waitCompleted() {
        lock.lock();
        try {
            while (acquires != 0) {
                try {
                    completed.await();
                } catch (InterruptedException e) {
                    ExceptionUtils.wrapAndThrow(e);
                }
            }
        } finally {
            lock.unlock();
        }

    }
}
