package ltd.beihu.core.retrofit.boot.interceptor;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

public class HttpLogInterceptor implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(HttpLogInterceptor.class);

    private static final String T_HEAD = "║ ";

    private static final String F_BREAK = " %n";
    private static final String F_URL = " %s";
    private static final String F_TIME = T_HEAD + "Elapsed Time: %.1fms";
    private static final String F_HEADERS = T_HEAD + "Headers: %s";
    private static final String F_RESPONSE = "Response: %d";
    private static final String F_BODY = "Body: %s";


    private static final String U_BREAKER = F_BREAK + "╔═════════════════════════════════════════════════════════════════════════" + F_BREAK + T_HEAD;
    private static final String D_BREAKER = "╚═════════════════════════════════════════════════════════════════════════" + F_BREAK;

    private static final String F_REQUEST_WITHOUT_BODY = F_URL + F_BREAK + F_TIME + F_BREAK + F_HEADERS + F_BREAK + T_HEAD;
    private static final String F_REQUEST_WITH_BODY = F_URL + F_BREAK + F_TIME + F_BREAK + F_HEADERS + F_BREAK + T_HEAD + F_BODY + F_BREAK;
    private static final String F_RESPONSE_WITH_BODY = T_HEAD + F_BREAK + T_HEAD + F_RESPONSE + F_BREAK + F_HEADERS + F_BODY + F_BREAK + D_BREAKER;


    private static String slimString(String source) {
        int end = source.length() >= 1000 ? 1000 : source.length();
        return source.substring(0, end).concat(" 、、、、、、 [ log print char max length = 1000 ] ");
    }

    private static void processeLog(Request request, double elapsedTime, Response response, String content) {
        if (request.method().equals("GET")) {
            logger.info(String.format(U_BREAKER + "GET: " + F_REQUEST_WITHOUT_BODY + F_RESPONSE_WITH_BODY, request.url(), elapsedTime, ConverterHeaders(request.headers().toString()), response.code(), ConverterHeaders(response.headers().toString()), content));
        } else {
            logger.info(String.format(U_BREAKER + request.method() + F_REQUEST_WITH_BODY + F_RESPONSE_WITH_BODY, request.url().toString(), elapsedTime, ConverterHeaders(request.headers().toString()), stringifyRequestBody(request), response.code(), ConverterHeaders(response.headers().toString()), content));
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long startTime = System.nanoTime();

        Response response = chain.proceed(request);

        double elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);


        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        String content = "";

        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                //todo:!check,how it works
                bodyEncoded(request.headers());
            } catch (UnsupportedCharsetException e) {
                content = "Couldn't decode the response body; charset is likely malformed.";
                processeLog(request, elapsedTime, response, slimString(content));
                return response;
            }
        }

        if (!isPlaintext(buffer)) {
            content = "HTTP (binary " + buffer.size() + "-byte body omitted)";
            processeLog(request, elapsedTime, response, slimString(content));
            return response;
        }

        if (contentLength != 0) {
            // 使用复制Buffer的方式
            // content = buffer.clone().readString(charset);

            //重构Response的方式
            content = responseBody.string();
            response = response.newBuilder()
                    .body(ResponseBody.create(contentType, content))
                    .build();
        }

        processeLog(request, elapsedTime, response, slimString(content));
        return response;
    }

    private static String ConverterHeaders(String str) {
        return StringUtils.replace(str, "\n", "\n" + T_HEAD);
    }

    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private static String stringifyRequestBody(Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            RequestBody requestBody = copy.body();
            if (requestBody != null) {
                requestBody.writeTo(buffer);
                return buffer.readUtf8();
            } else {
                return "";
            }
        } catch (final IOException e) {
            return "IOException! Cannot format requestBody";
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
