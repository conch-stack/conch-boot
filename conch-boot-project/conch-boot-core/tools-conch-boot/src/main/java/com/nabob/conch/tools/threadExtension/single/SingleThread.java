package com.nabob.conch.tools.threadExtension.single;

/**
 * @Project: [ops]
 * @Description: [单线程执行类]
 * @Author: [toming]
 * @CreateDate: [9/8/16 3:31 PM]
 * @Version: [v1.0]
 */
public interface SingleThread {
    boolean tryRun();

    boolean isRunning();

    void interrupt();

    /**
     * Stop the thread and releases any system resources associated
     * with it. If the thread is already stopped then invoking this
     * method has no effect.
     * Sure,I hope every thread should call this before garbage collection.
     */
    void stop();
}
