package ltd.beihu.core.tools.parallel;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @Project: [ops]
 * @Description: [抽象并发执行器]
 * @Author: [toming]
 * @CreateDate: [10/13/16 2:59 PM]
 * @Version: [v1.0]
 */
public abstract class AbstractParallelExecutor implements ParallelExecutor {

    private ParallelTaskManager parallelTaskManager = new ParallelTaskManager();

    @Override
    public void await() throws Exception {
        assertTaskNormal();
        parallelTaskManager.await();
        assertTaskNormal();
    }

    @Override
    public void await(long timeout) throws Exception {
        assertTaskNormal();
        parallelTaskManager.await(timeout);
        assertTaskNormal();
    }

    @Override
    public void submit(ParallelTask parallelTask) throws Exception {
        assertTaskNormal();

        parallelTaskManager.born();//开始一个任务
        //任务打包
        Runnable wrapRun = () -> {
            try {
                if (!parallelTaskManager.hasError()) {//任务没有出现异常,才执行
                    parallelTask.run();
                }
            } catch (Exception e) {
                parallelTaskManager.occurError(e);//写入任务异常信息
            } finally {
                parallelTaskManager.die();//结束一个任务
            }
        };
        doSubmit(wrapRun);
    }

    @Override
    public <T> ParallelFuture<T> call(CallableParallelTask<T> parallelTask) {
        parallelTaskManager.born();//开始一个任务
        //任务打包
        Callable<T> wrapRun = () -> {
            try {
                if (!parallelTaskManager.hasError()) {//任务没有出现异常,才执行
                    return parallelTask.run();
                }
            } catch (Exception e) {
                parallelTaskManager.occurError(e);//写入任务异常信息
            } finally {
                parallelTaskManager.die();//结束一个任务
            }
            return null;
        };
        return new ParallelFuture<T>(doSubmit(wrapRun));
    }

    /**
     * 判断当前任务状态正常
     */
    private void assertTaskNormal() throws Exception {
        if (parallelTaskManager.hasError())
            throw parallelTaskManager.getError();
    }

    @Override
    public void close() {
        parallelTaskManager.close();
    }

    /**
     * 提交执行
     */
    protected abstract void doSubmit(Runnable runnable);

    protected abstract <T> Future<T> doSubmit(Callable<T> callable);
}
