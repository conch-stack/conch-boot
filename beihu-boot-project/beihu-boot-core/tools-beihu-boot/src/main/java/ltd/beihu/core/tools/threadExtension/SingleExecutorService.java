package ltd.beihu.core.tools.threadExtension;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class SingleExecutorService extends AbstractExecutorService {
    //标识线程运行状态,true:ready,false:running
    private AtomicBoolean ready = new AtomicBoolean(true);
    //核心线程
    private Thread coreThread = new CoreThread();
    //核心任务
    private Runnable runnable;

    private volatile boolean shutDown = false;

    public SingleExecutorService() {
        coreThread.setDaemon(true);
    }

    @Override
    public void execute(Runnable command) {
        if (ready.get() && ready.compareAndSet(true, false)) {//可以运行
            if (!shutDown) {
                run(command);
            } else {
                throw new IllegalStateException("this thread is shutDown");
            }
        } else {
            throw new IllegalThreadStateException("this thread is running");
        }
    }

    private void run(Runnable command) {
        this.runnable = command;
        coreThread.start();
    }

    @Override
    public void shutdown() {
        if (shutDown) {
            throw new IllegalStateException("this thread was already shutDown");
        }
        if (!ready.get()) {
            coreThread.interrupt();
        }
    }

    @Override
    public List<Runnable> shutdownNow() {
        if (!ready.get()) {
            coreThread.interrupt();
            return Collections.singletonList(runnable);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean isShutdown() {
        return shutDown;
    }

    @Override
    public boolean isTerminated() {
        return ready.get();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        for (; ; ) {
            if (ready.get())
                return true;
            if (nanos <= 0)
                return false;
            TimeUnit.NANOSECONDS.sleep(5);
            nanos -= 5;
        }
    }

    /**
     * @throws RejectedExecutionException {@inheritDoc}
     * @throws NullPointerException       {@inheritDoc}
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (task == null) throw new NullPointerException();
        if (ready.get() && ready.compareAndSet(true, false)) {
            if (!shutDown) {
                RunnableFuture<T> ftask = newTaskFor(task);
                run(ftask);
                return ftask;
            } else {
                throw new IllegalStateException("this thread is shutDown");
            }
        } else {
            throw new IllegalThreadStateException("this thread is running");
        }
    }

    /**
     * Returns a {@code RunnableFuture} for the given callable task.
     *
     * @param callable the callable task being wrapped
     * @param <T>      the type of the callable's result
     * @return a {@code RunnableFuture} which, when run, will call the
     * underlying callable and which, as a {@code Future}, will yield
     * the callable's result as its result and provide for
     * cancellation of the underlying task
     * @since 1.6
     */
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new FutureTask<T>(callable);
    }


    private class CoreThread extends Thread {

        /**
         * @throws IllegalThreadStateException if the thread is running
         * @throws NullPointerException        if runnable is <tt>null</tt>
         */
        @Override
        public void run() {
            try {
                runnable.run();
            } finally {
                //释放旧任务,设置为就绪状态
                runnable = null;
                ready.set(true);
            }
        }
    }
}
