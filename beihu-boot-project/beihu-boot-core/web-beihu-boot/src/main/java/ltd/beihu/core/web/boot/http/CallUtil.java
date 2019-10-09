package ltd.beihu.core.web.boot.http;

import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public class CallUtil<T> {
    private CallUtil(Call<T> _call) {
        this._call = _call;
    }

    private Call<T> _call;
    private Call<T> _retryCall;
    private boolean success;
    private int retryTimes = 0;
    private Response<T> response;
    private Consumer<String> failedHandler = s -> {
    };

    private CallUtil<T> execute() {
        Response<T> execute = null;
        try {
            execute = _call.execute();
            if (execute.isSuccessful()) {
                response = execute;
            }
            success = true;
        } catch (IOException e) {
//            Asserts.isTrue(execute.isSuccessful() || retryTimes != 0, BasicServiceCode.TIME_OUT);
            if (_retryCall != null && retryTimes > 0) {
                retryTimes--;
                _call = this._retryCall.clone();
            } else {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    public CallUtil<T> onFail(Consumer<String> consumer) {
        this.failedHandler = consumer;
        return this;
    }

    public CallUtil<T> retryOnFail(int times) {
        _retryCall = _call.clone();
        this.retryTimes = times;
        return this;
    }

    public <R> R readBody(Function<T, R> successHandler) {
        if (_call.isExecuted()) {
            if (response != null && response.isSuccessful()) {
                T body = response.body();
                return successHandler.apply(body);
            } else {
                try {
                    if (response == null) {
                        throw new IllegalStateException("error to request");
                    }
                    this.failedHandler.accept(response.errorBody().string());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        } else {
            do {
                this.execute();
            } while (retryTimes > 0 && !success);
            return this.readBody(successHandler);
        }
    }


    public static <T> CallUtil<T> newCallUtil(Call<T> call) {
        return new CallUtil<>(call);
    }
}