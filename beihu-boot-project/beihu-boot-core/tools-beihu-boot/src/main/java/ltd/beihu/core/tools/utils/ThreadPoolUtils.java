package ltd.beihu.core.tools.utils;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ThreadPoolUtils {
    private static Logger logger = LoggerFactory.getLogger(ThreadPoolUtils.class);

    public static void setPoolSize(ExecutorService threadPool, Integer poolSize) {
        if (threadPool instanceof ThreadPoolExecutor) {
            ((ThreadPoolExecutor) threadPool).setMaximumPoolSize(poolSize);
            ((ThreadPoolExecutor) threadPool).setCorePoolSize(poolSize);
        }
    }

    public static ExecutorService createPool(int poolSize, Class clazz) {
        return createPool(poolSize, clazz.getSimpleName());
    }

    public static ExecutorService createPool(int poolSize, String name) {
        logger.info("create threadPool:{name:{},poolSize:{}", name, poolSize);
        return new ThreadPoolExecutor(poolSize, poolSize,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                new BasicThreadFactory.Builder().daemon(true)
                        .namingPattern(name + "-%d").build());
    }

    public static ExecutorService createSinglePool(Class clazz) {
        return createPool(1, clazz);
    }

    public static ExecutorService createSinglePool(String name) {
        return createPool(1, name);
    }
}
