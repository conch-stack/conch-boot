package ltd.beihu.core.tools.threadExtension.single;

/**
 * @Project: [ops]
 * @Package: [com.pengshu.common.single]
 * @Description: [单线程执行]
 * @Author: [toming]
 * @CreateDate: [9/8/16 11:40 AM]
 * @Version: [v1.0]
 */
public class SingleRun extends AbstractSingleThread {
    private Runnable runnable;

    public SingleRun(String name) {
        this.name = name;
    }

    public SingleRun setRunnable(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    protected void run() {
        runnable.run();
    }

}
