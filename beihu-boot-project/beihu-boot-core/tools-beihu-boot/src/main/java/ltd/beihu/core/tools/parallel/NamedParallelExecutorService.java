package ltd.beihu.core.tools.parallel;

import ltd.beihu.core.tools.constant.SysConfig;
import ltd.beihu.core.tools.utils.ThreadPoolUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @Project: [ops]
 * @Description: [命名的并发执行器服务]
 * @Author: [toming]
 * @CreateDate: [10/13/16 3:27 PM]
 * @Version: [v1.0]
 */
public class NamedParallelExecutorService implements ParallelExecutorService {

    private ExecutorService threadPool;

    public NamedParallelExecutorService() {
        threadPool = ThreadPoolUtils.createPool(SysConfig.DEFAULT_POOL_SIZE, getClass());
    }

    public NamedParallelExecutorService(String name) {
        threadPool = ThreadPoolUtils.createPool(SysConfig.DEFAULT_POOL_SIZE, name);
    }

    public NamedParallelExecutorService(String name, int concurrency) {
        threadPool = ThreadPoolUtils.createPool(concurrency, name);
    }

    @Override
    public void concurrency(int concurrency) {
        ThreadPoolUtils.setPoolSize(threadPool, concurrency);
    }

    @Override
    public ParallelExecutor createParallelExecutor() {
        return new NamedParallelExecutor();
    }

    public class NamedParallelExecutor extends AbstractParallelExecutor {
        @Override
        protected void doSubmit(Runnable runnable) {
            threadPool.submit(runnable);
        }

        @Override
        protected <T> Future<T> doSubmit(Callable<T> callable) {
            return threadPool.submit(callable);
        }

    }
}
