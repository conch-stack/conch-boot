package ltd.beihu.core.beihubootretrofitsample.client;

import com.nabob.conch.retrofit.annotation.RetrofitClient;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.Map;

/**
 * 测试 本地调用
 *
 * @author Adam
 * @date 2020/8/4
 */
@RetrofitClient(name = "testLocalRetrofitClient", applicationName = "${spring.application.name}", url = "https://www.oschina.net")
public interface TestLocalRetrofitClient {

    /**
     * 测试对象json
     */
    @GET(value = "/test")
    Call<Map<String, String>> search();

    /**
     * 测试Void
     * todo 不支持 需定义 CallAdaptor
     */
    @GET(value = "/test/void")
    Void testVoid();
}
