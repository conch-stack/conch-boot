package com.nabob.conch.tools.transmission;


import java.util.concurrent.TimeUnit;

/**
 * 速度限制:
 * 这里用的控制单位是ms,如果精度不够,再实现纳秒级
 */
public class RateTransmission extends AbstractTransmission {

    //一个请求需要多少毫秒
    private volatile Long ms_pre_one;

    //下一个请求可以进入的时间戳
    private volatile Long next_stamp;

    public RateTransmission(Long pre, TimeUnit timeUnit) {
        this(pre, timeUnit, false);
    }

    public RateTransmission(Long pre, TimeUnit timeUnit, boolean fair) {
        super(fair);
        if (pre < 0) throw new IllegalArgumentException();
        if (pre == 0)
            ms_pre_one = (long) Integer.MAX_VALUE;
        else if (pre == -1)
            ms_pre_one = -1L;
        else
            ms_pre_one = timeUnit.toMillis(1) / pre;
        next_stamp = System.currentTimeMillis();
    }

    @Override
    protected void onAcquired() {
        next_stamp = System.currentTimeMillis() + ms_pre_one;
        next.signal();
    }

    @Override
    protected boolean acquirable() {
        return System.currentTimeMillis() > next_stamp;
    }

    @Override
    protected void doWait() throws InterruptedException {
        Long wait_time = next_stamp - System.currentTimeMillis();
        if (wait_time > 0) {
            next.await(wait_time, TimeUnit.MILLISECONDS);
        }
    }

    public void shift(Long pre, TimeUnit timeUnit) {
        ms_pre_one = timeUnit.toMillis(1) / pre;
        next_stamp = System.currentTimeMillis() + ms_pre_one >> 1;
        lock.lock();
        try {
            next_stamp = System.currentTimeMillis() + ms_pre_one >> 1;
            next.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void shift(Double pre, TimeUnit timeUnit) {
        ms_pre_one = ((Double) (timeUnit.toMillis(1) / pre)).longValue();
        next_stamp = System.currentTimeMillis() + ms_pre_one / 2;
        lock.lock();
        try {
            next_stamp = System.currentTimeMillis() + ms_pre_one / 2;
            next.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
