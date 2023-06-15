package com.nabob.conch.tools.threadExtension.single;

import com.nabob.conch.tools.utils.ThreadPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Project: [ops]
 * @Description: [抽象单线程执行类]
 * @Author: [toming]
 * @CreateDate: [9/8/16 11:40 AM]
 * @Version: [v1.0]
 */
public abstract class AbstractSingleThread implements SingleThread {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private ExecutorService singleExecutorService;
    private AtomicBoolean running = new AtomicBoolean(false);//运行状态
    private Future future;
    protected String name = getClass().getSimpleName();

    @Override
    public boolean tryRun() {
        if (!running.get() && running.compareAndSet(false, true)) {
            if (singleExecutorService == null) {
                singleExecutorService = ThreadPoolUtils.createSinglePool(name);
            }
            future = singleExecutorService.submit(this::doRun);
            return true;
        }
        return false;
    }

    private void doRun() {
        try {
            logger.debug("{} start.........\n", name);
            beforeRun();
            //运行前再次确认,running必须置为true
            run();
        } catch (InterruptedException e) {
            logger.error("{} had be interrupt\n", name);
        } finally {
            future = null;
            running.set(false);
            logger.debug("{} end.........\n", name);
        }
    }

    protected void beforeRun() {
    }

    abstract protected void run() throws InterruptedException;

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void interrupt() {
        if (running.compareAndSet(true, false)) {
            if (future != null) {
                future.cancel(true);
            }
        }
    }

    @Override
    public void stop() {
        if (running.compareAndSet(true, false)) {
            if (future != null) {
                future.cancel(true);
            }
            singleExecutorService.shutdown();
        }
    }
}
