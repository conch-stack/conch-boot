package ltd.beihu.core.tools.locks;

import ltd.beihu.core.tools.utils.LockUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockWithCondition {
    private final ReentrantLock lock;

    private final Condition condition;

    public LockWithCondition() {
        this(new ReentrantLock());
    }

    public LockWithCondition(ReentrantLock lock) {
        this.lock = lock;
        this.condition = lock.newCondition();
    }

    public void await(long time, TimeUnit unit) throws InterruptedException {
        lock.lock();
        try {
            this.condition.await(time, unit);
        } finally {
            lock.unlock();
        }
    }

    public void signal() {
        LockUtils.runWithLock(this.lock, condition::signal);
    }

    public boolean trySignal() {
        return LockUtils.tryRunWithLock(this.lock, condition::signal);
    }
}
