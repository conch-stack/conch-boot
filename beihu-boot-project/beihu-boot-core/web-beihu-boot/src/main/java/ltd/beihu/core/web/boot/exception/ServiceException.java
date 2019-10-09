package ltd.beihu.core.web.boot.exception;

import ltd.beihu.core.tools.code.DefaultServiceCode;
import ltd.beihu.core.tools.code.ServiceCode;

import java.text.MessageFormat;

/**
 * @Author: zjz
 * @Desc:
 * @Date: 2019/1/7
 * @Version: V1.0.0
 */
public class ServiceException extends RuntimeException implements ServiceCode {

    private ServiceCode error;

    public ServiceException(ServiceCode error) {
        this.error = error;
    }

    public ServiceException(ServiceCode error, String detail) {
        this.error = new DefaultServiceCode(error.getCode(), error.getDesc(), detail);
    }

    public ServiceCode getError() {
        return error;
    }

    @Override
    public String getDesc() {
        return error.getDesc();
    }

    @Override
    public void setDesc(String desc) {
        error.setDesc(desc);
    }

    @Override
    public String getMesg() {
        return error.getMesg();
    }

    @Override
    public void setMesg(String mesg) {
        error.setMesg(mesg);
    }

    @Override
    public void setCode(int code) {
        error.setCode(code);
    }

    @Override
    public int getCode() {
        return error.getCode();
    }

    /**
     * Returns the detail message string of this throwable.
     *
     * @return the detail message string of this {@code Throwable} instance
     * (which may be {@code null}).
     */
    @Override
    public String getMessage() {
        return MessageFormat.format("code:{0},desc:{1},msg:{2}", getCode(), getDesc(), getMesg());
    }
}
