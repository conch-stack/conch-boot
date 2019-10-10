package ltd.beihu.core.tools.parallel;

import ltd.beihu.core.tools.utils.LockUtils;

import java.io.Closeable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Project: [ops]
 * @Description: [并发任务管理器]
 * @Author: [toming]
 * @CreateDate: [10/13/16 3:09 PM]
 * @Version: [v1.0]
 */
public class ParallelTaskManager implements Closeable {
    private AtomicInteger taskNum = new AtomicInteger();//当前运行中任务数量
    private ReentrantLock lock = new ReentrantLock();
    private Condition completed = lock.newCondition();
    private boolean occurError = false;
    private Exception exception = null;

    /**
     * 开始一个任务
     */
    public void born() {
        taskNum.incrementAndGet();
    }

    /**
     * 结束一个任务
     */
    public void die() {
        if (taskNum.decrementAndGet() == 0) {
            LockUtils.runWithLock(lock, () -> completed.signalAll());
        }
    }

    /**
     * 写入异常信息
     */
    public void occurError(Exception e) {
        LockUtils.runWithLock(lock, () -> {
            occurError = true;
            exception = e;
        });
    }

    /**
     * 是否发生错误
     */
    public boolean hasError() {
        return occurError;
    }

    /**
     * 获取发生的错误
     */
    public Exception getError() {
        return exception;
    }

    /**
     * 等待所有子任务完成
     */
    public void await() {
        if (taskNum.get() != 0) {
            try {
                lock.lock();
                try {
                    completed.await();
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 等待所有子任务完成
     */
    public void await(long timeout) {
        if (taskNum.get() != 0) {
            lock.lock();
            try {
                completed.await(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    @Override
    public void close() {
        LockUtils.runWithLock(lock, () -> completed.signalAll());
    }
}
