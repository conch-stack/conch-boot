package ltd.beihu.core.minio.boot;

import io.minio.errors.*;
import ltd.beihu.core.tools.code.BasicServiceCode;
import ltd.beihu.core.web.boot.response.BasicResponse;
import ltd.beihu.core.web.boot.response.JsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MinioEntityExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(MinioEntityExceptionHandler.class);

    @ExceptionHandler(value = {InvalidPortException.class})
    protected JsonResponse handlePortError(InvalidPortException e) {
        LOGGER.error("【Minio Error】The given port number is not valid.");
        return BasicResponse.error(BasicServiceCode.FAILED);
    }

    @ExceptionHandler(value = {InvalidEndpointException.class})
    protected JsonResponse handleEndpointError(InvalidEndpointException e) {
        LOGGER.error("【Minio Error】The given endpoint number is not valid.");
        return BasicResponse.error(BasicServiceCode.FAILED);
    }

    @ExceptionHandler(value = {RegionConflictException.class})
    protected JsonResponse handleRegionConflictError(RegionConflictException e) {
        LOGGER.error("【Minio Error】The passed region conflicts with the one previously specified.");
        return BasicResponse.error(BasicServiceCode.FAILED);
    }

    @ExceptionHandler(value = {NoResponseException.class})
    protected JsonResponse handleNoResponseError(NoResponseException e) {
        LOGGER.error("【Minio Error】No response is received!");
        return BasicResponse.error(BasicServiceCode.FAILED);
    }

    @ExceptionHandler(value = {ErrorResponseException.class})
    protected JsonResponse handleResponseError(ErrorResponseException e) {
        LOGGER.error("【Minio Error】Received an error response while executing the requested operation!");
        return BasicResponse.error(BasicServiceCode.FAILED);
    }

    @ExceptionHandler(value = {InsufficientDataException.class})
    protected JsonResponse handleInsufficientDataError(InsufficientDataException e) {
        LOGGER.error("【Minio Error】Reading given InputStream gets EOFException before reading given length.");
        return BasicResponse.error(BasicServiceCode.FAILED);
    }

    @ExceptionHandler(value = {InvalidBucketNameException.class})
    protected JsonResponse handleBucketNameError(InvalidBucketNameException e) {
        LOGGER.error("【Minio Error】The given bucket name is not valid!");
        return BasicResponse.error(BasicServiceCode.FAILED);
    }

    @ExceptionHandler(value = {InvalidExpiresRangeException.class})
    protected JsonResponse handleExpiresRangeError(InvalidExpiresRangeException e) {
        LOGGER.error("【Minio Error】The given expires value is out of range.");
        return BasicResponse.error(BasicServiceCode.FAILED);
    }

    @ExceptionHandler(value = {InvalidObjectPrefixException.class})
    protected JsonResponse handleObjectPrefixError(InvalidObjectPrefixException e) {
        LOGGER.error("【Minio Error】The given object prefix is not valid");
        return BasicResponse.error(BasicServiceCode.FAILED);
    }

}
