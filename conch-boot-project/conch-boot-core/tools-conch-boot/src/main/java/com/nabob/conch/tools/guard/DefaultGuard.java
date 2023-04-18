package com.nabob.conch.tools.guard;

import com.nabob.conch.tools.function.Generator;

public class DefaultGuard extends AbstractGuard {

    public DefaultGuard() {
        super(1);
    }

    public DefaultGuard(String name) {
        super(name, 1);
    }

    public void linger(long linger) {
        this.linger = linger;
    }

    private Generator<Boolean> checkHandle;

    public void checkMaster(Generator<Boolean> checkHandle) {
        this.checkHandle = checkHandle;
    }

    @Override
    protected boolean checkMaster() {
        return checkHandle == null ? true : checkHandle.generate();
    }

    private Runnable guardHandle;

    public void onGuard(Runnable guardHandle) {
        this.guardHandle = guardHandle;
    }

    @Override
    protected void onGuard() {
        if (guardHandle != null) {
            guardHandle.run();
        }
    }

    private Runnable normalHandle;

    public void onNormal(Runnable normalHandle) {
        this.normalHandle = normalHandle;
    }

    @Override
    protected void onNormal() {
        if (this.normalHandle != null) {
            this.normalHandle.run();
        }
    }
}
