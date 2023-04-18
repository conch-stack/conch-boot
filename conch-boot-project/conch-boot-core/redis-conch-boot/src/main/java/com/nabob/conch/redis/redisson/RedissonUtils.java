package com.nabob.conch.redis.redisson;

import org.redisson.api.*;

public class RedissonUtils {

    private RedissonClient redissonClient;

    private String prefix;

    private String lock;

    public RedissonUtils(RedissonClient redissonClient, String prefix, String lock) {
        this.redissonClient = redissonClient;
        this.prefix = prefix;
        this.lock = lock;
    }

    /**
     * Returns lock instance by name.
     * <p>
     * Implements a <b>non-fair</b> locking so doesn't guarantees an acquire order by threads.
     *
     * @param name - name of object
     * @return Lock object
     */
    public RLock getLock(String name) {
        return redissonClient.getLock(formatLockKey(name));
    }

    /**
     * Returns lock instance by name.
     * <p>
     * Implements a <b>fair</b> locking so it guarantees an acquire order by threads.
     *
     * @param name - name of object
     * @return Lock object
     */
    public RLock getFairLock(String name) {
        return redissonClient.getFairLock(formatLockKey(name));
    }

    /**
     * Returns readWriteLock instance by name.
     *
     * @param name - name of object
     * @return Lock object
     */
    public RReadWriteLock getReadWriteLock(String name) {
        return redissonClient.getReadWriteLock(formatLockKey(name));
    }

    /**
     * Returns semaphore instance by name
     *
     * @param name - name of object
     * @return Semaphore object
     */
    public RSemaphore getSemaphore(String name) {
        return redissonClient.getSemaphore(formatLockKey(name));
    }

    /**
     * Returns countDownLatch instance by name.
     *
     * @param name - name of object
     * @return CountDownLatch object
     */
    public RCountDownLatch getCountDownLatch(String name) {
        return redissonClient.getCountDownLatch(formatLockKey(name));
    }

    /**
     * Returns semaphore instance by name.
     * Supports lease time parameter for each acquired permit.
     *
     * @param name - name of object
     * @return PermitExpirableSemaphore object
     */
    public RPermitExpirableSemaphore getPermitExpirableSemaphore(String name) {
        return redissonClient.getPermitExpirableSemaphore(formatLockKey(name));
    }

    /**
     * Returns atomicLong instance by name.
     *
     * @param name - name of object
     * @return AtomicLong object
     */
    RAtomicLong getAtomicLong(String name) {
        return redissonClient.getAtomicLong(formatKey(name));
    }

    /**
     * Returns atomicDouble instance by name.
     *
     * @param name - name of object
     * @return AtomicDouble object
     */
    RAtomicDouble getAtomicDouble(String name) {
        return redissonClient.getAtomicDouble(formatKey(name));
    }

    private String formatKey(String key) {
        return this.prefix + this.lock + key;
    }

    private String formatLockKey(String key) {
        return this.prefix + this.lock + key;
    }

}
