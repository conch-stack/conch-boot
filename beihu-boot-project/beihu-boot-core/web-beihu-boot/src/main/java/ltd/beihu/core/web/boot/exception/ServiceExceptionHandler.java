package ltd.beihu.core.web.boot.exception;

import com.gitee.hengboy.mybatis.pageable.common.exception.PageableException;
import com.google.common.base.Throwables;
import ltd.beihu.core.tools.code.BasicServiceCode;
import ltd.beihu.core.web.boot.mail.DefaultMailSender;
import ltd.beihu.core.web.boot.response.BasicResponse;
import ltd.beihu.core.web.boot.response.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import java.util.Iterator;

/**
 * 异常处理器
 */
@RestControllerAdvice
public class ServiceExceptionHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionHandler.class);

	@Resource(name = "defaultMailSender")
	private DefaultMailSender defaultMailSender;

	/**
	 * 自定义异常
	 */
	@ExceptionHandler(ServiceException.class)
	public JsonResponse handleServiceException(ServiceException e){
		String stackTraceAsString = Throwables.getStackTraceAsString(e);
		LOGGER.error("【ServiceExceptionHandler - ServiceException】\r\n [{}]", stackTraceAsString);
		if (e.getCode() == BasicServiceCode.ERROR.getCode()
				|| e.getCode() == BasicServiceCode.FAILED.getCode()
				|| e.getCode() == BasicServiceCode.METHOD_NOT_ALLOWED.getCode()
				|| e.getCode() == BasicServiceCode.REJECT.getCode()
				|| e.getCode() == BasicServiceCode.SERVER_ERROR.getCode()
				|| e.getCode() == BasicServiceCode.SERVICE_UNAVAILABLE.getCode()
				|| e.getCode() == BasicServiceCode.TIME_OUT.getCode()) {
			defaultMailSender.warn("【服务异常】", stackTraceAsString);
		}
		return BasicResponse.error(e);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	protected JsonResponse handleIllegalArgumentError(IllegalArgumentException e) {
		String stackTraceAsString = Throwables.getStackTraceAsString(e);
		LOGGER.error("【ServiceExceptionHandler - ServiceException】\r\n [{}]", stackTraceAsString);
		return BasicResponse.error(BasicServiceCode.BAD_REQUEST);
	}

	/**
	 * 分页异常
	 */
	@ExceptionHandler(PageableException.class)
	public JsonResponse handleServiceException(PageableException e){
		String stackTraceAsString = Throwables.getStackTraceAsString(e);
		LOGGER.error("【ServiceExceptionHandler - PageableException】\r\n [{}]", stackTraceAsString);
		return BasicResponse.error(BasicServiceCode.PAGE_INDEX_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public JsonResponse handleValidException(MethodArgumentNotValidException e){
		LOGGER.error("【ServiceExceptionHandler - MethodArgumentNotValidException】\r\n [{}]", Throwables.getStackTraceAsString(e));
		Iterator var2 = e.getBindingResult().getAllErrors().iterator();
		StringBuilder sb = new StringBuilder();
		if (var2.hasNext()) {
			ObjectError error = (ObjectError)var2.next();
			sb.append(BasicServiceCode.BAD_REQUEST.getMesg()).append(",").append(error.getDefaultMessage());
		}
		return BasicResponse.error(new ServiceException(BasicServiceCode.BAD_REQUEST), sb.toString());
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public JsonResponse handleServletRequestParmeException(MissingServletRequestParameterException e){
		LOGGER.error("【ServiceExceptionHandler - MissingServletRequestParameterException】\r\n [{}]", Throwables.getStackTraceAsString(e));
		return BasicResponse.error(new ServiceException(BasicServiceCode.BAD_REQUEST));
	}


	@ExceptionHandler(Exception.class)
	public JsonResponse handleException(Exception e){
		String stackTraceAsString = Throwables.getStackTraceAsString(e);
		LOGGER.error("【ServiceExceptionHandler - Exception】\r\n [{}]", stackTraceAsString);
		defaultMailSender.warn("【系统异常】", stackTraceAsString);
		return BasicResponse.error(new ServiceException(BasicServiceCode.FAILED));
	}
}
