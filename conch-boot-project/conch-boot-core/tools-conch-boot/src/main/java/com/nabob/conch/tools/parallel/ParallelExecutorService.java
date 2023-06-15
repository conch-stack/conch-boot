package com.nabob.conch.tools.parallel;

/**
 * @Project: [ops]
 * @Description: [并发执行器服务]
 * @Author: [toming]
 * @CreateDate: [10/14/16 2:56 PM]
 * @Version: [v1.0]
 */
public interface ParallelExecutorService {
    /**
     * 设置并发量
     *
     * @param concurrency -并发量
     */
    void concurrency(int concurrency);

    ParallelExecutor createParallelExecutor();
}
