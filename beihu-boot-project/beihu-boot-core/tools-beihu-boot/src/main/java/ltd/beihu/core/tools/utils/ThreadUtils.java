package ltd.beihu.core.tools.utils;

/**
 * 线程工具类
 */
public class ThreadUtils {
    public static void closeIfPossible(Thread thread) {
        if (thread.isAlive() && !thread.isInterrupted()) {
            thread.interrupt();
        }
    }
}
