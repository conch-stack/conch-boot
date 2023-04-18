package ltd.beihu.core.beihubootretrofitsample.client;

import com.nabob.conch.retrofit.annotation.RetrofitClient;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * 测试 远程调用
 *
 * @author Adam
 * @date 2020/8/4
 */
@RetrofitClient(name = "testRetrofitClient", applicationName = "test", url = "https://www.oschina.net")
public interface TestRetrofitClient {

    /**
     * 测试对象json
     */
    @GET(value = "/action/ajax/get_tool_ad")
    Call<Map<Object, Object>> search();

    /**
     * 测试普通String
     */
    @GET(value = "/news/117669/july-2020-web-server-survey")
    Call<String> testString();

    /**
     * 测试普通String2
     */
    @GET(value = "/action/ajax/get_tool_ad")
    Call<String> testString2();

    /**
     * 测试CompletableFuture String4
     */
    @GET(value = "/action/ajax/get_tool_ad")
    CompletableFuture<String> testString4();
}
