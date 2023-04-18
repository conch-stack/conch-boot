package ltd.beihu.core.tools.parallel;

/**
 * @Project: [ops]
 * @Description: [并发任务]
 * @Author: [toming]
 * @CreateDate: [10/13/16 4:27 PM]
 * @Version: [v1.0]
 */
@FunctionalInterface
public interface ParallelTask {
    void run() throws Exception;
}
