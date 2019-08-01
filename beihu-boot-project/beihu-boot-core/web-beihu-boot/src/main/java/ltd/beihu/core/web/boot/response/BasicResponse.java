package ltd.beihu.core.web.boot.response;

import ltd.beihu.core.web.boot.code.BasicServiceCode;
import ltd.beihu.core.web.boot.code.ServiceCode;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/7
 * @Version: V1.0.0
 */
public class BasicResponse<T> extends AbstractJsonResponse {
    protected T data;

    protected BasicResponse() { }

    public static <T> BasicResponse<T> success() {
        BasicResponse<T> response = new BasicResponse<>();
        response.message = BasicServiceCode.SUCCESS.getMesg();
        response.codeDescription = BasicServiceCode.SUCCESS.getDesc();
        response.code = BasicServiceCode.SUCCESS.getCode();
        return response;
    }

    public static <T> BasicResponse<T> success(T data) {
        BasicResponse<T> response = new BasicResponse<>();
        response.data = data;
        response.message = BasicServiceCode.SUCCESS.getMesg();
        response.codeDescription = BasicServiceCode.SUCCESS.getDesc();
        response.code = BasicServiceCode.SUCCESS.getCode();
        return response;
    }

    public static <T> BasicResponse<T> success(T data, BasicServiceCode code) {
        BasicResponse<T> response = new BasicResponse<>();
        response.data = data;
        response.message = code.getMesg();
        response.codeDescription = code.getDesc();
        response.code = code.getCode();
        return response;
    }

    public static <T> BasicResponse<T> successMesg(String msg) {
        BasicResponse<T> response = new BasicResponse<>();
        response.message = msg;
        response.codeDescription = BasicServiceCode.SUCCESS.getDesc();
        response.code = BasicServiceCode.SUCCESS.getCode();
        return response;
    }

    public static <T> BasicResponse<T> success(T data, String msg) {
        BasicResponse<T> response = new BasicResponse<>();
        response.data = data;
        response.message = msg;
        response.codeDescription = BasicServiceCode.SUCCESS.getDesc();
        response.code = BasicServiceCode.SUCCESS.getCode();
        return response;
    }

    public static <T> BasicResponse<T> error() {
        BasicResponse<T> response = new BasicResponse<>();
        response.message = BasicServiceCode.FAILED.getMesg();
        response.codeDescription = BasicServiceCode.FAILED.getDesc();
        response.code = BasicServiceCode.FAILED.getCode();
        return response;
    }

    public static <T> BasicResponse<T> error(String msg) {
        BasicResponse<T> response = new BasicResponse<>();
        response.message = msg;
        response.codeDescription = BasicServiceCode.FAILED.getDesc();
        response.code = BasicServiceCode.FAILED.getCode();
        return response;
    }

    public static <T> BasicResponse<T> error(ServiceCode serviceError) {
        BasicResponse<T> response = new BasicResponse<>();
        response.code = serviceError.getCode();
        response.message = serviceError.getMesg();
        response.codeDescription = serviceError.getDesc();
        return response;
    }

    public static <T> BasicResponse<T> error(ServiceCode serviceError, String msg) {
        BasicResponse<T> response = new BasicResponse<>();
        response.code = serviceError.getCode();
        response.message = msg;
        response.codeDescription = serviceError.getDesc();
        return response;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
