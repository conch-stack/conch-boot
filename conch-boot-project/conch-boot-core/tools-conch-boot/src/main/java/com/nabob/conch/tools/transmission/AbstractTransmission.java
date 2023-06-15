package com.nabob.conch.tools.transmission;

import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 继承此类,应至少保证存在某种可能触发next.signal(),否则应重写doWait()
 */
public abstract class AbstractTransmission implements Transmission {

    //main lock
    protected ReentrantLock lock;

    //[获取下一个]的条件
    protected Condition next;

    //当前所有正在请求的计数
    private volatile int acquireCnt = 0;

    public AbstractTransmission() {
        this(false);
    }

    public AbstractTransmission(boolean fair) {
        lock = new ReentrantLock(fair);
        next = lock.newCondition();
    }

    @Override
    public void acquire() {
        lock.lock();
        try {
            acquireCnt++;
            while (!acquirable()) {
                try {
                    doWait();
                } catch (InterruptedException e) {
                    ExceptionUtils.wrapAndThrow(e);
                }
            }
            onAcquired();
        } finally {
            acquireCnt--;
            lock.unlock();
        }
    }

    protected void doWait() throws InterruptedException {
        next.await();
    }

    protected abstract void onAcquired();

    protected abstract boolean acquirable();

    @Override
    public boolean tryAcquire() {
        lock.lock();
        try {
            acquireCnt++;
            if (acquirable()) {
                onAcquired();
                return true;
            } else {
                return false;
            }
        } finally {
            acquireCnt--;
            lock.unlock();
        }
    }

    @Override
    public boolean tryAcquire(Long timeout, TimeUnit unit) {
        Long maxWait = System.currentTimeMillis() + timeout;
        try {
            if (lock.tryLock(timeout, unit)) {
                try {
                    acquireCnt++;
                    long nanos = unit.toNanos(maxWait - System.currentTimeMillis());
                    while (!acquirable()) {
                        if (nanos <= 0)
                            return false;
                        nanos = next.awaitNanos(nanos);
                    }
                    onAcquired();
                    return true;
                } finally {
                    acquireCnt--;
                    lock.unlock();
                }
            } else {
                return false;
            }
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public int acquireCnt() {
        return acquireCnt;
    }
}
