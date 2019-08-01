package ltd.beihu.core.web.boot.code;

import java.util.stream.Stream;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/7
 * @Version: V1.0.0
 */
public enum BasicServiceCode implements ServiceCode {

    SUCCESS(10000, "SUCCESS", "请求成功"),
    ERROR(10001, "SYSTEM_EXCEPTION", "系统异常"),
    FAILED(10002, "FAILED", "请求失败"),

    PAGE_INDEX_ERROR(10003, "PAGE_INDEX_ERROR", "页码必须大于0"),

    // 全局
    UNAUTHORIZED(401, "NO_AUTH", "未授权"),
    BAD_REQUEST(402, "PARAM_ERROR", "请求参数错误"),
    FORBIDDEN(403, "REQUEST_REJECT", "拒绝访问"),
    NOT_FOUND(404, "URI_NOT_FOUND", "资源不存在"),
    METHOD_NOT_ALLOWED(405, "REQUEST_ERROR", "请求错误"),
    TIME_OUT(408, "RESPONSE_OUT_OF_TTME", "响应超时"),

    REJECT(421, "REQUEST_REJECT", "请求异常,拒绝访问"),
    SERVER_ERROR(500, "SERVER_ERRRO", "服务器内部错误，请稍后重试"),
    SERVICE_UNAVAILABLE(503, "SERVER_UNAVAILABLE", "服务不可用，请稍后重试");
    ;

    BasicServiceCode(int code, String desc) {
        this(code, desc, "请求成功");
    }

    BasicServiceCode(int code, String desc, String message) {
        this.code = code;
        this.message = message;
        this.desc = desc;
    }

    private int code;
    private String desc;
    private String message;

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String getMesg() {
        return message;
    }

    @Override
    public void setMesg(String mesg) {
        this.message = message;
    }

    public static BasicServiceCode parse(int code) {
        return Stream.of(values())
                .filter(baseCode -> baseCode.getCode() == code)
                .findFirst()
                .orElse(ERROR);
    }
    public static BasicServiceCode findMessage(String message) {
        return Stream.of(values())
                .filter(baseCode -> baseCode.getMesg().equals(message))
                .findFirst()
                .orElse(ERROR);
    }
}

