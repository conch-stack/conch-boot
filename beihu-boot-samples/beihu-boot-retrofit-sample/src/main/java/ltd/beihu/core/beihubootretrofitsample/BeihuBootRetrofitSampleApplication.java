package ltd.beihu.core.beihubootretrofitsample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class BeihuBootRetrofitSampleApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(BeihuBootRetrofitSampleApplication.class, args);
    }

}
