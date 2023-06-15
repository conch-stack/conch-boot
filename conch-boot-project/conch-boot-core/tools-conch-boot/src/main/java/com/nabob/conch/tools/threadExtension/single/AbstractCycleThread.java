package com.nabob.conch.tools.threadExtension.single;

/**
 * @Project: [ops]
 * @Description: [抽象单线程循环执行类]
 * @Author: [toming]
 * @CreateDate: [9/13/16 2:10 PM]
 * @Version: [v1.0]
 */
public abstract class AbstractCycleThread extends AbstractSingleThread {
    private boolean keep = true;

    protected boolean keepRunning() {
        return keep && !Thread.currentThread().isInterrupted();
    }

    @Override
    protected void beforeRun() {
        keep = true;
    }

    @Override
    public void interrupt() {
        keep = false;
        super.interrupt();
    }

    @Override
    public void stop() {
        keep = false;
        super.stop();
    }
}
