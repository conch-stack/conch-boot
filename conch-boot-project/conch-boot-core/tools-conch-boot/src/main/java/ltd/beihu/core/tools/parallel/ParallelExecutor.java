package ltd.beihu.core.tools.parallel;

import java.io.Closeable;

/**
 * @Project: [ops]
 * @Description: [并发执行器]
 * @Author: [toming]
 * @CreateDate: [10/12/16 10:27 AM]
 * @Version: [v1.0]
 */
public interface ParallelExecutor extends Closeable {
    /**
     * 等待子任务全部结束
     *
     * @throws Exception -任务执行过程中出现的异常
     */
    void await() throws Exception;

    /**
     * 等待子任务全部完成
     *
     * @param time -最大等待时长
     * @throws Exception -子任务出现异常
     */
    void await(long time) throws Exception;

    /**
     * 提交子任务
     *
     * @param parallelTask -子任务
     */
    void submit(ParallelTask parallelTask) throws Exception;

    <T> ParallelFuture<T> call(CallableParallelTask<T> parallelTask);
}
