package com.nabob.conch.tools.threadExtension.single;

import com.nabob.conch.tools.utils.Assert;
import com.nabob.conch.tools.utils.MathUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * @Project: [ops]
 * @Description: [单线程无限循环类]
 * @Author: [toming]
 * @CreateDate: [9/13/16 1:57 PM]
 * @Version: [v1.0]
 */
public class SingleEndless extends AbstractCycleThread {
    private Runnable runnable;

    private int step = 5;//一个错误周期步长

    private Long sleep = 1000L;//每次挂起时间 默认1s

    private boolean breakInException = false;

    public SingleEndless() {
    }

    public SingleEndless(String name) {
        this.name = name;
    }

    public SingleEndless setRunnable(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public SingleEndless setStep(int step) {
        Assert.isTrue(step > 0, "step should not be negative");
        this.step = step;
        return this;
    }

    public SingleEndless setSleep(Long sleep) {
        Assert.isTrue(sleep > 0, "sleep should not be negative");
        this.sleep = sleep;
        return this;
    }

    public void setBreakInException(boolean breakInException) {
        this.breakInException = breakInException;
    }

    @Override
    protected void run() throws InterruptedException {
        int errorTime = 0;
        long period = 0;
        long start;
        while (keepRunning()) {
            try {
                if (errorTime >= step) {//错误次数太多,多休眠一次
                    Thread.sleep((period + Long.max(sleep, 50)) * MathUtils.fix(step, errorTime));
                }
                start = System.currentTimeMillis();
                runnable.run();
                period = MathUtils.fix(period, System.currentTimeMillis() - start);
                Thread.sleep(sleep);
                errorTime = 0;
            } catch (InterruptedException e) {
                return;
            } catch (Exception e) {
                logger.error("an exception occurred in the [{}] when call the runnable:[{}].\n\n",
                        name, ExceptionUtils.getStackTrace(e));
                if (breakInException) {
                    return;
                }
                ++errorTime;
            } finally {
                Thread.yield();
            }
        }
    }
}
