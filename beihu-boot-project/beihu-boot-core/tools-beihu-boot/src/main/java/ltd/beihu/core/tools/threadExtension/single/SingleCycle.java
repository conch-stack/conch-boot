package ltd.beihu.core.tools.threadExtension.single;

/**
 * @Project: [ops]
 * @Package: [com.pengshu.common.single]
 * @Description: [单线程循环执行]
 * @Author: [toming]
 * @CreateDate: [9/8/16 11:40 AM]
 * @Version: [v1.0]
 */
public class SingleCycle extends AbstractCycleThread {
    private Recyclable runnable;

    public SingleCycle() {
    }

    public SingleCycle(String name) {
        this.name = name;
    }

    public SingleCycle setRunnable(Recyclable runnable) {
        this.runnable = runnable;
        return this;
    }

    protected void run() throws InterruptedException {
        while (keepRunning() && runnable.run()) {
            Thread.yield();
        }
    }
}
