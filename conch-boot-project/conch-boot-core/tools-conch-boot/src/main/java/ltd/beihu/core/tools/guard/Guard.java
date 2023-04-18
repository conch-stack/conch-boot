package ltd.beihu.core.tools.guard;

public interface Guard {

    /**
     * 当前是否为守护状态
     */
    boolean isGuard();

    /**
     * 尝试唤醒守护者
     */
    void awake();

    void pause();
}
