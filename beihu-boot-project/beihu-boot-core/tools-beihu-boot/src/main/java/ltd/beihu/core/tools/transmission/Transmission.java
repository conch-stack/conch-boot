package ltd.beihu.core.tools.transmission;


import java.util.concurrent.TimeUnit;

public interface Transmission {
    void acquire();

    boolean tryAcquire();

    boolean tryAcquire(Long timeout, TimeUnit timeUnit);

    int acquireCnt();

}
