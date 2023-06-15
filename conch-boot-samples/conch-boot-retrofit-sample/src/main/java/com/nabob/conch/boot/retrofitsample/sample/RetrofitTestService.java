package com.nabob.conch.boot.retrofitsample.sample;

import com.google.gson.Gson;
import com.nabob.conch.boot.retrofitsample.client.TestLocalRetrofitClient;
import com.nabob.conch.boot.retrofitsample.client.TestRetrofitClient;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Adam
 * @date 2020/8/4
 */
@Service
public class RetrofitTestService {

    @Resource
    private TestRetrofitClient testRetrofitClient;

    @Resource
    private TestLocalRetrofitClient testLocalRetrofitClient;

    public void testSearch() throws IOException {
        Call<Map<Object, Object>> search = testRetrofitClient.search();
        String body = new Gson().toJson(search.execute().body());
        System.out.println(body);
    }

    public void testString() throws IOException {
        Call<String> search = testRetrofitClient.testString();
        String body = search.execute().body();
        System.out.println(body);
    }

    public void testString2() throws IOException {
        Call<String> search = testRetrofitClient.testString2();
        String body = search.execute().body();
        System.out.println(body);
    }

    public void testString4() throws IOException, ExecutionException, InterruptedException {
        CompletableFuture<String> search = testRetrofitClient.testString4();
        String body = search.get();
        System.out.println(body);
    }


    public void testRemoteToLocalCall() throws IOException, ExecutionException, InterruptedException {
        Call<Map<String, String>> search = testLocalRetrofitClient.search();
        String body = new Gson().toJson(search.execute().body());
        System.out.println(body);
    }

    public void testVoid() {
        testLocalRetrofitClient.testVoid();
    }
}
