package com.nabob.conch.tools.guard;

import com.nabob.conch.tools.threadExtension.single.SingleCycle;
import com.nabob.conch.tools.threadExtension.single.SingleThread;
import com.nabob.conch.tools.utils.ThreadPoolUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractGuard implements Guard {
    private String name = getClass().getSimpleName();

    private Logger logger = LoggerFactory.getLogger(getClass());

    private volatile boolean guard = false;//标记守护状态

    private SingleThread guardThread = new SingleCycle(getClass().getSimpleName() + "-monitor").setRunnable(this::scan);

    private ExecutorService executorService = ThreadPoolUtils.createSinglePool(getClass());

    //检测失败后等待
    protected long linger;

    public AbstractGuard(String name, long linger) {
        this.name = name;
        this.linger = linger;
    }

    public AbstractGuard(long linger) {
        this.linger = linger;
    }

    @Override
    public boolean isGuard() {
        return guard;
    }

    private boolean scan() throws InterruptedException {
        if (checkMaster()) {
            if (guard) {
                guard = false;
                logger.info(name + "--->normal");
                executorService.submit(this::onNormal);
            }
            return false;
        } else {
            if (!guard) {
                guard = true;
                logger.info(name + "--->guard");
                executorService.submit(this::onGuard);
            }
            if (linger > 0) {
                TimeUnit.MILLISECONDS.sleep(linger);
            }
            return true;
        }
    }

    @Override
    public void awake() {
        guardThread.tryRun();
    }

    @Override
    public void pause() {
        guardThread.interrupt();
    }

    /**
     * 检查宿主状态是否恢复
     */
    protected abstract boolean checkMaster();

    /**
     * 进入守护态
     */
    protected abstract void onGuard();

    /**
     * 恢复正常态
     */
    protected abstract void onNormal();
}
