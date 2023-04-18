package ltd.beihu.core.beihubootretrofitsample.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author Adam
 * @date 2020/8/5
 */
@RestController
@RequestMapping("/test")
public class TestLocalCtrl {

    @Autowired
    private RetrofitTestService retrofitTestService;

    /**
     * Mock 本地接口
     */
    @GetMapping
    public Map<String, String> getRs() {
        Map<String, String> rs = new HashMap<>();
        rs.put("test1", "111");
        rs.put("test2", "222");
        return rs;
    }

    /**
     * Mock 本地接口 2 Void返回
     */
    @GetMapping("/void")
    public void getVoid() {
        System.out.println("test void call");
    }

    /**
     * 测试远程调用
     */
    @GetMapping("/remote")
    public String testRemoteCall() {
        try {
            retrofitTestService.testSearch();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 测试远程转本地调用
     */
    @GetMapping("/local")
    public String testLocalCall() {
        try {
            retrofitTestService.testRemoteToLocalCall();
        } catch (ExecutionException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     * 测试 Void 返回
     */
    @GetMapping("/testVoid")
    public String testVoid() {
        retrofitTestService.testVoid();
        return "success";
    }

}
