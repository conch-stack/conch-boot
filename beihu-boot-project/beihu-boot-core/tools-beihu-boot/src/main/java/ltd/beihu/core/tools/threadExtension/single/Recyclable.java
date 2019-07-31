package ltd.beihu.core.tools.threadExtension.single;

import java.util.Objects;

/**
 * @Project: [ops]
 * @Package: [com.pengshu.common.single]
 * @Description: [循环操作，可以被中断]
 * @Author: [toming]
 * @CreateDate: [9/8/16 3:01 PM]
 * @Version: [v1.0]
 */
@FunctionalInterface
public interface Recyclable {
    boolean run() throws InterruptedException;

    static Recyclable alwaysTrue(Runnable runnable) {
        Objects.requireNonNull(runnable);
        return () -> {
            runnable.run();
            return true;
        };
    }
}
