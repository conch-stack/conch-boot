package ltd.beihu.core.beihubootretrofitsample.config;

import ltd.beihu.core.retrofit.boot.annotation.RetrofitClientScan;
import org.springframework.context.annotation.Configuration;

/**
 * 扫描
 *
 * @author Adam
 * @date 2020/8/4
 */
@Configuration
@RetrofitClientScan(basePackages = "ltd.beihu.core.beihubootretrofitsample")
public class Config {
}
