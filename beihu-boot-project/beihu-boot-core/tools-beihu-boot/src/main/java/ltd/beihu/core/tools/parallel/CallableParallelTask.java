package ltd.beihu.core.tools.parallel;

/**
 * @Project: [ops]
 * @Package: [com.pengshu.common.parallel]
 * @Description: [带返回值的并发任务]
 * @Author: [toming]
 * @CreateDate: [10/13/16 4:27 PM]
 * @Version: [v1.0]
 */
@FunctionalInterface
public interface CallableParallelTask<T> {
    T run();
}
