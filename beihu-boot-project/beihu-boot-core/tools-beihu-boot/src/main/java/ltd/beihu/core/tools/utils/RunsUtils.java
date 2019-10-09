package ltd.beihu.core.tools.utils;

import com.google.common.base.Predicates;
import ltd.beihu.core.tools.function.RunnableWithError;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class RunsUtils {

    public static Retry retry() {
        return new RetryBuild();
    }

    public static interface Retry {

        <T> RetryCall<T> callWith(CallAble<T> supplier);

        RetryRun runWith(RunnableWithError runnable);

        <T> RetryCallerBuild<T> caller();

        RetryRunner runner();

        Retry retryOn(Class<? extends Throwable> exception);

        Retry maxRetry(int maxRetryTimes);

        Retry onException(Consumer<Throwable> onException);
    }

    public static interface RetryCallerBuild<T> extends RetryCaller<T> {

        RetryCallerBuild<T> onFailure(Supplier<T> failureHandle);

        RetryCallerBuild<T> retryOn(Predicate<T> predicate);
    }

    public static interface RetryCaller<T> {
        T call(CallAble<T> supplier);
    }

    public static interface RetryCall<T> {
        RetryCall<T> retryOn(Predicate<T> predicate);

        RetryCall<T> onFailure(Supplier<T> failureHandle);

        T call();
    }

    public static interface RetryRunnerBuild extends RetryRunner {
        RetryRunnerBuild onFailure(Runnable failureHandle);
    }

    public static interface RetryRunner {
        void run(RunnableWithError runnable);
    }

    public static interface RetryRun {
        RetryRun onFailure(Runnable failureHandle);

        void run();
    }

    private static class RetryBuild implements Retry {

        private Class<? extends Throwable> exception = Throwable.class;
        private int maxRetryTimes = 1;
        private Consumer<Throwable> onException = (e) -> {
        };

        @Override
        public RetryBuild retryOn(Class<? extends Throwable> exception) {
            this.exception = exception;
            return this;
        }

        @Override
        public RetryBuild maxRetry(int maxRetryTimes) {
            this.maxRetryTimes = maxRetryTimes;
            return this;
        }

        @Override
        public RetryBuild onException(Consumer<Throwable> onException) {
            this.onException = onException;
            return this;
        }

        @Override
        public <T> RetryCall<T> callWith(CallAble<T> supplier) {
            return new RetryCallImpl<T>(exception, maxRetryTimes, onException, Predicates.alwaysFalse(),
                    null, supplier);
        }

        @Override
        public RetryRun runWith(RunnableWithError runnable) {
            return new RetryRunImpl(exception, maxRetryTimes, onException, null, runnable);
        }

        @Override
        public <T> RetryCallerBuild<T> caller() {
            return new RetryCallerImpl<T>(exception, maxRetryTimes, onException, Predicates.alwaysFalse(),
                    null);
        }

        @Override
        public RetryRunner runner() {
            return new RetryRunnerImpl(exception, maxRetryTimes, onException, null);
        }
    }

    private static abstract class AbstractRetry {

        protected Class<? extends Throwable> exception = Throwable.class;
        protected int maxRetryTimes = 1;
        protected Consumer<Throwable> onException = (e) -> {
        };

        public AbstractRetry(Class<? extends Throwable> exception, int maxRetryTimes, Consumer<Throwable> onException) {
            this.exception = exception;
            this.maxRetryTimes = maxRetryTimes;
            this.onException = onException;
        }

        protected void onException(Throwable e) {

            if (!this.exception.isInstance(e)) {
                ExceptionUtils.wrapAndThrow(e);
            }
            if (onException != null) {
                onException.accept(e);
            }
        }

    }

    private static abstract class AbstractRetryCall<T> extends AbstractRetry {

        protected Predicate<T> predicate;
        protected Supplier<T> failureHandle;

        public AbstractRetryCall(Class<? extends Throwable> exception, int maxRetryTimes, Consumer<Throwable> onException, Predicate<T> predicate, Supplier<T> failureHandle) {
            super(exception, maxRetryTimes, onException);
            this.predicate = predicate;
            this.failureHandle = failureHandle;
        }

        public T call(CallAble<T> supplier) {
            for (int i = 0; i < maxRetryTimes; i++) {
                try {
                    T t = supplier.call();
                    if (predicate != null && predicate.test(t)) {
                        continue;
                    }
                    return t;
                } catch (Throwable e) {
                    onException(e);
                }
            }
            return failureHandle == null ? null : failureHandle.get();
        }
    }

    private static class RetryCallImpl<T> extends AbstractRetryCall<T> implements RetryCall<T> {
        private CallAble<T> callAble;

        public RetryCallImpl(Class<? extends Throwable> exception, int maxRetryTimes, Consumer<Throwable> onException, Predicate<T> predicate, Supplier<T> failureHandle, CallAble<T> callAble) {
            super(exception, maxRetryTimes, onException, predicate, failureHandle);
            this.callAble = callAble;
        }

        @Override
        public RetryCall<T> retryOn(Predicate<T> predicate) {
            this.predicate = predicate;
            return this;
        }

        @Override
        public RetryCall<T> onFailure(Supplier<T> failureHandle) {
            this.failureHandle = failureHandle;
            return this;
        }

        @Override
        public T call() {
            return call(callAble);
        }
    }

    private static class RetryCallerImpl<T> extends AbstractRetryCall<T> implements RetryCallerBuild<T> {
        public RetryCallerImpl(Class<? extends Throwable> exception, int maxRetryTimes, Consumer<Throwable> onException, Predicate<T> predicate, Supplier<T> failureHandle) {
            super(exception, maxRetryTimes, onException, predicate, failureHandle);
        }

        @Override
        public RetryCallerBuild<T> retryOn(Predicate<T> predicate) {
            this.predicate = predicate;
            return this;
        }

        @Override
        public RetryCallerBuild<T> onFailure(Supplier<T> failureHandle) {
            this.failureHandle = failureHandle;
            return this;
        }
    }

    private static abstract class AbstractRetryRun extends AbstractRetry {

        protected Runnable failureHandle;

        public AbstractRetryRun(Class<? extends Throwable> exception, int maxRetryTimes, Consumer<Throwable> onException, Runnable failureHandle) {
            super(exception, maxRetryTimes, onException);
            this.failureHandle = failureHandle;
        }

        public void run(RunnableWithError runnable) {
            for (int i = 0; i < maxRetryTimes; i++) {
                try {
                    runnable.run();
                    return;
                } catch (Throwable e) {
                    onException(e);
                }
            }
            if (failureHandle != null) {
                failureHandle.run();
            }
        }
    }

    private static class RetryRunImpl extends AbstractRetryRun implements RetryRun {
        private RunnableWithError runnable;

        public RetryRunImpl(Class<? extends Throwable> exception, int maxRetryTimes, Consumer<Throwable> onException, Runnable failureHandle, RunnableWithError runnable) {
            super(exception, maxRetryTimes, onException, failureHandle);
            this.runnable = runnable;
        }


        @Override
        public RetryRun onFailure(Runnable failureHandle) {
            this.failureHandle = failureHandle;
            return this;
        }

        @Override
        public void run() {
            run(runnable);
        }
    }

    private static class RetryRunnerImpl extends AbstractRetryRun implements RetryRunnerBuild {
        public RetryRunnerImpl(Class<? extends Throwable> exception, int maxRetryTimes, Consumer<Throwable> onException, Runnable failureHandle) {
            super(exception, maxRetryTimes, onException, failureHandle);
        }


        @Override
        public RetryRunnerBuild onFailure(Runnable failureHandle) {
            this.failureHandle = failureHandle;
            return this;
        }

        @Override
        public void run(RunnableWithError runnable) {
            super.run(runnable);
        }
    }

}
