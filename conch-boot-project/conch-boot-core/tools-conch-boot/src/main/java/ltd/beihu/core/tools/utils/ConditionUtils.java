package ltd.beihu.core.tools.utils;


public class ConditionUtils {
    public static boolean tryDo(Runnable runnable) {
        try {
            runnable.run();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean tryDo(Runnable primary, Runnable... others) {
        if (tryDo(primary)) {
            return true;
        }
        for (Runnable runnable : others) {
            if (tryDo(runnable))
                return true;
        }
        return false;
    }
}
