package ltd.beihu.core.beihubootwebsample.http;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TestHttpClient {

    @POST(value = "/app/statistics/buyQuantity")
    Call<TestHttpRs> test(@Body TestHttpDto testHttpDto);

}
